package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.CalculationQualityCheck
import my.edu.aiu.app.tdminsight.ui.components.ConcentrationTimeGraph
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

private val TDM_STEPS = listOf(
    "Home",
    "Patient",
    "Workflow",
    "Inputs",
    "Review",
    "Results",
    "Explanation"
)

@Composable
private fun ResultRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ResultsScreen(
    navController: NavController
) {
    val caseViewModel =
        rememberSharedCaseViewModel(navController)

    val input =
        caseViewModel.tdmInput

    LaunchedEffect(input) {

        if (
            input != null &&
            caseViewModel.tdmResult == null
        ) {

            val result =
                TDMCalculationEngine()
                    .calculate(input)

            caseViewModel.updateTdmResult(
                result
            )
        }
    }

    val result =
        caseViewModel.tdmResult

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppHeader(
            navController
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp)
        ) {

            StepProgressBar(
                TDM_STEPS,
                currentStepIndex = 5
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ScreenTitleRow(
                icon = Icons.Filled.BarChart,
                title = "Results"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (
                result != null &&
                input != null
            ) {

                // =================================================
                // 1. CALCULATED PARAMETERS
                // =================================================

                SectionCard {

                    Text(
                        text = "Calculated Parameters",
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    ResultRow(
                        label =
                            "Elimination Rate (Ke)",
                        value =
                            "%.4f /hr"
                                .format(result.ke)
                    )

                    ResultRow(
                        label = "Half-life",
                        value =
                            "%.2f hr"
                                .format(
                                    result.halfLifeHr
                                )
                    )

                    ResultRow(
                        label =
                            "Volume of Distribution (Vd)",
                        value =
                            "%.2f L"
                                .format(
                                    result.vd
                                )
                    )

                    result.clearance?.let {

                        ResultRow(
                            label = "Clearance",
                            value =
                                "%.2f L/hr"
                                    .format(it)
                        )
                    }

                    result.expectedCmin?.let {

                        ResultRow(
                            label =
                                "Trough (Cmin)",
                            value =
                                "%.2f mg/L"
                                    .format(it)
                        )
                    }

                    result.expectedCmax?.let {

                        ResultRow(
                            label =
                                "Peak (Cmax)",
                            value =
                                "%.2f mg/L"
                                    .format(it)
                        )
                    }

                    result.auc24?.let {

                        ResultRow(
                            label = "AUC24",
                            value =
                                "%.2f mg·hr/L"
                                    .format(it)
                        )
                    }

                    result.newSuggestedDoseMg?.let {

                        ResultRow(
                            label =
                                "Suggested Dose",
                            value =
                                "%.0f mg"
                                    .format(it)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // 2. FEATURE 1
                // CONCENTRATION-TIME GRAPH
                // =================================================

                SectionCard {

                    ConcentrationTimeGraph(
                        input = input,
                        result = result,
                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // 3. FEATURE 2
                // CALCULATION QUALITY CHECK
                // =================================================

                CalculationQualityCheck(
                    input = input,
                    result = result,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =================================================
                // 4. EXISTING EXPLANATION BUTTON
                // =================================================

                PrimaryAppButton(
                    text = "View Explanation",
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    navController.navigate(
                        AppRoutes
                            .CALCULATION_EXPLANATION
                    )
                }

            } else {

                Text(
                    text =
                        "No input data found — please go back and complete previous steps.",
                    style =
                        MaterialTheme.typography
                            .bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            AppFooter()

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}