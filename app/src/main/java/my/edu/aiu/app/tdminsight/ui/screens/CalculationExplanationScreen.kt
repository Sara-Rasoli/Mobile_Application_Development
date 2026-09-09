package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun CalculationExplanationScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val steps = caseViewModel.tdmResult?.steps ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 6)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Calculation Explanation", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            steps.forEach { step ->
                SectionCard {
                    Text(step.label, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(step.value, style = MaterialTheme.typography.bodyMedium)
                    if (step.note.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(step.note, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            SecondaryAppButton(text = "Back to Home", modifier = Modifier.fillMaxWidth()) {
                navController.popBackStack(route = AppRoutes.HOME, inclusive = false)
            }
        }
    }
}