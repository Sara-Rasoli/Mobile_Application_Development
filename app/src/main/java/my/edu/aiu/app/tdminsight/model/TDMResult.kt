package my.edu.aiu.app.tdminsight.model

data class TDMResult(
    val ke: Double,
    val halfLifeHr: Double,
    val vd: Double,
    val clearance: Double? = null,
    val auc24: Double? = null,
    val expectedCmin: Double? = null,
    val expectedCmax: Double? = null,
    val newSuggestedDoseMg: Double? = null,
    val steps: List<CalculationStep> = emptyList()
)