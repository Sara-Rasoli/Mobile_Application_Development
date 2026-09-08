package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.*

class PrePostCalculator {
    private val postCalculator = PostCalculator()

    fun calculate(input: TDMInput): TDMResult {
        val postResult = postCalculator.calculate(input)
        val steps = postResult.calculationSteps.toMutableList()

        val newDose = input.doseMg * (500.0 / (if (postResult.auc24MgHourPerL > 0) postResult.auc24MgHourPerL else 1.0))

        steps.add(
            CalculationStep(
                title = "Recommended Adjusted Dose (Target AUC 500)",
                value = "${String.format("%.0f", newDose)} mg"
            )
        )

        return postResult.copy(
            workflow = TDMWorkflow.PRE_POST,
            calculationSteps = steps
        )
    }
}