package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.ln

class PreCalculator {
    fun calculate(input: TDMInput.Pre): TDMResult {
        val steps = mutableListOf<CalculationStep>()
        val dose = input.doseMg
        val interval = input.intervalHr
        val tInf = input.infusionDurationHr
        val trough = input.preLevelConc

        steps += CalculationStep("Dose", "$dose mg", "Entered dose")
        steps += CalculationStep("Dosing Interval", "$interval hr", "Entered interval")
        steps += CalculationStep("Pre-dose (trough) Level", "$trough mg/L", "Entered trough level")

        val ke = if (interval > 0 && trough > 0) ln(dose / (trough * tInf)) / interval else 0.0
        steps += CalculationStep("Elimination Rate (Ke)", "%.4f".format(ke), "ln(Dose / (Trough × Infusion Duration)) / Interval")

        val halfLife = if (ke > 0) ln(2.0) / ke else 0.0
        steps += CalculationStep("Half-life", "%.2f hr".format(halfLife), "0.693 / Ke")

        val vd = if (ke > 0 && trough > 0) dose / trough else 0.0
        steps += CalculationStep("Volume of Distribution (Vd)", "%.2f L".format(vd), "Dose / Trough")

        val clearance = ke * vd
        steps += CalculationStep("Clearance", "%.2f L/hr".format(clearance), "Ke × Vd")

        return TDMResult(
            ke = ke, halfLifeHr = halfLife, vd = vd, clearance = clearance,
            expectedCmin = trough, steps = steps
        )
    }
}