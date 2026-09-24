package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

class PostCalculator {

    fun calculate(
        input: TDMInput.Post,
        weightKg: Double = 70.0
    ): TDMResult {

        val steps = mutableListOf<CalculationStep>()

        val dose = input.doseMg
        val tau = input.intervalHr
        val tinf = input.infusionDurationHr
        val hoursAfterInfusionEnd = input.samplingTimeHr
        val peak = input.postLevelConc
        val mic = input.micMgL

        steps += CalculationStep(
            "Dose",
            "$dose mg",
            "Entered dose"
        )

        steps += CalculationStep(
            "Dosing Interval (tau)",
            "$tau hr",
            "Entered interval"
        )

        steps += CalculationStep(
            "Infusion Duration (tinf)",
            "$tinf hr",
            "Entered infusion duration"
        )

        steps += CalculationStep(
            "Post-dose Level",
            "$peak mg/L",
            "Entered post-dose concentration"
        )

        steps += CalculationStep(
            "MIC",
            "$mic mg/L",
            "Entered minimum inhibitory concentration"
        )

        // Volume of distribution
        val vd = 0.7 * weightKg

        steps += CalculationStep(
            "Volume of Distribution (Vd)",
            "%.2f L".format(vd),
            "0.7 × Weight ($weightKg kg)"
        )

        // Total time from the start of infusion to sampling
        val t = tinf + hoursAfterInfusionEnd

        steps += CalculationStep(
            "Sample Time (t)",
            "%.2f hr".format(t),
            "tinf ($tinf hr) + hoursAfterInfusionEnd ($hoursAfterInfusionEnd hr)"
        )

        // Solve Ke using the steady-state infusion model
        val ke = PkMath.solveKeForConcentration(
            doseMg = dose,
            tinf = tinf,
            tau = tau,
            vd = vd,
            t = t,
            measuredConc = peak
        )

        steps += CalculationStep(
            "Elimination Rate (Ke)",
            "%.4f /hr".format(ke),
            "Ke solved numerically from steady-state model"
        )

        // Half-life
        val halfLife = PkMath.calculateHalfLife(ke)

        steps += CalculationStep(
            "Half-life (t½)",
            "%.2f hr".format(halfLife),
            "ln(2) / Ke"
        )

        // Clearance
        val clearance = PkMath.calculateClearance(
            ke,
            vd
        )

        steps += CalculationStep(
            "Clearance (CL)",
            "%.2f L/hr".format(clearance),
            "Ke × Vd"
        )

        // Steady-state peak
        val cmaxSs = PkMath.calculateCmaxSs(
            dose,
            tinf,
            tau,
            ke,
            vd
        )

        steps += CalculationStep(
            "Steady-state Peak (Cmax,ss)",
            "%.2f mg/L".format(cmaxSs),
            "[k0 / (Ke·Vd)] × (1 − e^(−Ke·tinf)) / (1 − e^(−Ke·tau))"
        )

        // Steady-state trough
        val cminSs = PkMath.calculateCminSs(
            cmaxSs,
            ke,
            tau,
            tinf
        )

        steps += CalculationStep(
            "Steady-state Trough (Cmin,ss)",
            "%.2f mg/L".format(cminSs),
            "Cmax,ss × e^(−Ke·(tau − tinf))"
        )

        // AUC for one dosing interval
        val aucTau = PkMath.calculateAucTau(
            dose,
            clearance
        )

        steps += CalculationStep(
            "AUC (interval)",
            "%.2f mg·h/L".format(aucTau),
            "Dose / CL"
        )

        // AUC over 24 hours
        val auc24 = PkMath.calculateAuc24(
            aucTau,
            tau
        )

        steps += CalculationStep(
            "AUC (24h)",
            "%.2f mg·h/L".format(auc24),
            "AUCtau × 24 / tau"
        )

        // AUC/MIC
        val aucMic =
            if (mic > 0.0) {
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
            aucTau = aucTau,
            auc24 = auc24,
            micMgL = mic,
            aucMic = aucMic,
            expectedCmin = cminSs,
            expectedCmax = cmaxSs,
            steps = steps
        )
    }
}