package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.Gender
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClinicalPlausibilityCheckerTest {

    @Test
    fun testTypicalPatientAllGood() {
        val patientInfo = PatientInfo(
            caseId = "CASE1",
            name = "Typical Adult",
            gender = Gender.MALE,
            heightCm = 175.0,
            weightKg = 70.0,
            ageYears = 40,
            serumCreatinine = 80.0, // SCr 80 umol/L -> CrCl ~ 96.7 mL/min -> KePred ~ 0.0846
            isPaediatric = false
        )

        // Calculated Ke 0.10 /hr (half-life 6.93 h) matches KePred 0.0846 closely
        val result = TDMResult(
            ke = 0.10,
            halfLifeHr = 6.93,
            vd = 45.0, // 45 / 70 = 0.643 L/kg
            clearance = 4.5,
            auc24 = 500.0
        )

        val report = ClinicalPlausibilityChecker.evaluate(result = result, patientInfo = patientInfo)

        assertTrue(report.isRenalCheckApplicable)
        assertNotNull(report.crClMlMin)
        assertNotNull(report.kePredicted)
        assertEquals(PlausibilityLevel.GOOD, report.overallLevel)
    }

    @Test
    fun testRenalImpairmentPatientConsistentSlowKe() {
        val patientInfo = PatientInfo(
            caseId = "CASE2",
            name = "Renal Impaired Adult",
            gender = Gender.FEMALE,
            heightCm = 160.0,
            weightKg = 60.0,
            ageYears = 75,
            serumCreatinine = 250.0, // High SCr -> Low CrCl ~ 16.3 mL/min -> KePred ~ 0.0179
            isPaediatric = false
        )

        // Calculated Ke 0.018 /hr matches expected slow KePred 0.0179
        val result = TDMResult(
            ke = 0.018,
            halfLifeHr = 38.5,
            vd = 42.0, // 42 / 60 = 0.7 L/kg
            clearance = 0.756,
            auc24 = 500.0
        )

        val report = ClinicalPlausibilityChecker.evaluate(result = result, patientInfo = patientInfo)

        assertTrue(report.isRenalCheckApplicable)
        val renalItem = report.items.first { it.parameterName.startsWith("Renal Consistency") }
        assertEquals(PlausibilityLevel.GOOD, renalItem.level)
    }

    @Test
    fun testMismatchedCaseNormalCrClWithVerySlowKeIsConcerning() {
        val patientInfo = PatientInfo(
            caseId = "CASE3",
            name = "Mismatched Patient",
            gender = Gender.MALE,
            heightCm = 175.0,
            weightKg = 70.0,
            ageYears = 30,
            serumCreatinine = 70.0, // Normal SCr -> CrCl ~ 116 mL/min -> KePred ~ 0.101
            isPaediatric = false
        )

        // Calculated Ke 0.015 /hr is 6.7x slower than predicted (~0.101 /hr)
        val result = TDMResult(
            ke = 0.015,
            halfLifeHr = 46.2,
            vd = 45.0,
            clearance = 0.675,
            auc24 = 500.0
        )

        val report = ClinicalPlausibilityChecker.evaluate(result = result, patientInfo = patientInfo)

        val renalItem = report.items.first { it.parameterName.startsWith("Renal Consistency") }
        assertEquals(PlausibilityLevel.CONCERNING, renalItem.level)
        assertEquals(PlausibilityLevel.CONCERNING, report.overallLevel)
    }

    @Test
    fun testPaediatricPatientSkippedRenalCrossCheck() {
        val patientInfo = PatientInfo(
            caseId = "CASE4",
            name = "Paediatric Patient",
            gender = Gender.MALE,
            heightCm = 110.0,
            weightKg = 20.0,
            ageYears = 6,
            serumCreatinine = 40.0,
            isPaediatric = true
        )

        val result = TDMResult(
            ke = 0.10,
            halfLifeHr = 6.93,
            vd = 13.0,
            clearance = 1.3,
            auc24 = 500.0
        )

        val report = ClinicalPlausibilityChecker.evaluate(result = result, patientInfo = patientInfo)

        assertFalse(report.isRenalCheckApplicable)
        val renalItem = report.items.first { it.parameterName.startsWith("Renal Consistency") }
        assertEquals("Not applicable", renalItem.valueFormatted)
    }

    @Test
    fun testAbsoluteRangeOutlierIsConcerning() {
        val patientInfo = PatientInfo(
            caseId = "CASE5",
            name = "Outlier Patient",
            gender = Gender.MALE,
            heightCm = 175.0,
            weightKg = 70.0,
            ageYears = 40,
            serumCreatinine = 80.0,
            isPaediatric = false
        )

        // Ke = 0.40 /hr is physiologically implausible (> 0.25)
        val result = TDMResult(
            ke = 0.40,
            halfLifeHr = 1.73,
            vd = 45.0,
            clearance = 18.0,
            auc24 = 500.0
        )

        val report = ClinicalPlausibilityChecker.evaluate(result = result, patientInfo = patientInfo)

        val keItem = report.items.first { it.parameterName == "Elimination Rate (Ke)" }
        assertEquals(PlausibilityLevel.CONCERNING, keItem.level)
        assertEquals(PlausibilityLevel.CONCERNING, report.overallLevel)
    }
}
