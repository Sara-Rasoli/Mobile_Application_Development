package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun WorkflowSelectionScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 2)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Select TDM Method", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            WorkflowOption("Vancomycin Pre", "Pre-dose (trough-based) workflow.") {
                caseViewModel.setWorkflow(TDMWorkflow.PRE)
                navController.navigate(AppRoutes.DYNAMIC_INPUT)
            }
            Spacer(modifier = Modifier.height(12.dp))
            WorkflowOption("Vancomycin Post", "Post-dose (peak/level-based) workflow.") {
                caseViewModel.setWorkflow(TDMWorkflow.POST)
                navController.navigate(AppRoutes.DYNAMIC_INPUT)
            }
            Spacer(modifier = Modifier.height(12.dp))
            WorkflowOption("Vancomycin Pre + Post", "Combined pre- and post-dose workflow.") {
                caseViewModel.setWorkflow(TDMWorkflow.PRE_POST)
                navController.navigate(AppRoutes.DYNAMIC_INPUT)
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("Back") }
        }
    }
}

@Composable
private fun WorkflowOption(title: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, my.edu.aiu.app.tdminsight.ui.theme.BorderLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}