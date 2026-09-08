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

        // Calculate Ke and Vd based on pre-dose/trough level
        val ke = if (interval > 0 && trough > 0) {
            ln(dose / (trough * tInf)) / interval
        } else {
            0.0
        }

        val halfLife = if (ke > 0) ln(2.0) / ke else 0.0
        val vd = if (ke > 0 && trough > 0) dose / trough else 0.0
        val clearance = ke * vd

        return TDMResult(
            ke = ke,
            halfLifeHr = halfLife,
            vd = vd,
            clearance = clearance,
            auc24 = null,
            expectedCmax = null,
            expectedCmin = trough
        )
    }
}