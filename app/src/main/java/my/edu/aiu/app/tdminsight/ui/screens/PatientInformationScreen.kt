package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.Gender
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.LabeledInputField
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.BorderLight
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

private fun Gender.displayLabel(): String = when (this) {
    Gender.MALE -> "Male"
    Gender.FEMALE -> "Female"
    Gender.OTHER -> "Unspecified"
}

@Composable
fun PatientInformationScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)

    var caseId by remember { mutableStateOf("") }
    var patientName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var ageText by remember { mutableStateOf("") }
    var heightText by remember { mutableStateOf("") }
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Enter the patient parameters required for the calculation.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            SectionCard {
                Text("Case", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                LabeledInputField(
                    label = "Case ID",
                    value = caseId,
                    onValueChange = { caseId = it },
                    helperText = "Unique identifier for this case.",
                    required = true
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            SectionCard {
                Text("Patient Information", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                LabeledInputField(
                    label = "Patient Name",
                    value = patientName,
                    onValueChange = { patientName = it },
                    helperText = "Fictional patient name or label.",
                    required = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Gender", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Gender.entries.forEach { genderOption ->
                    val isSelected = selectedGender == genderOption
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .selectable(selected = isSelected, onClick = { selectedGender = genderOption })
                            .then(
                                Modifier
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            border = BorderStroke(1.dp, if (isSelected) TealPrimary else BorderLight),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = isSelected, onClick = { selectedGender = genderOption })
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(genderOption.displayLabel(), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                LabeledInputField(
                    label = "Age",
                    value = ageText,
                    onValueChange = { ageText = it },
                    unit = "years",
                    helperText = "Patient age.",
                    required = true,
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(16.dp))
                LabeledInputField(
                    label = "Height",
                    value = heightText,
                    onValueChange = { heightText = it },
                    unit = "cm",
                    helperText = "Patient height.",
                    required = true,
                    keyboardType = KeyboardType.Decimal
                )
                Spacer(modifier = Modifier.height(16.dp))
                LabeledInputField(
                    label = "Weight",
                    value = weightText,
                    onValueChange = { weightText = it },
                    unit = "kg",
                    helperText = "Patient body weight (total body weight).",
                    required = true,
                    keyboardType = KeyboardType.Decimal
                )
                Spacer(modifier = Modifier.height(16.dp))
                LabeledInputField(
                    label = "Serum Creatinine",
                    value = creatinineText,
                    onValueChange = { creatinineText = it },
                    unit = "µmol/L",
                    helperText = "Used to estimate renal function.",
                    required = true,
                    keyboardType = KeyboardType.Decimal
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Paediatric patient?", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Adjusts the calculation method for paediatric patients.",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    Switch(checked = isPaediatric, onCheckedChange = { isPaediatric = it })
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(20.dp))
            PrimaryAppButton(text = "Continue", modifier = Modifier.fillMaxWidth()) {
                val age = ageText.toIntOrNull()
                val height = heightText.toDoubleOrNull()
                val weight = weightText.toDoubleOrNull()
                val creatinine = creatinineText.toDoubleOrNull()
                when {
                    caseId.isBlank() -> errorMessage = "Case ID is required."
                    patientName.isBlank() -> errorMessage = "Patient name is required."
                    selectedGender == null -> errorMessage = "Please select a gender."
                    age == null -> errorMessage = "Age must be a whole number."
                    height == null -> errorMessage = "Height must be a number."
                    weight == null -> errorMessage = "Weight must be a number."
                    creatinine == null -> errorMessage = "Serum creatinine must be a number."
                    else -> {
                        errorMessage = null
                        caseViewModel.updatePatientInfo(
                            PatientInfo(
                                caseId = caseId.trim(),
                                name = patientName.trim(),
                                gender = selectedGender!!,
                                heightCm = height,
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
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryAppButton(text = "Back", modifier = Modifier.fillMaxWidth()) {
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}