package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

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
fun CalculationExplanationScreen(navController: NavController) {

    val caseViewModel = rememberSharedCaseViewModel(navController)

    val steps = caseViewModel.tdmResult?.steps ?: emptyList()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Top application header
        AppHeader(navController)

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {

            // Progress indicator
            StepProgressBar(
                steps = TDM_STEPS,
                currentStepIndex = 6
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Screen title
            ScreenTitleRow(
                icon = Icons.Filled.MenuBook,
                title = "Calculation Explanation"
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Review how the pharmacokinetic parameters were calculated from the entered data.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (steps.isNotEmpty()) {

                steps.forEach { step ->

                    SectionCard {

                        Text(
                            text = step.label,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = step.value,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (step.note.isNotBlank()) {

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = step.note,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

            } else {

                SectionCard {

                    Text(
                        text = "No calculation data available",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please complete a TDM calculation before viewing the explanation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Back to Results button
            PrimaryAppButton(
                text = "Back to Results",
                modifier = Modifier.fillMaxWidth(),
                showIcon = false
            ) {
                navController.navigate(AppRoutes.RESULTS) {
                    popUpTo(AppRoutes.RESULTS) {
                        inclusive = true
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Back to Home button
            SecondaryAppButton(
                text = "Back to Home",
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.popBackStack(
                    route = AppRoutes.HOME,
                    inclusive = false
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}