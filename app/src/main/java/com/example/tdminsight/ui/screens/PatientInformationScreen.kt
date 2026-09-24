package com.example.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tdminsight.model.PatientInfo

/**
 * Patient Information screen (Section 2 flow, Member 1 scope).
 *
 * Note: this screen only does light "is it a well-formed number" UI-level
 * checking so it can build a PatientInfo object. Deep validation (range
 * checks, cross-field checks, mathematical error protection) is owned by
 * Member 2's TDMValidator per Section 5, and should be plugged in here or
 * on the Review screen — flag it in the group chat before changing this
 * file's public shape (Section 12).
 */
@Composable
fun PatientInformationScreen(
    onContinue: (PatientInfo) -> Unit,
    onBack: () -> Unit
) {
    var patientId by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var weightText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Patient Information",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = patientId,
                onValueChange = { patientId = it },
                label = { Text("Patient ID (fictional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it },
                label = { Text("Age (years)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weightText,
                onValueChange = { weightText = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val age = ageText.toIntOrNull()
                    val weight = weightText.toDoubleOrNull()
                    when {
                        patientId.isBlank() -> errorMessage = "Patient ID is required."
                        age == null -> errorMessage = "Age must be a whole number."
                        weight == null -> errorMessage = "Weight must be a number."
                        else -> {
                            errorMessage = null
                            onContinue(
                                PatientInfo(
                                    patientId = patientId.trim(),
                                    age = age,
                                    weightKg = weight
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}
