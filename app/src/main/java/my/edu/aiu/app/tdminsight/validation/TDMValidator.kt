package my.edu.aiu.app.tdminsight.validation

import my.edu.aiu.app.tdminsight.model.TDMInput

object TDMValidator {
    private const val MIN_DOSE_MG = 100.0
    private const val MAX_DOSE_MG = 3000.0
    private const val MIN_INTERVAL_HR = 4.0
    private const val MAX_INTERVAL_HR = 48.0
    private const val MIN_LEVEL_CONC = 0.1
    private const val MAX_LEVEL_CONC = 100.0
    private const val MIN_TIME_HR = 0.1
    private const val MAX_TIME_HR = 24.0
    private const val MIN_INFUSION_DURATION_HR = 0.5
    private const val MAX_INFUSION_DURATION_HR = 4.0

    data class ValidationResult(
        val input: TDMInput?,
        val errors: Map<String, String>
    )

    private fun parsePositive(
        text: String,
        fieldLabel: String,
        min: Double,
        max: Double,
        errors: MutableMap<String, String>,
        key: String
    ): Double? {
        val value = text.toDoubleOrNull()
        return when {
            text.isBlank() -> { errors[key] = "$fieldLabel is required."; null }
            value == null -> { errors[key] = "$fieldLabel must be a number."; null }
            value < min || value > max -> {
                errors[key] = "$fieldLabel must be between $min and $max."
                null
            }
            else -> value
        }
    }

    fun validatePre(
        doseMgText: String,
        intervalHrText: String,
        infusionDurationHrText: String,
        preLevelText: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()
        val doseMg = parsePositive(doseMgText, "Dose", MIN_DOSE_MG, MAX_DOSE_MG, errors, "doseMg")
        val intervalHr = parsePositive(intervalHrText, "Dosing interval", MIN_INTERVAL_HR, MAX_INTERVAL_HR, errors, "intervalHr")
        val infusionDurationHr = parsePositive(infusionDurationHrText, "Infusion duration", MIN_INFUSION_DURATION_HR, MAX_INFUSION_DURATION_HR, errors, "infusionDurationHr")
        val preLevelConc = parsePositive(preLevelText, "Pre-dose level", MIN_LEVEL_CONC, MAX_LEVEL_CONC, errors, "preLevelConc")

        val input = if (errors.isEmpty() && doseMg != null && intervalHr != null &&
            infusionDurationHr != null && preLevelConc != null
        ) {
            TDMInput.Pre(
                doseMg = doseMg,
                intervalHr = intervalHr,
                infusionDurationHr = infusionDurationHr,
                preLevelConc = preLevelConc
            )
        } else null

        return ValidationResult(input, errors)
    }

    fun validatePost(
        doseMgText: String,
        intervalHrText: String,
        infusionDurationHrText: String,
        samplingTimeHrText: String,
        postLevelText: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()
        val doseMg = parsePositive(doseMgText, "Dose", MIN_DOSE_MG, MAX_DOSE_MG, errors, "doseMg")
        val intervalHr = parsePositive(intervalHrText, "Dosing interval", MIN_INTERVAL_HR, MAX_INTERVAL_HR, errors, "intervalHr")
        val infusionDurationHr = parsePositive(infusionDurationHrText, "Infusion duration", MIN_INFUSION_DURATION_HR, MAX_INFUSION_DURATION_HR, errors, "infusionDurationHr")
        val samplingTimeHr = parsePositive(samplingTimeHrText, "Sampling time", MIN_TIME_HR, MAX_TIME_HR, errors, "samplingTimeHr")
        val postLevelConc = parsePositive(postLevelText, "Post-dose level", MIN_LEVEL_CONC, MAX_LEVEL_CONC, errors, "postLevelConc")

        // Cross-field check: sampling time must occur before the next dose is due.
        if (samplingTimeHr != null && intervalHr != null && samplingTimeHr >= intervalHr) {
            errors["samplingTimeHr"] = "Sampling time must be less than the dosing interval."
        }

        val input = if (errors.isEmpty() && doseMg != null && intervalHr != null &&
            infusionDurationHr != null && samplingTimeHr != null && postLevelConc != null
        ) {
            TDMInput.Post(
                doseMg = doseMg,
                intervalHr = intervalHr,
                infusionDurationHr = infusionDurationHr,
                samplingTimeHr = samplingTimeHr,
                postLevelConc = postLevelConc
            )
        } else null

        return ValidationResult(input, errors)
    }

    fun validatePrePost(
        doseMgText: String,
        intervalHrText: String,
        infusionDurationHrText: String,
        infusionToPostGapHrText: String,
        preToPostGapHrText: String,
        preLevelText: String,
        postLevelText: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()
        val doseMg = parsePositive(doseMgText, "Dose", MIN_DOSE_MG, MAX_DOSE_MG, errors, "doseMg")
        val intervalHr = parsePositive(intervalHrText, "Dosing interval", MIN_INTERVAL_HR, MAX_INTERVAL_HR, errors, "intervalHr")
        val infusionDurationHr = parsePositive(infusionDurationHrText, "Infusion duration", MIN_INFUSION_DURATION_HR, MAX_INFUSION_DURATION_HR, errors, "infusionDurationHr")
        val infusionToPostGapHr = parsePositive(infusionToPostGapHrText, "Infusion-to-post gap", MIN_TIME_HR, MAX_TIME_HR, errors, "infusionToPostGapHr")
        val preToPostGapHr = parsePositive(preToPostGapHrText, "Pre-to-post gap", MIN_TIME_HR, MAX_TIME_HR, errors, "preToPostGapHr")
        val preLevelConc = parsePositive(preLevelText, "Pre-dose level", MIN_LEVEL_CONC, MAX_LEVEL_CONC, errors, "preLevelConc")
        val postLevelConc = parsePositive(postLevelText, "Post-dose level", MIN_LEVEL_CONC, MAX_LEVEL_CONC, errors, "postLevelConc")

        // Cross-field check: the two sample times must fit inside a single dosing interval.
        if (preToPostGapHr != null && intervalHr != null && preToPostGapHr >= intervalHr) {
            errors["preToPostGapHr"] = "Pre-to-post gap must be less than the dosing interval."
        }
        // Cross-field check: a post-dose (peak) level should be higher than the pre-dose (trough) level.
        if (preLevelConc != null && postLevelConc != null && postLevelConc <= preLevelConc) {
            errors["postLevelConc"] = "Post-dose level should normally be higher than pre-dose level."
        }

        val input = if (errors.isEmpty() && doseMg != null && intervalHr != null &&
            infusionDurationHr != null && infusionToPostGapHr != null &&
            preToPostGapHr != null && preLevelConc != null && postLevelConc != null
        ) {
            TDMInput.PrePost(
                doseMg = doseMg,
                intervalHr = intervalHr,
                infusionDurationHr = infusionDurationHr,
                infusionToPostGapHr = infusionToPostGapHr,
                preToPostGapHr = preToPostGapHr,
                preLevelConc = preLevelConc,
                postLevelConc = postLevelConc
            )
        } else null

        return ValidationResult(input, errors)
    }
}