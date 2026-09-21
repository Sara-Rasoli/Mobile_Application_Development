package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.abs
import kotlin.math.ln

private data class QualityCheckItem(
    val name: String,
    val passed: Boolean,
    val detail: String
)

@Composable
fun CalculationQualityCheck(
    input: TDMInput,
    result: TDMResult,
    modifier: Modifier = Modifier
) {
    val checks = createQualityChecks(
        input = input,
        result = result
    )

    val allPassed = checks.all { it.passed }

    val successColor =
        MaterialTheme.colorScheme.primary

    val errorColor =
        MaterialTheme.colorScheme.error

    val containerColor =
        if (allPassed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        }

    val titleColor =
        if (allPassed) {
            successColor
        } else {
            errorColor
        }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Calculation Quality Check",
                style = MaterialTheme.typography.titleMedium,
                color = titleColor
            )

            Spacer(
                modifier = Modifier.padding(
                    top = 2.dp
                )
            )

            Text(
                text = "Educational calculation validation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.padding(
                    top = 8.dp
                )
            )

            checks.forEach { check ->

                QualityCheckRow(
                    check = check
                )
            }

            Spacer(
                modifier = Modifier.padding(
                    top = 8.dp
                )
            )

            if (allPassed) {

                Text(
                    text = "✓ Calculation checks passed",
                    style = MaterialTheme.typography.titleSmall,
                    color = successColor
                )

                Spacer(
                    modifier = Modifier.padding(
                        top = 2.dp
                    )
                )

                Text(
                    text = "The calculated result passed the application's input and mathematical consistency checks.",
                    style = MaterialTheme.typography.bodySmall
                )

            } else {

                Text(
                    text = "⚠ Review required",
                    style = MaterialTheme.typography.titleSmall,
                    color = errorColor
                )

                Spacer(
                    modifier = Modifier.padding(
                        top = 2.dp
                    )
                )

                Text(
                    text = "One or more checks did not pass. Review the inputs and calculation before interpreting the result.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun QualityCheckRow(
    check: QualityCheckItem
) {
    val statusColor =
        if (check.passed) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text =
                    if (check.passed) {
                        "✓"
                    } else {
                        "!"
                    },
                style =
                    MaterialTheme.typography
                        .titleSmall,
                color = statusColor
            )

            Spacer(
                modifier = Modifier.padding(
                    start = 8.dp
                )
            )

            Column {

                Text(
                    text = check.name,
                    style =
                        MaterialTheme.typography
                            .bodyMedium
                )

                Text(
                    text = check.detail,
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }
        }

        Text(
            text =
                if (check.passed) {
                    "Passed"
                } else {
                    "Review"
                },
            style =
                MaterialTheme.typography
                    .labelMedium,
            color = statusColor
        )
    }
}

private fun createQualityChecks(
    input: TDMInput,
    result: TDMResult
): List<QualityCheckItem> {

    return listOf(

        createInputCheck(input),

        createTimingCheck(input),

        createEliminationRateCheck(result),

        createHalfLifeCheck(result),

        createPharmacokineticValuesCheck(result),

        createCalculationStepsCheck(result)
    )
}

private fun createInputCheck(
    input: TDMInput
): QualityCheckItem {

    val values = when (input) {

        is TDMInput.Pre -> {
            listOf(
                input.doseMg,
                input.intervalHr,
                input.infusionDurationHr,
                input.preLevelConc
            )
        }

        is TDMInput.Post -> {
            listOf(
                input.doseMg,
                input.intervalHr,
                input.infusionDurationHr,
                input.samplingTimeHr,
                input.postLevelConc
            )
        }

        is TDMInput.PrePost -> {
            listOf(
                input.doseMg,
                input.intervalHr,
                input.infusionDurationHr,
                input.infusionToPostGapHr,
                input.preToPostGapHr,
                input.preLevelConc,
                input.postLevelConc
            )
        }
    }

    val passed =
        values.all {
            it.isFinite() && it > 0.0
        }

    return QualityCheckItem(
        name = "Input values",
        passed = passed,
        detail =
            if (passed) {
                "Required numerical values are present."
            } else {
                "One or more input values are invalid."
            }
    )
}

private fun createTimingCheck(
    input: TDMInput
): QualityCheckItem {

    val passed = when (input) {

        is TDMInput.Pre -> {
            input.intervalHr > 0.0 &&
                    input.infusionDurationHr > 0.0 &&
                    input.infusionDurationHr <=
                    input.intervalHr
        }

        is TDMInput.Post -> {
            input.intervalHr > 0.0 &&
                    input.samplingTimeHr > 0.0 &&
                    input.samplingTimeHr <
                    input.intervalHr
        }

        is TDMInput.PrePost -> {
            input.intervalHr > 0.0 &&
                    input.infusionToPostGapHr > 0.0 &&
                    input.preToPostGapHr > 0.0 &&
                    input.preToPostGapHr <
                    input.intervalHr
        }
    }

    return QualityCheckItem(
        name = "Sampling timing",
        passed = passed,
        detail =
            if (passed) {
                "Timing values are within the selected dosing interval."
            } else {
                "Review the sampling and dosing-time relationship."
            }
    )
}

private fun createEliminationRateCheck(
    result: TDMResult
): QualityCheckItem {

    val passed =
        result.ke.isFinite() &&
                result.ke > 0.0

    return QualityCheckItem(
        name = "Elimination rate",
        passed = passed,
        detail =
            if (passed) {
                "Ke is positive and mathematically usable."
            } else {
                "Ke is zero, negative, or not a valid number."
            }
    )
}

private fun createHalfLifeCheck(
    result: TDMResult
): QualityCheckItem {

    if (
        !result.ke.isFinite() ||
        result.ke <= 0.0 ||
        !result.halfLifeHr.isFinite() ||
        result.halfLifeHr <= 0.0
    ) {
        return QualityCheckItem(
            name = "Half-life consistency",
            passed = false,
            detail =
                "Half-life cannot be validated from the current Ke."
        )
    }

    val expectedHalfLife =
        ln(2.0) / result.ke

    val difference =
        abs(
            result.halfLifeHr -
                    expectedHalfLife
        )

    val tolerance =
        maxOf(
            0.05,
            expectedHalfLife * 0.01
        )

    val passed =
        difference <= tolerance

    return QualityCheckItem(
        name = "Half-life consistency",
        passed = passed,
        detail =
            if (passed) {
                "Half-life is consistent with Ke."
            } else {
                "Calculated half-life differs from ln(2) / Ke."
            }
    )
}

private fun createPharmacokineticValuesCheck(
    result: TDMResult
): QualityCheckItem {

    val requiredValuesValid =
        result.ke.isFinite() &&
                result.ke > 0.0 &&
                result.halfLifeHr.isFinite() &&
                result.halfLifeHr > 0.0 &&
                result.vd.isFinite() &&
                result.vd > 0.0

    val optionalValuesValid =
        listOf(
            result.clearance,
            result.auc24,
            result.expectedCmin,
            result.expectedCmax,
            result.newSuggestedDoseMg
        ).all { value ->
            value == null ||
                    (
                            value.isFinite() &&
                                    value >= 0.0
                            )
        }

    val passed =
        requiredValuesValid &&
                optionalValuesValid

    return QualityCheckItem(
        name = "Pharmacokinetic values",
        passed = passed,
        detail =
            if (passed) {
                "Calculated parameters are finite and within valid mathematical ranges."
            } else {
                "One or more calculated parameters need review."
            }
    )
}

private fun createCalculationStepsCheck(
    result: TDMResult
): QualityCheckItem {

    val passed =
        result.steps.isNotEmpty()

    return QualityCheckItem(
        name = "Calculation steps",
        passed = passed,
        detail =
            if (passed) {
                "${result.steps.size} calculation steps were generated."
            } else {
                "No calculation steps were generated."
            }
    )
}