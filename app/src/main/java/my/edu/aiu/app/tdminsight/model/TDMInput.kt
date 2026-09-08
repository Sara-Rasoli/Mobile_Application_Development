package my.edu.aiu.app.tdminsight.model

sealed class TDMInput {

    data class Pre(
        val doseMg: Double,
        val intervalHr: Double,
        val infusionDurationHr: Double,
        val preLevelConc: Double
    ) : TDMInput()

    data class Post(
        val doseMg: Double,
        val intervalHr: Double,
        val infusionDurationHr: Double,
        val samplingTimeHr: Double,
        val postLevelConc: Double
    ) : TDMInput()

    data class PrePost(
        val doseMg: Double,
        val intervalHr: Double,
        val infusionDurationHr: Double,
        val infusionToPostGapHr: Double,
        val preToPostGapHr: Double,
        val preLevelConc: Double,
        val postLevelConc: Double
    ) : TDMInput()
}