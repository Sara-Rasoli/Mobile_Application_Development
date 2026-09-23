package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.model.CalculationStep

@Composable
fun CalculationExplorer(
    steps: List<CalculationStep>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = "Interactive Calculation Explorer",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "Tap any calculation to see its formula, result, meaning, and role in the TDM calculation.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.padding(top = 12.dp)
        )

        steps.forEachIndexed { index, step ->

            CalculationExplorerItem(
                step = step,
                stepNumber = index + 1
            )

            if (index < steps.lastIndex) {
                Spacer(
                    modifier = Modifier.padding(
                        top = 5.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun CalculationExplorerItem(
    step: CalculationStep,
    stepNumber: Int
) {
    var expanded by remember(step) {
        mutableStateOf(false)
    }

    val explanation =
        getCalculationExplanation(
            label = step.label,
            value = step.value,
            note = step.note
        )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (expanded) {
                    MaterialTheme.colorScheme
                        .surfaceContainerHighest
                } else {
                    MaterialTheme.colorScheme
                        .surfaceContainer
                }
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            // -------------------------------------------------
            // HEADER
            // -------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        if (explanation.isCalculated) {
                            Icons.Filled.CheckCircle
                        } else {
                            Icons.Filled.Functions
                        },
                    contentDescription = null,
                    tint =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.padding(
                        start = 10.dp
                    )
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "$stepNumber. ${step.label}",
                        style =
                            MaterialTheme.typography
                                .titleSmall
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            top = 2.dp
                        )
                    )

                    Text(
                        text = step.value,
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            MaterialTheme.colorScheme
                                .primary
                    )
                }

                Icon(
                    imageVector =
                        if (expanded) {
                            Icons.Filled.ExpandLess
                        } else {
                            Icons.Filled.ExpandMore
                        },
                    contentDescription =
                        if (expanded) {
                            "Collapse"
                        } else {
                            "Expand"
                        },
                    tint =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }

            // -------------------------------------------------
            // EXPANDED CONTENT
            // -------------------------------------------------

            AnimatedVisibility(
                visible = expanded
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 14.dp
                        )
                ) {

                    ExplanationSection(
                        title = "Formula / Method",
                        content = explanation.formula
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )

                    ExplanationSection(
                        title = "Calculated Result",
                        content =
                            explanation.result
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )

                    ExplanationSection(
                        title = "What does this mean?",
                        content =
                            explanation.meaning
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )

                    ExplanationSection(
                        title = "Why is it important?",
                        content =
                            explanation.importance
                    )

                    if (step.note.isNotBlank()) {

                        Spacer(
                            modifier = Modifier.padding(
                                top = 10.dp
                            )
                        )

                        ExplanationSection(
                            title = "Calculation note",
                            content = step.note
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExplanationSection(
    title: String,
    content: String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            style =
                MaterialTheme.typography
                    .labelLarge,
            color =
                MaterialTheme.colorScheme
                    .primary
        )

        Spacer(
            modifier = Modifier.padding(
                top = 3.dp
            )
        )

        Text(
            text = content,
            style =
                MaterialTheme.typography
                    .bodyMedium
        )
    }
}

private data class CalculationExplanation(
    val formula: String,
    val result: String,
    val meaning: String,
    val importance: String,
    val isCalculated: Boolean
)

private fun getCalculationExplanation(
    label: String,
    value: String,
    note: String
): CalculationExplanation {

    return when {

        label.equals(
            "Dose",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    "Entered dose = $value",
                result =
                    "Dose used in this calculation: $value",
                meaning =
                    "This is the amount of drug administered to the patient.",
                importance =
                    "The administered dose is used by the pharmacokinetic model to estimate drug exposure and other parameters.",
                isCalculated = false
            )
        }

        label.equals(
            "Dosing Interval",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    "Dosing interval = $value",
                result =
                    "Interval used: $value",
                meaning =
                    "This represents the time between scheduled doses.",
                importance =
                    "The dosing interval determines how the drug concentration changes between doses.",
                isCalculated = false
            )
        }

        label.contains(
            "Pre-dose",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    "Measured pre-dose concentration = $value",
                result =
                    "Measured trough level: $value",
                meaning =
                    "A pre-dose concentration represents the drug level immediately before the next scheduled dose.",
                importance =
                    "The trough level can be used to evaluate drug elimination and the concentration remaining before the next dose.",
                isCalculated = false
            )
        }

        label.contains(
            "Post-dose",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    "Measured post-dose concentration = $value",
                result =
                    "Measured post-dose level: $value",
                meaning =
                    "This represents the measured drug concentration after administration.",
                importance =
                    "The post-dose level provides information about drug exposure after the dose.",
                isCalculated = false
            )
        }

        label.contains(
            "Sampling Time",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    "Sampling time = $value",
                result =
                    "Sampling time used: $value",
                meaning =
                    "This is the time between the relevant dose or infusion event and collection of the drug sample.",
                importance =
                    "The timing of a concentration sample is essential because pharmacokinetic calculations depend on how concentration changes over time.",
                isCalculated = false
            )
        }

        label.contains(
            "Pre-to-Post Gap",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    "Δt = $value",
                result =
                    "Time gap used: $value",
                meaning =
                    "This is the time between the two measured concentrations.",
                importance =
                    "The concentration change over a known time interval is used to estimate the elimination rate.",
                isCalculated = false
            )
        }

        label.contains(
            "Elimination Rate",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    if (note.isNotBlank()) {
                        note
                    } else {
                        "Ke = ln(C₁ / C₂) / Δt"
                    },
                result =
                    "Ke = $value /hr",
                meaning =
                    "Ke describes the rate at which the drug concentration decreases from the body.",
                importance =
                    "Ke is a fundamental pharmacokinetic parameter. It is used to determine the elimination half-life and contributes to other calculations such as clearance.",
                isCalculated = true
            )
        }

        label.contains(
            "Half-life",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    if (note.isNotBlank()) {
                        note
                    } else {
                        "t½ = 0.693 / Ke"
                    },
                result =
                    "Half-life = $value",
                meaning =
                    "Half-life is the estimated time required for the drug concentration to decrease by approximately 50% during the elimination phase.",
                importance =
                    "Half-life helps describe how long the drug remains in the body and is an important pharmacokinetic parameter.",
                isCalculated = true
            )
        }

        label.contains(
            "Volume of Distribution",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    if (note.isNotBlank()) {
                        note
                    } else {
                        "Vd = Dose / Concentration"
                    },
                result =
                    "Vd = $value",
                meaning =
                    "Volume of distribution is an apparent volume that relates the amount of drug in the body to the measured concentration.",
                importance =
                    "Vd helps describe the apparent distribution of the drug throughout the body and is used in pharmacokinetic calculations.",
                isCalculated = true
            )
        }

        label.contains(
            "Clearance",
            ignoreCase = true
        ) -> {

            CalculationExplanation(
                formula =
                    if (note.isNotBlank()) {
                        note
                    } else {
                        "Clearance = Ke × Vd"
                    },
                result =
                    "Clearance = $value",
                meaning =
                    "Clearance describes the body's apparent ability to eliminate the drug from the circulation.",
                importance =
                    "Clearance is an important parameter for understanding drug elimination and exposure.",
                isCalculated = true
            )
        }

        else -> {

            CalculationExplanation(
                formula =
                    if (note.isNotBlank()) {
                        note
                    } else {
                        "See the calculation details above."
                    },
                result =
                    value,
                meaning =
                    "This value is part of the pharmacokinetic calculation workflow.",
                importance =
                    "This value contributes to the interpretation of the TDM calculation.",
                isCalculated = true
            )
        }
    }
}