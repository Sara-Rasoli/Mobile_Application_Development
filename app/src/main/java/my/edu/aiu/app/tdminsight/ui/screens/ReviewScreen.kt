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
fun ReviewScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val patientInfo = caseViewModel.patientInfo
    val workflow = caseViewModel.selectedWorkflow

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Review Your Inputs", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (patientInfo != null && workflow != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Case ID: ${patientInfo.caseId}")
                    Text("Age: ${patientInfo.ageYears}")
                    Text("Weight: ${patientInfo.weightKg} kg")
                    Text("Serum Creatinine: ${patientInfo.serumCreatinine}")
                    Text("Paediatric: ${if (patientInfo.isPaediatric) "Yes" else "No"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Workflow: ${workflow.name.replace('_', '+')}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(AppRoutes.RESULTS) }) {
                Text("Run Calculation")
            }
        } else {
            Text("Missing data — please go back and complete previous steps.")
        }
    }
}