package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.*

class PrePostCalculator {
    private val postCalculator = PostCalculator()

    fun calculate(input: TDMInput): TDMResult {
        val postResult = postCalculator.calculate(input)
        val steps = postResult.calculationSteps.toMutableList()

        steps.add(
            CalculationStep(
                title = "Dosage Re-evaluation (Pre + Post Synthesis)",
                formula = "New Dose = Current Dose * (Target AUC / Measured AUC)",
                substitution = "New Dose = ${input.doseMg} * (500.0 / ${String.format("%.2f", postResult.auc24MgHourPerL)})",
                result = "${String.format("%.0f", input.doseMg * (500.0 / (if (postResult.auc24MgHourPerL > 0) postResult.auc24MgHourPerL else 1.0)))} mg",
                clinicalNote = "Target AUC set to median 500 mg·h/L"
            )
        )

        return postResult.copy(
            workflow = TDMWorkflow.PRE_POST,
            calculationSteps = steps
        )
    }
}