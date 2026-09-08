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

        // Estimate Ke and Vd based on post-dose level and infusion timing
        val ke = if (tSample > 0 && peak > 0) {
            ln(dose / (peak * tInf)) / tSample
        } else {
            0.0
        }

        val halfLife = if (ke > 0) ln(2.0) / ke else 0.0
        val vd = if (ke > 0 && peak > 0) dose / peak else 0.0
        val clearance = ke * vd

        return TDMResult(
            ke = ke,
            halfLifeHr = halfLife,
            vd = vd,
            clearance = clearance,
            auc24 = null,
            expectedCmax = peak,
            expectedCmin = null
        )
    }
}