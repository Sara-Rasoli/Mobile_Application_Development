package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.ln

class PrePostCalculator {
    fun calculate(input: TDMInput.PrePost): TDMResult {
        val steps = mutableListOf<CalculationStep>()
        val peak = input.postLevelConc
        val trough = input.preLevelConc
        val deltaT = input.preToPostGapHr
        val dose = input.doseMg

        steps += CalculationStep("Dose", "$dose mg", "Entered dose")
        steps += CalculationStep("Pre-dose Level", "$trough mg/L", "Entered trough level")
        steps += CalculationStep("Post-dose Level", "$peak mg/L", "Entered peak level")
        steps += CalculationStep("Pre-to-Post Gap", "$deltaT hr", "Entered time between the two levels")

        val ke = if (peak > 0 && trough > 0 && deltaT > 0) (ln(peak) - ln(trough)) / deltaT else 0.0
        steps += CalculationStep("Elimination Rate (Ke)", "%.4f".format(ke), "(ln(Peak) − ln(Trough)) / Gap")

        val halfLife = if (ke > 0) ln(2.0) / ke else 0.0
        steps += CalculationStep("Half-life", "%.2f hr".format(halfLife), "0.693 / Ke")

        val vd = if (ke > 0 && peak > 0) dose / peak else 0.0
        steps += CalculationStep("Volume of Distribution (Vd)", "%.2f L".format(vd), "Dose / Peak")

        val clearance = ke * vd
        steps += CalculationStep("Clearance", "%.2f L/hr".format(clearance), "Ke × Vd")

        return TDMResult(
            ke = ke, halfLifeHr = halfLife, vd = vd, clearance = clearance,
            expectedCmin = trough, expectedCmax = peak, steps = steps
        )
    }
}