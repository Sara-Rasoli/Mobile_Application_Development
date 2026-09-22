package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.TDMInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class PkCalculatorsTest {

    @Test
    fun testPkMathSteadyStateConcentrationAndSolver() {
        val dose = 1000.0
        val tinf = 1.0
        val tau = 12.0
        val trueKe = 0.08
        val trueVd = 45.0

        val cmaxSs = PkMath.calculateCmaxSs(dose, tinf, tau, trueKe, trueVd)
        val cminSs = PkMath.calculateCminSs(cmaxSs, trueKe, tau, tinf)

        // Verify boundary concentrations
        val cAt0 = PkMath.calculateConcentration(dose, tinf, tau, trueKe, trueVd, 0.0)
        val cAtTinf = PkMath.calculateConcentration(dose, tinf, tau, trueKe, trueVd, tinf)
        val cAtTau = PkMath.calculateConcentration(dose, tinf, tau, trueKe, trueVd, tau)

        assertEquals(cminSs, cAt0, 0.001)
        assertEquals(cmaxSs, cAtTinf, 0.001)
        assertEquals(cminSs, cAtTau, 0.001)

        // Test bisection search solver
        val solvedKe = PkMath.solveKeForConcentration(dose, tinf, tau, trueVd, tau, cminSs)
        assertEquals(trueKe, solvedKe, 0.0001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSolveKeOutOfBoundsThrowsException() {
        val dose = 1000.0
        val tinf = 1.0
        val tau = 12.0
        val vd = 45.0
        // Measured concentration 5000 mg/L exceeds maximum model concentration at minKe=0.001 (~1862 mg/L)
        PkMath.solveKeForConcentration(dose, tinf, tau, vd, tau, 5000.0)
    }

    @Test
    fun testVdCalculatedFromEnteredWeight60KgAnd100Kg() {
        val inputPre = TDMInput.Pre(
            doseMg = 1000.0,
            intervalHr = 12.0,
            infusionDurationHr = 1.0,
            preLevelConc = 15.0
        )
        val inputPost = TDMInput.Post(
            doseMg = 1000.0,
            intervalHr = 12.0,
            infusionDurationHr = 1.0,
            samplingTimeHr = 1.0,
            postLevelConc = 30.0
        )

        // Test with 60.0 kg -> expected Vd = 0.7 * 60 = 42.0 L
        val resultPre60 = PreCalculator().calculate(inputPre, weightKg = 60.0)
        val resultPost60 = PostCalculator().calculate(inputPost, weightKg = 60.0)
        val engineResult60 = TDMCalculationEngine().calculate(inputPre, weightKg = 60.0)

        assertEquals(42.0, resultPre60.vd, 0.001)
        assertEquals(42.0, resultPost60.vd, 0.001)
        assertEquals(42.0, engineResult60.vd, 0.001)

        // Test with 100.0 kg -> expected Vd = 0.7 * 100 = 70.0 L
        val resultPre100 = PreCalculator().calculate(inputPre, weightKg = 100.0)
        val resultPost100 = PostCalculator().calculate(inputPost, weightKg = 100.0)
        val engineResult100 = TDMCalculationEngine().calculate(inputPre, weightKg = 100.0)

        assertEquals(70.0, resultPre100.vd, 0.001)
        assertEquals(70.0, resultPost100.vd, 0.001)
        assertEquals(70.0, engineResult100.vd, 0.001)

        assertNotEquals(resultPre60.vd, resultPre100.vd, 0.001)
    }

    @Test
    fun testPreCalculatorRecoversTrueKe() {
        val dose = 1000.0
        val tinf = 1.0
        val tau = 12.0
        val trueKe = 0.08
        val weightKg = 64.2857142857 // 0.7 * 64.2857 = 45.0 L Vd
        val vd = 0.7 * weightKg

        val trough = PkMath.calculateConcentration(dose, tinf, tau, trueKe, vd, tau)
        val input = TDMInput.Pre(
            doseMg = dose,
            intervalHr = tau,
            infusionDurationHr = tinf,
            preLevelConc = trough
        )

        val calculator = PreCalculator()
        val result = calculator.calculate(input, weightKg = weightKg)

        assertEquals(trueKe, result.ke, 0.0001)
        assertEquals(45.0, result.vd, 0.01)
        assertEquals(8.664, result.halfLifeHr, 0.01)
        assertEquals(3.60, result.clearance!!, 0.01)
        assertEquals(277.78, result.aucTau!!, 0.1)
        assertEquals(555.56, result.auc24!!, 0.1)
        assertNotNull(result.steps)
    }

    @Test
    fun testPostCalculatorRecoversTrueKe() {
        val dose = 1000.0
        val tinf = 1.0
        val tau = 12.0
        val trueKe = 0.08
        val weightKg = 64.2857142857
        val vd = 0.7 * weightKg
        val hoursAfterInfusionEnd = 1.0
        val sampleTime = tinf + hoursAfterInfusionEnd

        val peak = PkMath.calculateConcentration(dose, tinf, tau, trueKe, vd, sampleTime)
        val input = TDMInput.Post(
            doseMg = dose,
            intervalHr = tau,
            infusionDurationHr = tinf,
            samplingTimeHr = hoursAfterInfusionEnd,
            postLevelConc = peak
        )

        val calculator = PostCalculator()
        val result = calculator.calculate(input, weightKg = weightKg)

        assertEquals(trueKe, result.ke, 0.0001)
        assertEquals(45.0, result.vd, 0.01)
        assertEquals(8.664, result.halfLifeHr, 0.01)
        assertEquals(3.60, result.clearance!!, 0.01)
        assertEquals(277.78, result.aucTau!!, 0.1)
        assertEquals(555.56, result.auc24!!, 0.1)
        assertNotNull(result.steps)
    }

    @Test
    fun testPrePostCalculatorRecoversTrueKeAndVd() {
        val dose = 1000.0
        val tinf = 1.0
        val tau = 12.0
        val trueKe = 0.08
        val trueVd = 45.0
        val hoursAfterInfusionEnd = 1.0
        val tPost = tinf + hoursAfterInfusionEnd // 2.0 h
        val tPre = tau // 12.0 h
        val deltaT = tPre - tPost // 10.0 h

        val cPost = PkMath.calculateConcentration(dose, tinf, tau, trueKe, trueVd, tPost)
        val cPre = PkMath.calculateConcentration(dose, tinf, tau, trueKe, trueVd, tPre)

        val input = TDMInput.PrePost(
            doseMg = dose,
            intervalHr = tau,
            infusionDurationHr = tinf,
            infusionToPostGapHr = hoursAfterInfusionEnd,
            preToPostGapHr = deltaT,
            preLevelConc = cPre,
            postLevelConc = cPost
        )

        val calculator = PrePostCalculator()
        val result = calculator.calculate(input)

        assertEquals(trueKe, result.ke, 0.001)
        assertEquals(trueVd, result.vd, 0.1)
        assertEquals(8.664, result.halfLifeHr, 0.01)
        assertEquals(3.60, result.clearance!!, 0.1)
        assertEquals(277.78, result.aucTau!!, 1.0)
        assertEquals(555.56, result.auc24!!, 2.0)
        assertNotNull(result.steps)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPrePostCalculatorThrowsWhenCpostLessOrEqualCpre() {
        val input = TDMInput.PrePost(
            doseMg = 1000.0,
            intervalHr = 12.0,
            infusionDurationHr = 1.0,
            infusionToPostGapHr = 1.0,
            preToPostGapHr = 10.0,
            preLevelConc = 20.0,
            postLevelConc = 15.0 // Invalid: Cpost <= Cpre
        )
        PrePostCalculator().calculate(input)
    }
}
