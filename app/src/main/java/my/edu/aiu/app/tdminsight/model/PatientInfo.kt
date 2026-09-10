package my.edu.aiu.app.tdminsight.model

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

data class PatientInfo(
    val caseId: String,
    val name: String,
    val gender: Gender,
    val heightCm: Double,
    val weightKg: Double,
    val ageYears: Int,
    val serumCreatinine: Double,
    val isPaediatric: Boolean
)