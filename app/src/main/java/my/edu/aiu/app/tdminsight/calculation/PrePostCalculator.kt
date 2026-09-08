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

        val ke = if (peak > 0 && trough > 0 && deltaT > 0) {
            (ln(peak) - ln(trough)) / deltaT
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
            expectedCmin = trough
        )
    }
}