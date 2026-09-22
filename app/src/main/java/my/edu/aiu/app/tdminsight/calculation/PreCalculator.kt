package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.CalculationStep
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

class PreCalculator {

    fun calculate(
        input: TDMInput.Pre,
        weightKg: Double = 70.0,
        hoursBeforeNextDose: Double = 0.0
    ): TDMResult {
        val steps = mutableListOf<CalculationStep>()
        val dose = input.doseMg
        val tau = input.intervalHr
        val tinf = input.infusionDurationHr
        val trough = input.preLevelConc

        steps += CalculationStep("Dose", "$dose mg", "Entered dose")
        steps += CalculationStep("Dosing Interval (tau)", "$tau hr", "Entered interval")
        steps += CalculationStep("Infusion Duration (tinf)", "$tinf hr", "Entered duration")
        steps += CalculationStep("Pre-dose (trough) Level", "$trough mg/L", "Entered trough level")

        val vd = 0.7 * weightKg
        steps += CalculationStep(
            "Volume of Distribution (Vd)",
            "%.2f L".format(vd),
            "0.7 × Weight ($weightKg kg)"
        )

        val t = tau - hoursBeforeNextDose
        steps += CalculationStep(
            "Sample Time (t)",
            "%.2f hr".format(t),
            "tau ($tau hr) − hoursBeforeNextDose ($hoursBeforeNextDose hr)"
        )

        val ke = PkMath.solveKeForConcentration(
            doseMg = dose,
            tinf = tinf,
            tau = tau,
            vd = vd,
            t = t,
            measuredConc = trough
        )
        steps += CalculationStep(
            "Elimination Rate (Ke)",
            "%.4f /hr".format(ke),
            "Ke solved numerically from steady-state model"
        )

        val halfLife = PkMath.calculateHalfLife(ke)
        steps += CalculationStep(
            "Half-life (t½)",
            "%.2f hr".format(halfLife),
            "ln(2) / Ke"
        )

        val clearance = PkMath.calculateClearance(ke, vd)
        steps += CalculationStep(
            "Clearance (CL)",
            "%.2f L/hr".format(clearance),
            "Ke × Vd"
        )

        val cmaxSs = PkMath.calculateCmaxSs(dose, tinf, tau, ke, vd)
        steps += CalculationStep(
            "Steady-state Peak (Cmax,ss)",
            "%.2f mg/L".format(cmaxSs),
            "[k0 / (Ke·Vd)] × (1 − e^(−Ke·tinf)) / (1 − e^(−Ke·tau))"
        )

        val cminSs = PkMath.calculateCminSs(cmaxSs, ke, tau, tinf)
        steps += CalculationStep(
            "Steady-state Trough (Cmin,ss)",
            "%.2f mg/L".format(cminSs),
            "Cmax,ss × e^(−Ke·(tau − tinf))"
        )

        val aucTau = PkMath.calculateAucTau(dose, clearance)
        steps += CalculationStep(
            "AUC (interval)",
            "%.2f mg·h/L".format(aucTau),
            "Dose / CL"
        )

        val auc24 = PkMath.calculateAuc24(aucTau, tau)
        steps += CalculationStep(
            "AUC (24h)",
            "%.2f mg·h/L".format(auc24),
            "AUCtau × 24 / tau (Target: 400–600 mg·h/L for MIC = 1 mg/L)"
        )

        return TDMResult(
            ke = ke,
            halfLifeHr = halfLife,
            vd = vd,
            clearance = clearance,
            aucTau = aucTau,
            auc24 = auc24,
            expectedCmin = cminSs,
            expectedCmax = cmaxSs,
            steps = steps
        )
    }
}
