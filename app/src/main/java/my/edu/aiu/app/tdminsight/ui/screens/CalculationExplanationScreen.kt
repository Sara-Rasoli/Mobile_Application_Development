package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

@Composable
fun CalculationExplanationScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val steps = caseViewModel.tdmResult?.steps ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Calculation Explanation", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        steps.forEach { step ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(step.label, style = MaterialTheme.typography.titleSmall)
                    Text(step.value, style = MaterialTheme.typography.bodyMedium)
                    if (step.note.isNotBlank()) {
                        Text(step.note, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack(route = AppRoutes.HOME, inclusive = false) }) {
            Text("Back to Home")
        }
    }
}