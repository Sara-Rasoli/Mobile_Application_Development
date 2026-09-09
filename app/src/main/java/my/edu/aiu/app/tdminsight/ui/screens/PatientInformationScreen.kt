package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun PatientInformationScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)

    var caseId by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var weightText by remember { mutableStateOf("") }
    var creatinineText by remember { mutableStateOf("") }
    var isPaediatric by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 1)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Patient Information", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            SectionCard {
                Text("Case", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = caseId,
                    onValueChange = { caseId = it },
                    label = { Text("Case ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            SectionCard {
                Text("Patient Information", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it },
                    label = { Text("Age (years)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = creatinineText,
                    onValueChange = { creatinineText = it },
                    label = { Text("Serum Creatinine (µmol/L)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Paediatric patient?")
                    Switch(checked = isPaediatric, onCheckedChange = { isPaediatric = it })
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SecondaryAppButton(text = "Back", modifier = Modifier.weight(1f)) { navController.popBackStack() }
                PrimaryAppButton(text = "Continue", modifier = Modifier.weight(1f)) {
                    val age = ageText.toIntOrNull()
                    val weight = weightText.toDoubleOrNull()
                    val creatinine = creatinineText.toDoubleOrNull()
                    when {
                        caseId.isBlank() -> errorMessage = "Case ID is required."
                        age == null -> errorMessage = "Age must be a whole number."
                        weight == null -> errorMessage = "Weight must be a number."
                        creatinine == null -> errorMessage = "Serum creatinine must be a number."
                        else -> {
                            errorMessage = null
                            caseViewModel.updatePatientInfo(
                                PatientInfo(
                                    caseId = caseId.trim(),
                                    weightKg = weight,
                                    ageYears = age,
                                    serumCreatinine = creatinine,
                                    isPaediatric = isPaediatric
                                )
                            )
                            navController.navigate(AppRoutes.WORKFLOW_SELECTION)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}