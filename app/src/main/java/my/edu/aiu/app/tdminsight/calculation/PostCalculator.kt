package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.*
import kotlin.math.ln

class PostCalculator {
    fun calculate(input: TDMInput): TDMResult {
        val steps = mutableListOf<CalculationStep>()
        val info = input.patientInfo

        val peak = input.postConcentrationMgL ?: 0.0
        val trough = input.preConcentrationMgL ?: 0.0
        val deltaT = input.dosingIntervalHours - input.infusionDurationHours

        val ke = if (peak > 0 && trough > 0 && deltaT > 0) {
            (ln(peak) - ln(trough)) / deltaT
        } else 0.0

        steps.add(
            CalculationStep(
                title = "Patient-Specific Ke (From Measured Levels)",
                formula = "Ke = [ln(Peak) - ln(Trough)] / Δt",
                substitution = "Ke = [ln($peak) - ln($trough)] / $deltaT",
                result = "${String.format("%.4f", ke)} h⁻¹"
            )
        )

        val halfLife = if (ke > 0) ln(2.0) / ke else 0.0
        val vd = 0.7 * info.weightKg
        val clearance = ke * vd
        val dailyDose = input.doseMg * (24.0 / input.dosingIntervalHours)
        val auc24 = if (clearance > 0) dailyDose / clearance else 0.0

        return TDMResult(
            workflow = TDMWorkflow.POST,
            patientInfo = info,
            ke = ke,
            halfLifeHours = halfLife,
            vdLiters = vd,
            clearanceLitersPerHour = clearance,
            auc24MgHourPerL = auc24,
            estimatedPeakMgL = peak,
            estimatedTroughMgL = trough,
            calculationSteps = steps
        )
    }
}