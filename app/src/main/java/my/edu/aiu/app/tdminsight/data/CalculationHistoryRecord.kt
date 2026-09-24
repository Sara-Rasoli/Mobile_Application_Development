package my.edu.aiu.app.tdminsight.data

data class CalculationHistoryRecord(
    val id: Long,
    val caseId: String,
    val patientName: String,
    val workflow: String,
    val createdAt: Long,

    val idealBodyWeightKg: Double,
    val dosingWeightKg: Double,
    val creatinineClearanceMlMin: Double,

    val ke: Double,
    val halfLifeHr: Double,
    val vd: Double,
    val clearance: Double?,

    val auc24: Double?,
    val micMgL: Double?,
    val aucMic: Double?,
    val expectedCmax: Double?,
    val expectedCmin: Double?
)