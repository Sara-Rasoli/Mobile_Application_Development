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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.data.TDMHistoryRepository
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

private val TDM_STEPS =
    listOf(
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
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 4.dp
                ),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style =
                MaterialTheme.typography
                    .bodyMedium
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography
                    .titleMedium
        )
    }
}

@Composable
fun ResultsScreen(
    navController: NavController
) {

    val caseViewModel =
        rememberSharedCaseViewModel(
            navController
        )

    val context =
        LocalContext.current

    val historyRepository =
        remember(context) {
            TDMHistoryRepository(
                context
            )
        }

    val input =
        caseViewModel.tdmInput

    val patient =
        caseViewModel.patientInfo

    val workflow =
        caseViewModel.selectedWorkflow

    // =============================================================
    // CALCULATE RESULT
    // =============================================================

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

    // =============================================================
    // SAVE COMPLETED CALCULATION
    // =============================================================

    LaunchedEffect(
        patient,
        workflow,
        input,
        result
    ) {

        if (
            patient != null &&
            workflow != null &&
            input != null &&
            result != null
        ) {

            withContext(
                Dispatchers.IO
            ) {

                historyRepository.saveCase(
                    patient = patient,
                    workflow = workflow,
                    input = input,
                    result = result
                )
            }
        }
    }

    // =============================================================
    // SCREEN
    // =============================================================

    Column(
        modifier =
            Modifier.fillMaxSize()
    ) {

        AppHeader(
            navController
        )

        Column(
            modifier =
                Modifier
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
                modifier =
                    Modifier.height(12.dp)
            )

            ScreenTitleRow(
                icon =
                    Icons.Filled.BarChart,
                title = "Results"
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            if (
                result != null &&
                input != null
            ) {

                // =================================================
                // 1. PHARMACOKINETIC PARAMETERS
                // =================================================

                SectionCard {

                    Text(
                        text =
                            "Pharmacokinetic Parameters",
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    ResultRow(
                        label =
                            "Elimination Rate (Ke)",
                        value =
                            "%.5f /hr"
                                .format(
                                    result.ke
                                )
                    )

                    ResultRow(
                        label =
                            "Half-Life (t½)",
                        value =
                            "%.2f hours"
                                .format(
                                    result.halfLifeHr
                                )
                    )

                    ResultRow(
                        label =
                            "Volume of Distribution",
                        value =
                            "%.2f L"
                                .format(
                                    result.vd
                                )
                    )

                    result.clearance?.let {

                        ResultRow(
                            label =
                                "Clearance",
                            value =
                                "%.2f L/hr"
                                    .format(it)
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                // =================================================
                // 2. EFFICACY PARAMETERS
                // =================================================

                SectionCard {

                    Text(
                        text =
                            "Efficacy Parameters",
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    result.auc24?.let {

                        ResultRow(
                            label =
                                "AUC₂₄",
                            value =
                                "%.2f mg•h/L"
                                    .format(it)
                        )
                    }

                    result.micMgL?.let {

                        ResultRow(
                            label =
                                "MIC",
                            value =
                                "%.2f mg/L"
                                    .format(it)
                        )
                    }

                    result.aucMic?.let {

                        ResultRow(
                            label =
                                "AUC/MIC",
                            value =
                                "%.2f"
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

                    result.expectedCmin?.let {

                        ResultRow(
                            label =
                                "Trough (Cmin)",
                            value =
                                "%.2f mg/L"
                                    .format(it)
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                // =================================================
                // 3. CONCENTRATION-TIME GRAPH
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
                    modifier =
                        Modifier.height(16.dp)
                )

                // =================================================
                // 4. CALCULATION QUALITY CHECK
                // =================================================

                CalculationQualityCheck(
                    input = input,
                    result = result,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                // =================================================
                // 5. EXPLANATION
                // =================================================

                PrimaryAppButton(
                    text =
                        "View Step-by-Step Explanation",
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
                        "No calculation result found. Please go back and complete the calculation.",
                    style =
                        MaterialTheme.typography
                            .bodyMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            AppFooter()

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }
    }
}