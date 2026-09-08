package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

@Composable
fun WorkflowSelectionScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Select TDM Method", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        WorkflowOption("Pre", "Pre-dose (trough-based) workflow.") {
            caseViewModel.setWorkflow(TDMWorkflow.PRE)
            navController.navigate(AppRoutes.DYNAMIC_INPUT)
        }
        Spacer(modifier = Modifier.height(12.dp))
        WorkflowOption("Post", "Post-dose (peak/level-based) workflow.") {
            caseViewModel.setWorkflow(TDMWorkflow.POST)
            navController.navigate(AppRoutes.DYNAMIC_INPUT)
        }
        Spacer(modifier = Modifier.height(12.dp))
        WorkflowOption("Pre + Post", "Combined pre- and post-dose workflow.") {
            caseViewModel.setWorkflow(TDMWorkflow.PRE_POST)
            navController.navigate(AppRoutes.DYNAMIC_INPUT)
        }

        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = { navController.popBackStack() }) { Text("Back") }
    }
}

@Composable
private fun WorkflowOption(title: String, description: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
    }
}