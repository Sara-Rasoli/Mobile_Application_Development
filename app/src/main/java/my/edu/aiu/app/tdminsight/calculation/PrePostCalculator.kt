package my.edu.aiu.app.tdminsight.calculation

import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

class PrePostCalculator {

    fun calculate(
        input: TDMInput.PrePost
    ): TDMResult {

        val steps = mutableListOf<CalculationStep>()

        val dose = input.doseMg
        val tau = input.intervalHr
        val tinf = input.infusionDurationHr

        val hoursAfterInfusionEnd = input.infusionToPostGapHr
        val deltaT = input.preToPostGapHr

        val cPre = input.preLevelConc
        val cPost = input.postLevelConc

        val mic = input.micMgL

        // =========================================================
        // INPUT VALUES
        // =========================================================

        steps += CalculationStep(
            "Dose",
            "$dose mg",
            "Entered dose"
        )

        steps += CalculationStep(
            "Dosing Interval (tau)",
            "$tau hr",
            "Entered interval"
        )

        steps += CalculationStep(
            "Infusion Duration (tinf)",
            "$tinf hr",
            "Entered infusion duration"
        )

        steps += CalculationStep(
            "Pre-dose Level (Cpre)",
            "$cPre mg/L",
            "Entered trough level"
        )

        steps += CalculationStep(
            "Post-dose Level (Cpost)",
            "$cPost mg/L",
            "Entered peak level"
        )

        steps += CalculationStep(
            "Pre-to-Post Gap (deltaT)",
            "$deltaT hr",
            "Time gap between Cpost and Cpre"
        )

        steps += CalculationStep(
            "MIC",
            "$mic mg/L",
            "Entered minimum inhibitory concentration"
        )

        // =========================================================
        // INPUT VALIDATION
        // =========================================================

        if (dose <= 0.0) {
            throw IllegalArgumentException(
                "Dose must be greater than 0 mg."
            )
        }

        if (tau <= 0.0) {
            throw IllegalArgumentException(
                "Dosing interval must be greater than 0 hours."
            )
        }

        if (tinf <= 0.0) {
            throw IllegalArgumentException(
                "Infusion duration must be greater than 0 hours."
            )
        }

        if (tinf >= tau) {
            throw IllegalArgumentException(
                "Infusion duration must be shorter than the dosing interval."
            )
        }

        if (cPre <= 0.0 || cPost <= 0.0) {
            throw IllegalArgumentException(
                "Both pre-dose and post-dose concentrations must be greater than 0."
            )
        }

        if (cPost <= cPre) {
            throw IllegalArgumentException(
                "Post-dose level ($cPost mg/L) must be greater than pre-dose level ($cPre mg/L)."
            )
        }

        if (deltaT <= 0.0) {
            throw IllegalArgumentException(
                "Time gap between pre and post samples ($deltaT hr) must be greater than 0."
            )
        }

        if (hoursAfterInfusionEnd < 0.0) {
            throw IllegalArgumentException(
                "Hours after infusion end cannot be negative."
            )
        }

        // =========================================================
        // SAMPLE TIMES
        // =========================================================

        val tPost = tinf + hoursAfterInfusionEnd

        val tPre = tPost + deltaT

        val hoursBeforeNextDose =
            max(
                0.0,
                tau - tPre
            )

        if (tPre <= tPost) {
            throw IllegalArgumentException(
                "Pre-sample time must be greater than post-sample time."
            )
        }

        if (tPre > tau) {
            throw IllegalArgumentException(
                "Pre-sample time exceeds the dosing interval. Please check the sample timing."
            )
        }

        steps += CalculationStep(
            "Post Sample Time",
            "%.2f hr".format(tPost),
            "tinf ($tinf hr) + infusion-to-post gap ($hoursAfterInfusionEnd hr)"
        )

        steps += CalculationStep(
            "Pre Sample Time",
            "%.2f hr".format(tPre),
            "Post sample time + pre-to-post gap"
        )

        // =========================================================
        // ELIMINATION RATE
        // =========================================================

        val ke =
            ln(cPost / cPre) / deltaT

        steps += CalculationStep(
            "Elimination Rate (Ke)",
            "%.4f /hr".format(ke),
            "ln(Cpost / Cpre) / (tPre − tPost)"
        )

        // =========================================================
        // EXTRAPOLATED PEAK
        // =========================================================

        val cMax =
            cPost * exp(
                ke * hoursAfterInfusionEnd
            )

        steps += CalculationStep(
            "Extrapolated Peak (Cmax)",
            "%.2f mg/L".format(cMax),
            "Cpost × e^(Ke × hoursAfterInfusionEnd)"
        )

        // =========================================================
        // EXTRAPOLATED TROUGH
        // =========================================================

        val cMin =
            cPre * exp(
                -ke * hoursBeforeNextDose
            )

        steps += CalculationStep(
            "Extrapolated Trough (Cmin)",
            "%.2f mg/L".format(cMin),
            "Cpre × e^(−Ke × hoursBeforeNextDose)"
        )

        if (cMax <= cMin) {
            throw IllegalArgumentException(
                "Extrapolated peak ($cMax mg/L) must be greater than extrapolated trough ($cMin mg/L)."
            )
        }

        // =========================================================
        // VOLUME OF DISTRIBUTION
        // =========================================================

        val k0 =
            dose / tinf

        val vdNumerator =
            k0 * (
                1.0 - exp(-ke * tinf)
            )

        val vdDenominator =
            ke * (
                cMax -
                        cMin * exp(-ke * tinf)
            )

        if (vdDenominator <= 0.0) {
            throw IllegalArgumentException(
                "Calculated volume of distribution is non-positive. Please check the concentration and sampling values."
            )
        }

        val vd =
            vdNumerator / vdDenominator

        if (vd <= 0.0) {
            throw IllegalArgumentException(
                "Calculated volume of distribution is non-positive."
            )
        }

        steps += CalculationStep(
            "Volume of Distribution (Vd)",
            "%.2f L".format(vd),
            "[k0 × (1 − e^(−Ke·tinf))] / [Ke × (Cmax − Cmin × e^(−Ke·tinf))]"
        )

        // =========================================================
        // HALF-LIFE
        // =========================================================

        val halfLife =
            PkMath.calculateHalfLife(ke)

        steps += CalculationStep(
            "Half-life (t½)",
            "%.2f hr".format(halfLife),
            "ln(2) / Ke"
        )

        // =========================================================
        // CLEARANCE
        // =========================================================

        val clearance =
            PkMath.calculateClearance(
                ke,
                vd
            )

        steps += CalculationStep(
            "Clearance (CL)",
            "%.2f L/hr".format(clearance),
            "Ke × Vd"
        )

        // =========================================================
        // STEADY-STATE PEAK
        // =========================================================

        val cmaxSs =
            PkMath.calculateCmaxSs(
                dose,
                tinf,
                tau,
                ke,
                vd
            )

        steps += CalculationStep(
            "Steady-state Peak (Cmax,ss)",
            "%.2f mg/L".format(cmaxSs),
            "[k0 / (Ke·Vd)] × (1 − e^(−Ke·tinf)) / (1 − e^(−Ke·tau))"
        )

        // =========================================================
        // STEADY-STATE TROUGH
        // =========================================================

        val cminSs =
            PkMath.calculateCminSs(
                cmaxSs,
                ke,
                tau,
                tinf
            )

        steps += CalculationStep(
            "Steady-state Trough (Cmin,ss)",
            "%.2f mg/L".format(cminSs),
            "Cmax,ss × e^(−Ke·(tau − tinf))"
        )

        // =========================================================
        // AUC INTERVAL
        // =========================================================

        val aucTau =
            PkMath.calculateAucTau(
                dose,
                clearance
            )

        steps += CalculationStep(
            "AUC (interval)",
            "%.2f mg·h/L".format(aucTau),
            "Dose / CL"
        )

        // =========================================================
        // AUC24
        // =========================================================

        val auc24 =
            PkMath.calculateAuc24(
                aucTau,
                tau
            )

        steps += CalculationStep(
            "AUC (24h)",
            "%.2f mg·h/L".format(auc24),
            "AUCtau × 24 / tau"
        )

        // =========================================================
        // AUC / MIC
        // =========================================================

        val aucMic =
            if (mic > 0.0) {
                auc24 / mic
            } else {
                0.0
            }

        steps += CalculationStep(
            "AUC/MIC",
            "%.2f".format(aucMic),
            "AUC24 / MIC"
        )

        // =========================================================
        // FINAL RESULT
        // =========================================================

        return TDMResult(
            ke = ke,
            halfLifeHr = halfLife,
            vd = vd,
            clearance = clearance,
            aucTau = aucTau,
            auc24 = auc24,
            micMgL = mic,
            aucMic = aucMic,
            expectedCmin = cminSs,
            expectedCmax = cmaxSs,
            steps = steps
        )
    }
}