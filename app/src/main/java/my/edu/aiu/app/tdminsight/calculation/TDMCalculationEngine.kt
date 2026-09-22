package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

class TDMCalculationEngine {

    private val preCalculator = PreCalculator()
    private val postCalculator = PostCalculator()
    private val prePostCalculator = PrePostCalculator()

    fun calculate(input: TDMInput, weightKg: Double = 70.0): TDMResult {
        return when (input) {
            is TDMInput.Pre -> preCalculator.calculate(input, weightKg = weightKg)
            is TDMInput.Post -> postCalculator.calculate(input, weightKg = weightKg)
            is TDMInput.PrePost -> prePostCalculator.calculate(input)
        }
    }
}
