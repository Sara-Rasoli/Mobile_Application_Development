package my.edu.aiu.app.tdminsight

import my.edu.aiu.app.tdminsight.ui.camera.extractConcentrationCandidates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConcentrationParserTest {

    @Test
    fun testExtractConcentrationCandidates() {
        val text1 = "Vancomycin 12,5 mg/L"
        val res1 = extractConcentrationCandidates(text1)
        assertEquals(1, res1.size)
        assertEquals(12.5, res1[0], 0.001)

        val text2 = "Vanco: 8.0 MG/L"
        val res2 = extractConcentrationCandidates(text2)
        assertEquals(1, res2.size)
        assertEquals(8.0, res2[0], 0.001)

        val text3 = "Patient lab report details with no valid number mg/L or mcg/mL"
        val res3 = extractConcentrationCandidates(text3)
        assertTrue(res3.isEmpty())

        val text4 = "Multiple levels found: Pre: 15.4 mg/L and Post: 35,2 mcg/mL"
        val res4 = extractConcentrationCandidates(text4)
        assertEquals(2, res4.size)
        assertEquals(15.4, res4[0], 0.001)
        assertEquals(35.2, res4[1], 0.001)
    }
}
