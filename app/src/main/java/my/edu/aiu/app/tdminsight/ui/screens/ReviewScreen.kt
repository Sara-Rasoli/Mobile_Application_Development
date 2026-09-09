package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun ReviewScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val patientInfo = caseViewModel.patientInfo
    val workflow = caseViewModel.selectedWorkflow

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 4)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Review Your Inputs", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            if (patientInfo != null && workflow != null) {
                SectionCard {
                    Text("Case ID: ${patientInfo.caseId}")
                    Text("Age: ${patientInfo.ageYears}")
                    Text("Weight: ${patientInfo.weightKg} kg")
                    Text("Serum Creatinine: ${patientInfo.serumCreatinine}")
                    Text("Paediatric: ${if (patientInfo.isPaediatric) "Yes" else "No"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Workflow: ${workflow.name.replace('_', '+')}")

                }
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryAppButton(text = "Run Calculation", modifier = Modifier.fillMaxWidth()) {
                    navController.navigate(AppRoutes.RESULTS)
                }
            } else {
                Text("Missing data — please go back and complete previous steps.")
            }
        }
    }
}