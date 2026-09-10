package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
private fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ReviewScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val patientInfo = caseViewModel.patientInfo
    val workflow = caseViewModel.selectedWorkflow

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(navController)
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 4)
            Spacer(modifier = Modifier.height(8.dp))
            ScreenTitleRow(icon = Icons.Filled.FactCheck, title = "Review Calculation")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Please review all inputs before calculating.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (patientInfo != null && workflow != null) {
                SectionCard {
                    Text("Case", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReviewRow("Case ID", patientInfo.caseId)
                    ReviewRow("Patient Name", patientInfo.name)
                }
                Spacer(modifier = Modifier.height(12.dp))

                SectionCard {
                    Text("Patient", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReviewRow("Gender", patientInfo.gender.name.lowercase().replaceFirstChar { it.uppercase() })
                    ReviewRow("Age", "${patientInfo.ageYears} years")
                    ReviewRow("Height", "${patientInfo.heightCm} cm")
                    ReviewRow("Weight", "${patientInfo.weightKg} kg")
                    ReviewRow("Serum Creatinine", "${patientInfo.serumCreatinine} µmol/L")
                    ReviewRow("Paediatric", if (patientInfo.isPaediatric) "Yes" else "No")
                }
                Spacer(modifier = Modifier.height(12.dp))

                SectionCard {
                    Text("Workflow", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReviewRow("TDM Method", workflow.name.replace('_', '+'))
                }

                Spacer(modifier = Modifier.height(20.dp))
                PrimaryAppButton(text = "Confirm & Calculate", modifier = Modifier.fillMaxWidth()) {
                    navController.navigate(AppRoutes.RESULTS)
                }
            } else {
                Text("Missing data — please go back and complete previous steps.")
            }
        }
    }
}