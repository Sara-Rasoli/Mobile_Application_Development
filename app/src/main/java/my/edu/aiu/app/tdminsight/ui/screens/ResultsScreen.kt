package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import my.edu.aiu.app.tdminsight.ui.components.AucTargetBanner
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ResultParameter
import my.edu.aiu.app.tdminsight.ui.components.ResultsParameterCard
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
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

            caseViewModel.updateTdmResult(result)
        }
    }

    val result =
        caseViewModel.tdmResult

    val patientInfo =
        caseViewModel.patientInfo

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppHeader(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
        ) {

            StepProgressBar(
                steps = TDM_STEPS,
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

            if (result != null) {

                // =================================================
                // AUC TARGET STATUS
                // =================================================

                result.auc24?.let { auc24 ->

                    AucTargetBanner(
                        auc24 = auc24,
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )
                }

                // =================================================
                // PHARMACOKINETIC PARAMETERS
                // =================================================

                val pharmacokineticParameters =
                    buildList {

                        /*
                         * IBW, dosing weight and creatinine
                         * clearance will be added in the
                         * next stage after the approved
                         * equations are confirmed.
                         */

                        add(
                            ResultParameter(
                                label = "Elimination Rate (Ke)",
                                value =
                                    "%.5f hr⁻¹"
                                        .format(result.ke)
                            )
                        )

                        add(
                            ResultParameter(
                                label = "Half-Life (t½)",
                                value =
                                    "%.2f hours"
                                        .format(
                                            result.halfLifeHr
                                        )
                            )
                        )

                        add(
                            ResultParameter(
                                label = "Volume of Distribution",
                                value =
                                    "%.2f L"
                                        .format(result.vd)
                            )
                        )

                        result.clearance?.let { clearance ->

                            add(
                                ResultParameter(
                                    label = "Clearance",
                                    value =
                                        "%.2f L/hr"
                                            .format(
                                                clearance
                                            )
                                )
                            )
                        }
                    }

                ResultsParameterCard(
                    title =
                        "Pharmacokinetic Parameters",
                    parameters =
                        pharmacokineticParameters,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // =================================================
                // EFFICACY PARAMETERS
                // =================================================

                val efficacyParameters =
                    buildList {

                        result.auc24?.let { auc24 ->

                            add(
                                ResultParameter(
                                    label = "AUC24",
                                    value =
                                        "%.2f mg·h/L"
                                            .format(auc24)
                                )
                            )
                        }

                        result.micMgL?.let { mic ->

                            add(
                                ResultParameter(
                                    label = "MIC",
                                    value =
                                        "%.2f mg/L"
                                            .format(mic)
                                )
                            )
                        }

                        result.aucMic?.let { aucMic ->

                            add(
                                ResultParameter(
                                    label = "AUC/MIC",
                                    value =
                                        "%.2f"
                                            .format(aucMic)
                                )
                            )
                        }

                        result.expectedCmax?.let { cmax ->

                            add(
                                ResultParameter(
                                    label = "Peak (Cmax)",
                                    value =
                                        "%.2f mg/L"
                                            .format(cmax)
                                )
                            )
                        }

                        result.expectedCmin?.let { cmin ->

                            add(
                                ResultParameter(
                                    label = "Trough (Cmin)",
                                    value =
                                        "%.2f mg/L"
                                            .format(cmin)
                                )
                            )
                        }
                    }

                if (efficacyParameters.isNotEmpty()) {

                    ResultsParameterCard(
                        title =
                            "Efficacy Parameters",
                        parameters =
                            efficacyParameters,
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )
                }

                // =================================================
                // STEP-BY-STEP EXPLANATION
                // =================================================

                PrimaryAppButton(
                    text =
                        "View Step-by-Step Explanation",
                    modifier =
                        Modifier.fillMaxWidth()
                ) {
                    navController.navigate(
                        AppRoutes.CALCULATION_EXPLANATION
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // =================================================
                // NEW CALCULATION
                // =================================================

                SecondaryAppButton(
                    text = "New Calculation",
                    modifier =
                        Modifier.fillMaxWidth()
                ) {
                    caseViewModel.reset()

                    navController.navigate(
                        AppRoutes.PATIENT_INFO
                    ) {
                        popUpTo(
                            AppRoutes.HOME
                        ) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text =
                        "Academic Prototype — For Educational and Demonstration Purposes Only",
                    style =
                        MaterialTheme.typography.labelSmall
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                AppFooter()

            } else {

                Text(
                    text =
                        "No input data found — please go back and complete the previous steps.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}