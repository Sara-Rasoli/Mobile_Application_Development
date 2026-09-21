package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
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
private fun SectionHeaderWithEdit(title: String, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.clickable(onClick = onEditClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = TealPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Edit", color = TealPrimary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ReviewScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val patientInfo = caseViewModel.patientInfo
    val workflow = caseViewModel.selectedWorkflow
    val tdmInput = caseViewModel.tdmInput

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(navController)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 4)
            Spacer(modifier = Modifier.height(8.dp))
            ScreenTitleRow(icon = Icons.Filled.FactCheck, title = "Review Calculation")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Please review all inputs before calculating. Tap Edit on any section to make changes.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (patientInfo != null && workflow != null) {
                SectionCard {
                    SectionHeaderWithEdit("Case & Patient") {
                        navController.navigate(AppRoutes.PATIENT_INFO)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ReviewRow("Case ID", patientInfo.caseId)
                    ReviewRow("Patient Name", patientInfo.name)
                    ReviewRow("Gender", patientInfo.gender.name.lowercase().replaceFirstChar { it.uppercase() })
                    ReviewRow("Age", "${patientInfo.ageYears} years")
                    ReviewRow("Height", "${patientInfo.heightCm} cm")
                    ReviewRow("Weight", "${patientInfo.weightKg} kg")
                    ReviewRow("Serum Creatinine", "${patientInfo.serumCreatinine} µmol/L")
                    ReviewRow("Paediatric", if (patientInfo.isPaediatric) "Yes" else "No")
                }
                Spacer(modifier = Modifier.height(12.dp))

                SectionCard {
                    SectionHeaderWithEdit("Workflow") {
                        navController.navigate(AppRoutes.WORKFLOW_SELECTION)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ReviewRow("TDM Method", workflow.name.replace('_', '+'))
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (tdmInput != null) {
                    SectionCard {
                        SectionHeaderWithEdit("Input Values") {
                            navController.navigate(AppRoutes.DYNAMIC_INPUT)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        when (tdmInput) {
                            is TDMInput.Pre -> {
                                ReviewRow("Dose", "${tdmInput.doseMg} mg")
                                ReviewRow("Dosing Interval", "${tdmInput.intervalHr} hr")
                                ReviewRow("Infusion Duration", "${tdmInput.infusionDurationHr} hr")
                                ReviewRow("Pre-Level", "${tdmInput.preLevelConc} mg/L")
                            }
                            is TDMInput.Post -> {
                                ReviewRow("Dose", "${tdmInput.doseMg} mg")
                                ReviewRow("Dosing Interval", "${tdmInput.intervalHr} hr")
                                ReviewRow("Infusion Duration", "${tdmInput.infusionDurationHr} hr")
                                ReviewRow("Sampling Time", "${tdmInput.samplingTimeHr} hr")
                                ReviewRow("Post-Level", "${tdmInput.postLevelConc} mg/L")
                            }
                            is TDMInput.PrePost -> {
                                ReviewRow("Dose", "${tdmInput.doseMg} mg")
                                ReviewRow("Dosing Interval", "${tdmInput.intervalHr} hr")
                                ReviewRow("Infusion Duration", "${tdmInput.infusionDurationHr} hr")
                                ReviewRow("Infusion-to-Post Gap", "${tdmInput.infusionToPostGapHr} hr")
                                ReviewRow("Pre-to-Post Gap", "${tdmInput.preToPostGapHr} hr")
                                ReviewRow("Pre-Level", "${tdmInput.preLevelConc} mg/L")
                                ReviewRow("Post-Level", "${tdmInput.postLevelConc} mg/L")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                PrimaryAppButton(text = "Confirm & Calculate", modifier = Modifier.fillMaxWidth()) {
                    navController.navigate(AppRoutes.RESULTS)
                }
            } else {
                Text("Missing data — please go back and complete previous steps.")
            }
            Spacer(modifier = Modifier.height(20.dp))
            AppFooter()
        }
    }
}