package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

class TDMCalculationEngine {
    private val preCalculator = PreCalculator()
    private val postCalculator = PostCalculator()
    private val prePostCalculator = PrePostCalculator()

    fun calculate(input: TDMInput): TDMResult {
        return when (input) {
            is TDMInput.Pre -> preCalculator.calculate(input)
            is TDMInput.Post -> postCalculator.calculate(input)
            is TDMInput.PrePost -> prePostCalculator.calculate(input)
        }
    }
}