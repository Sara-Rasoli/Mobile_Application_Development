package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.ln

class PostCalculator {
    fun calculate(input: TDMInput.Post): TDMResult {
        val steps = mutableListOf<CalculationStep>()
        val peak = input.postLevelConc
        val dose = input.doseMg
        val tInf = input.infusionDurationHr
        val tSample = input.samplingTimeHr

        steps += CalculationStep("Dose", "$dose mg", "Entered dose")
        steps += CalculationStep("Post-dose Level", "$peak mg/L", "Entered post-level")
        steps += CalculationStep("Sampling Time", "$tSample hr after infusion", "Entered sampling time")

        val ke = if (tSample > 0 && peak > 0) ln(dose / (peak * tInf)) / tSample else 0.0
        steps += CalculationStep("Elimination Rate (Ke)", "%.4f".format(ke), "ln(Dose / (Peak × Infusion Duration)) / Sampling Time")

        val halfLife = if (ke > 0) ln(2.0) / ke else 0.0
        steps += CalculationStep("Half-life", "%.2f hr".format(halfLife), "0.693 / Ke")

        val vd = if (ke > 0 && peak > 0) dose / peak else 0.0
        steps += CalculationStep("Volume of Distribution (Vd)", "%.2f L".format(vd), "Dose / Peak")

        val clearance = ke * vd
        steps += CalculationStep("Clearance", "%.2f L/hr".format(clearance), "Ke × Vd")

        return TDMResult(
            ke = ke, halfLifeHr = halfLife, vd = vd, clearance = clearance,
            expectedCmax = peak, steps = steps
        )
    }
}