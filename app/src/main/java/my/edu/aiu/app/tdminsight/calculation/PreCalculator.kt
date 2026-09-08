package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.*
import kotlin.math.exp
import kotlin.math.ln

class PreCalculator {
    fun calculate(input: TDMInput): TDMResult {
        val steps = mutableListOf<CalculationStep>()
        val info = input.patientInfo

        // 1. Creatinine Clearance via Cockcroft-Gault
        val rawCrCl = ((140.0 - info.age) * info.weightKg) / (72.0 * info.serumCreatinineMgDl)
        val crCl = if (info.isFemale) rawCrCl * 0.85 else rawCrCl

        steps.add(
            CalculationStep(
                title = "Creatinine Clearance (Cockcroft-Gault)",
                formula = "CrCl = [(140 - Age) * Weight] / [72 * SCr] (* 0.85 if female)",
                substitution = "CrCl = [(140 - ${info.age}) * ${info.weightKg}] / [72 * ${info.serumCreatinineMgDl}]",
                result = "${String.format("%.2f", crCl)} mL/min"
            )
        )

        // 2. Population Ke estimation: Ke = 0.00083 * CrCl + 0.0044
        val ke = 0.00083 * crCl + 0.0044
        steps.add(
            CalculationStep(
                title = "Elimination Rate Constant (Ke)",
                formula = "Ke = 0.00083 * CrCl + 0.0044",
                substitution = "Ke = 0.00083 * ${String.format("%.2f", crCl)} + 0.0044",
                result = "${String.format("%.4f", ke)} h⁻¹"
            )
        )

        // 3. Half-life: t1/2 = ln(2) / Ke
        val halfLife = ln(2.0) / ke
        steps.add(
            CalculationStep(
                title = "Elimination Half-Life (t1/2)",
                formula = "t1/2 = 0.693 / Ke",
                substitution = "t1/2 = 0.693 / ${String.format("%.4f", ke)}",
                result = "${String.format("%.2f", halfLife)} hours"
            )
        )

        // 4. Volume of Distribution (Vd = 0.7 L/kg)
        val vd = 0.7 * info.weightKg
        steps.add(
            CalculationStep(
                title = "Volume of Distribution (Vd)",
                formula = "Vd = 0.7 L/kg * Weight",
                substitution = "Vd = 0.7 * ${info.weightKg}",
                result = "${String.format("%.2f", vd)} L"
            )
        )

        // 5. Clearance: CL = Ke * Vd
        val clearance = ke * vd
        steps.add(
            CalculationStep(
                title = "Clearance (CL)",
                formula = "CL = Ke * Vd",
                substitution = "CL = ${String.format("%.4f", ke)} * ${String.format("%.2f", vd)}",
                result = "${String.format("%.2f", clearance)} L/h"
            )
        )

        // 6. AUC24 = Daily Dose / CL
        val dailyDose = input.doseMg * (24.0 / input.dosingIntervalHours)
        val auc24 = dailyDose / clearance
        steps.add(
            CalculationStep(
                title = "24-Hour Area Under Curve (AUC24)",
                formula = "AUC24 = Daily Dose / Clearance",
                substitution = "AUC24 = $dailyDose / ${String.format("%.2f", clearance)}",
                result = "${String.format("%.2f", auc24)} mg·h/L",
                clinicalNote = "Target therapeutic window: 400–600 mg·h/L"
            )
        )

        // 7. Peak Concentration
        val tInf = input.infusionDurationHours
        val tau = input.dosingIntervalHours
        val peak = (input.doseMg / vd) * ((1.0 - exp(-ke * tInf)) / (1.0 - exp(-ke * tau)))

        // 8. Trough Concentration
        val trough = peak * exp(-ke * (tau - tInf))

        return TDMResult(
            workflow = TDMWorkflow.PRE,
            patientInfo = info,
            estimatedCrClMlMin = crCl,
            ke = ke,
            halfLifeHours = halfLife,
            vdLiters = vd,
            clearanceLitersPerHour = clearance,
            auc24MgHourPerL = auc24,
            estimatedPeakMgL = peak,
            estimatedTroughMgL = trough,
            calculationSteps = steps
        )
    }
}