package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.ln

class PrePostCalculator {

    fun calculate(
        input: TDMInput.PrePost
    ): TDMResult {

        val steps =
            mutableListOf<CalculationStep>()

        val peak = input.postLevelConc
        val trough = input.preLevelConc
        val deltaT = input.preToPostGapHr
        val dose = input.doseMg
        val interval = input.intervalHr
        val mic = input.micMgL

        steps += CalculationStep(
            "Dose",
            "$dose mg",
            "Entered dose"
        )

        steps += CalculationStep(
            "Pre-dose Level",
            "$trough mg/L",
            "Entered pre-dose level"
        )

        steps += CalculationStep(
            "Post-dose Level",
            "$peak mg/L",
            "Entered post-dose level"
        )

        steps += CalculationStep(
            "Pre-to-Post Gap",
            "$deltaT hr",
            "Entered time between the two levels"
        )

        steps += CalculationStep(
            "MIC",
            "$mic mg/L",
            "Entered minimum inhibitory concentration"
        )

        val ke =
            if (
                peak > 0 &&
                trough > 0 &&
                deltaT > 0
            ) {
                (
                        ln(peak) -
                                ln(trough)
                        ) / deltaT
            } else {
                0.0
            }

        steps += CalculationStep(
            "Elimination Rate (Ke)",
            "%.4f".format(ke),
            "(ln(Peak) − ln(Trough)) / Gap"
        )

        val halfLife =
            if (ke > 0) {
                ln(2.0) / ke
            } else {
                0.0
            }

        steps += CalculationStep(
            "Half-life",
            "%.2f hr".format(halfLife),
            "0.693 / Ke"
        )

        val vd =
            if (
                ke > 0 &&
                peak > 0
            ) {
                dose / peak
            } else {
                0.0
            }

        steps += CalculationStep(
            "Volume of Distribution (Vd)",
            "%.2f L".format(vd),
            "Dose / Peak"
        )

        val clearance =
            ke * vd

        steps += CalculationStep(
            "Clearance",
            "%.2f L/hr".format(clearance),
            "Ke × Vd"
        )

        val auc24 =
            if (
                clearance > 0 &&
                interval > 0
            ) {
                (dose / clearance) *
                        (24.0 / interval)
            } else {
                0.0
            }

        steps += CalculationStep(
            "AUC24",
            "%.2f mg·h/L".format(auc24),
            "(Dose / Clearance) × (24 / Dosing Interval)"
        )

        val aucMic =
            if (mic > 0) {
                auc24 / mic
            } else {
                0.0
            }

        steps += CalculationStep(
            "AUC/MIC",
            "%.2f".format(aucMic),
            "AUC24 / MIC"
        )

        return TDMResult(
            ke = ke,
            halfLifeHr = halfLife,
            vd = vd,
            clearance = clearance,
            auc24 = auc24,
            micMgL = mic,
            aucMic = aucMic,
            expectedCmin = trough,
            expectedCmax = peak,
            steps = steps
        )
    }
}