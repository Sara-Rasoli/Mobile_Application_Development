package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.Gender
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

enum class PlausibilityLevel {
    GOOD,
    REVIEW,
    CONCERNING
}

data class PlausibilityItem(
    val parameterName: String,
    val level: PlausibilityLevel,
    val valueFormatted: String,
    val explanation: String
)

data class PlausibilityReport(
    val overallLevel: PlausibilityLevel,
    val items: List<PlausibilityItem>,
    val crClMlMin: Double? = null,
    val kePredicted: Double? = null,
    val keRatio: Double? = null,
    val isRenalCheckApplicable: Boolean = true,
    val renalCheckNotApplicableReason: String? = null
)

object ClinicalPlausibilityChecker {

    fun evaluate(
        result: TDMResult,
        input: TDMInput? = null,
        patientInfo: PatientInfo? = null
    ): PlausibilityReport {
        val items = mutableListOf<PlausibilityItem>()

        // 1. Renal function cross-check (Cockcroft-Gault & Matzke equations)
        var crCl: Double? = null
        var kePred: Double? = null
        var keRatio: Double? = null
        var isRenalApplicable = true
        var notApplicableReason: String? = null

        if (patientInfo == null) {
            isRenalApplicable = false
            notApplicableReason = "Patient information not provided"
        } else if (patientInfo.isPaediatric) {
            isRenalApplicable = false
            notApplicableReason = "Not applicable for paediatric patients (Cockcroft-Gault formula is for adults only)"
        } else if (patientInfo.ageYears <= 0 || patientInfo.weightKg <= 0.0 || patientInfo.serumCreatinine <= 0.0) {
            isRenalApplicable = false
            notApplicableReason = "Missing or invalid patient age, weight, or serum creatinine"
        } else {
            val scrMgDl = patientInfo.serumCreatinine / 88.4
            var crClVal = ((140.0 - patientInfo.ageYears) * patientInfo.weightKg) / (72.0 * scrMgDl)
            if (patientInfo.gender == Gender.FEMALE) {
                crClVal *= 0.85
            }
            crCl = crClVal
            val kePredVal = 0.00083 * crClVal + 0.0044
            kePred = kePredVal

            if (result.ke > 0.0 && kePredVal > 0.0) {
                val ratio = result.ke / kePredVal
                keRatio = ratio

                val level = when {
                    ratio in 0.7..1.4 -> PlausibilityLevel.GOOD
                    ratio in 0.4..2.5 -> PlausibilityLevel.REVIEW
                    else -> PlausibilityLevel.CONCERNING
                }

                val explanation = when (level) {
                    PlausibilityLevel.GOOD ->
                        "Calculated Ke (%.4f/hr) is consistent with expected renal function (CrCl ≈ %.1f mL/min predicts Ke ≈ %.4f/hr)."
                            .format(result.ke, crClVal, kePredVal)
                    PlausibilityLevel.REVIEW ->
                        if (ratio < 0.7) {
                            "Calculated Ke (%.4f/hr) is moderately slower than predicted for CrCl ≈ %.1f mL/min (predicted Ke ≈ %.4f/hr). Verify entered levels or confirm documented renal impairment."
                                .format(result.ke, crClVal, kePredVal)
                        } else {
                            "Calculated Ke (%.4f/hr) is moderately faster than predicted for CrCl ≈ %.1f mL/min (predicted Ke ≈ %.4f/hr). Verify entered concentrations and timing."
                                .format(result.ke, crClVal, kePredVal)
                        }
                    PlausibilityLevel.CONCERNING ->
                        if (ratio < 0.4) {
                            "Calculated Ke (%.4f/hr) is about %.1fx slower than expected for this patient's estimated renal function (CrCl ≈ %.1f mL/min predicts Ke ≈ %.4f/hr). Please verify entered dose, concentrations, and sampling times, or confirm documented renal impairment."
                                .format(result.ke, 1.0 / ratio, crClVal, kePredVal)
                        } else {
                            "Calculated Ke (%.4f/hr) is about %.1fx faster than expected for this patient's estimated renal function (CrCl ≈ %.1f mL/min predicts Ke ≈ %.4f/hr). Please verify entered dose, concentrations, and sampling times."
                                .format(result.ke, ratio, crClVal, kePredVal)
                        }
                }

                items += PlausibilityItem(
                    parameterName = "Renal Consistency (Ke vs Predicted)",
                    level = level,
                    valueFormatted = "%.4f /hr (Ratio: %.2f)".format(result.ke, ratio),
                    explanation = explanation
                )
            }
        }

        if (!isRenalApplicable) {
            items += PlausibilityItem(
                parameterName = "Renal Consistency (Ke vs Predicted)",
                level = PlausibilityLevel.GOOD,
                valueFormatted = "Not applicable",
                explanation = notApplicableReason ?: "Renal cross-check not applicable"
            )
        }

        // 2. Absolute Ke check
        val keLevel = when {
            result.ke in 0.05..0.15 -> PlausibilityLevel.GOOD
            result.ke in 0.003..0.25 -> PlausibilityLevel.REVIEW
            else -> PlausibilityLevel.CONCERNING
        }
        val keExp = when (keLevel) {
            PlausibilityLevel.GOOD -> "Calculated Ke (%.4f/hr) is within typical population bounds (0.05–0.15 /hr).".format(result.ke)
            PlausibilityLevel.REVIEW -> "Calculated Ke (%.4f/hr) is outside typical range (0.05–0.15 /hr) but within possible clinical bounds (0.003–0.25 /hr).".format(result.ke)
            PlausibilityLevel.CONCERNING -> "Calculated Ke (%.4f/hr) is outside physiologically plausible bounds (0.003–0.25 /hr). Check input data for errors.".format(result.ke)
        }
        items += PlausibilityItem("Elimination Rate (Ke)", keLevel, "%.4f /hr".format(result.ke), keExp)

        // 3. Absolute Half-life check
        val hlLevel = when {
            result.halfLifeHr in 4.0..8.0 -> PlausibilityLevel.GOOD
            result.halfLifeHr in 1.0..230.0 -> PlausibilityLevel.REVIEW
            else -> PlausibilityLevel.CONCERNING
        }
        val hlExp = when (hlLevel) {
            PlausibilityLevel.GOOD -> "Calculated half-life (%.2f hr) is within typical population bounds (4–8 h).".format(result.halfLifeHr)
            PlausibilityLevel.REVIEW -> "Calculated half-life (%.2f hr) is outside typical range (4–8 h) but within possible clinical bounds (1–230 h).".format(result.halfLifeHr)
            PlausibilityLevel.CONCERNING -> "Calculated half-life (%.2f hr) is outside plausible clinical limits (1–230 h).".format(result.halfLifeHr)
        }
        items += PlausibilityItem("Half-life (t½)", hlLevel, "%.2f hr".format(result.halfLifeHr), hlExp)

        // 4. Absolute Volume of Distribution (Vd in L/kg)
        val weight = patientInfo?.weightKg
        if (weight != null && weight > 0.0) {
            val vdPerKg = result.vd / weight
            val vdLevel = when {
                vdPerKg in 0.6..0.7 -> PlausibilityLevel.GOOD
                vdPerKg in 0.4..1.0 -> PlausibilityLevel.REVIEW
                else -> PlausibilityLevel.CONCERNING
            }
            val vdExp = when (vdLevel) {
                PlausibilityLevel.GOOD -> "Calculated Vd (%.2f L/kg, total %.2f L) is within typical bounds (0.6–0.7 L/kg).".format(vdPerKg, result.vd)
                PlausibilityLevel.REVIEW -> "Calculated Vd (%.2f L/kg, total %.2f L) is outside typical range (0.6–0.7 L/kg) but within possible bounds (0.4–1.0 L/kg).".format(vdPerKg, result.vd)
                PlausibilityLevel.CONCERNING -> "Calculated Vd (%.2f L/kg, total %.2f L) is outside physiologically expected bounds (0.4–1.0 L/kg).".format(vdPerKg, result.vd)
            }
            items += PlausibilityItem("Volume of Distribution (Vd)", vdLevel, "%.2f L (%.2f L/kg)".format(result.vd, vdPerKg), vdExp)
        } else {
            val vdLevel = when {
                result.vd in 30.0..60.0 -> PlausibilityLevel.GOOD
                result.vd in 20.0..100.0 -> PlausibilityLevel.REVIEW
                else -> PlausibilityLevel.CONCERNING
            }
            items += PlausibilityItem("Volume of Distribution (Vd)", vdLevel, "%.2f L".format(result.vd), "Calculated Vd total is %.2f L.".format(result.vd))
        }

        // 5. Absolute AUC24 check
        result.auc24?.let { auc24 ->
            val aucLevel = when {
                auc24 in 400.0..600.0 -> PlausibilityLevel.GOOD
                auc24 in 300.0..700.0 -> PlausibilityLevel.REVIEW
                else -> PlausibilityLevel.CONCERNING
            }
            val aucExp = when (aucLevel) {
                PlausibilityLevel.GOOD -> "24-hour AUC (%.1f mg·h/L) is within the target therapeutic window (400–600 mg·h/L).".format(auc24)
                PlausibilityLevel.REVIEW -> "24-hour AUC (%.1f mg·h/L) is outside the target window (400–600 mg·h/L) but within acceptable margins (300–700 mg·h/L).".format(auc24)
                PlausibilityLevel.CONCERNING -> "24-hour AUC (%.1f mg·h/L) is significantly outside the target therapeutic range (<300 or >700 mg·h/L).".format(auc24)
            }
            items += PlausibilityItem("AUC (24h)", aucLevel, "%.1f mg·h/L".format(auc24), aucExp)
        }

        // Determine overall level (worst level among items)
        val overall = when {
            items.any { it.level == PlausibilityLevel.CONCERNING } -> PlausibilityLevel.CONCERNING
            items.any { it.level == PlausibilityLevel.REVIEW } -> PlausibilityLevel.REVIEW
            else -> PlausibilityLevel.GOOD
        }

        return PlausibilityReport(
            overallLevel = overall,
            items = items,
            crClMlMin = crCl,
            kePredicted = kePred,
            keRatio = keRatio,
            isRenalCheckApplicable = isRenalApplicable,
            renalCheckNotApplicableReason = notApplicableReason
        )
    }
}
