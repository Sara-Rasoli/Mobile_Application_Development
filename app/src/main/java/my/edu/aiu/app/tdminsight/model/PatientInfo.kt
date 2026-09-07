package my.edu.aiu.app.tdminsight.model

data class PatientInfo(
    val caseId: String,
    val weightKg: Double,
    val ageYears: Int,
    val serumCreatinine: Double,
    val isPaediatric: Boolean
)