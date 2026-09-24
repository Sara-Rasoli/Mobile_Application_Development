package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.exp
import kotlin.math.ln

class PreCalculator {

    fun calculate(
        input: TDMInput.Pre
    ): TDMResult {

        val steps =
            mutableListOf<CalculationStep>()

        val dose = input.doseMg
        val interval = input.intervalHr
        val tInf = input.infusionDurationHr
        val trough = input.preLevelConc
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
            "Dosing Interval",
            "$interval hr",
            "Entered interval"
        )

        steps += CalculationStep(
            "Pre-dose (trough) Level",
            "$trough mg/L",
            "Entered trough level"
        )

        steps += CalculationStep(
            "MIC",
            "$mic mg/L",
            "Entered minimum inhibitory concentration"
        )

        // =========================================================
        // ELIMINATION RATE
        // =========================================================

        val ke =
            if (
                interval > 0 &&
                trough > 0 &&
                tInf > 0
            ) {

                ln(
                    dose /
                            (trough * tInf)
                ) / interval

            } else {
                0.0
            }

        steps += CalculationStep(
            "Elimination Rate (Ke)",
            "%.4f".format(ke),
            "ln(Dose / (Trough × Infusion Duration)) / Interval"
        )

        // =========================================================
        // HALF-LIFE
        // =========================================================

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

        // =========================================================
        // VOLUME OF DISTRIBUTION
        // =========================================================

        val vd =
            if (
                ke > 0 &&
                trough > 0
            ) {

                dose / trough

            } else {
                0.0
            }

        steps += CalculationStep(
            "Volume of Distribution (Vd)",
            "%.2f L".format(vd),
            "Dose / Trough"
        )

        // =========================================================
        // CLEARANCE
        // =========================================================

        val clearance =
            ke * vd

        steps += CalculationStep(
            "Clearance",
            "%.2f L/hr".format(clearance),
            "Ke × Vd"
        )

        // =========================================================
        // AUC24
        // =========================================================

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

        // =========================================================
        // AUC / MIC
        // =========================================================

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

        // =========================================================
        // EXPECTED PEAK / CMAX
        // =========================================================
        //
        // The pre-dose concentration is the trough.
        // The estimated peak is calculated using:
        //
        // Cmax = Cmin × e^(Ke × interval)
        //
        // =========================================================

        val expectedCmax =
            if (
                ke > 0.0 &&
                interval > 0.0 &&
                trough > 0.0
            ) {

                trough *
                        exp(
                            ke * interval
                        )

            } else {
                null
            }

        expectedCmax?.let { cmax ->

            steps += CalculationStep(
                "Expected Peak (Cmax)",
                "%.2f mg/L".format(cmax),
                "Cmin × e^(Ke × Dosing Interval)"
            )
        }

        // =========================================================
        // FINAL RESULT
        // =========================================================

        return TDMResult(
            ke = ke,
            halfLifeHr = halfLife,
            vd = vd,
            clearance = clearance,

            auc24 = auc24,
            micMgL = mic,
            aucMic = aucMic,

            expectedCmin = trough,
            expectedCmax = expectedCmax,

            steps = steps
        )
    }
}