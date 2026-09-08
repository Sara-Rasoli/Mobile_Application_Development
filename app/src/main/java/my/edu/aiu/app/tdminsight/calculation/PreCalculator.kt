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
                value = "${String.format("%.2f", crCl)} mL/min"
            )
        )

        // 2. Population Ke estimation
        val ke = 0.00083 * crCl + 0.0044
        steps.add(
            CalculationStep(
                title = "Elimination Rate Constant (Ke)",
                value = "${String.format("%.4f", ke)} h⁻¹"
            )
        )

        // 3. Half-life
        val halfLife = ln(2.0) / ke
        steps.add(
            CalculationStep(
                title = "Elimination Half-Life (t1/2)",
                value = "${String.format("%.2f", halfLife)} hours"
            )
        )

        // 4. Volume of Distribution
        val vd = 0.7 * info.weightKg
        steps.add(
            CalculationStep(
                title = "Volume of Distribution (Vd)",
                value = "${String.format("%.2f", vd)} L"
            )
        )

        // 5. Clearance
        val clearance = ke * vd
        steps.add(
            CalculationStep(
                title = "Clearance (CL)",
                value = "${String.format("%.2f", clearance)} L/h"
            )
        )

        // 6. AUC24
        val dailyDose = input.doseMg * (24.0 / input.dosingIntervalHours)
        val auc24 = dailyDose / clearance
        steps.add(
            CalculationStep(
                title = "24-Hour Area Under Curve (AUC24)",
                value = "${String.format("%.2f", auc24)} mg·h/L"
            )
        )

        // 7. Peak & Trough Concentrations
        val tInf = input.infusionDurationHours
        val tau = input.dosingIntervalHours
        val peak = (input.doseMg / vd) * ((1.0 - exp(-ke * tInf)) / (1.0 - exp(-ke * tau)))
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