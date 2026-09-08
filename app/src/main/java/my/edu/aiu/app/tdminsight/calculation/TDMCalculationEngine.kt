package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.*

class TDMCalculationEngine {
    private val preCalculator = PreCalculator()
    private val postCalculator = PostCalculator()
    private val prePostCalculator = PrePostCalculator()

    fun calculate(input: TDMInput): TDMResult {
        return when (input.workflow) {
            TDMWorkflow.PRE -> preCalculator.calculate(input)
            TDMWorkflow.POST -> postCalculator.calculate(input)
            TDMWorkflow.PRE_POST -> prePostCalculator.calculate(input)
        }
    }
}