package com.example.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tdminsight.model.PatientInfo
import com.example.tdminsight.model.TDMWorkflow

/**
 * Review screen — shown after the Dynamic Input Form, before Calculate
 * (Section 2 flow). Lets the user confirm everything before the
 * TDMCalculationEngine (Member 3) runs.
 *
 * Currently reviews PatientInfo + TDMWorkflow, which is all Member 1 owns
 * end-to-end today. Once Member 2's Dynamic Input Form produces a real
 * TDMInput, extend this screen to also display the clinical fields — please
 * flag that change in the group chat first (Section 12), since this is a
 * shared-path screen.
 */
@Composable
fun ReviewScreen(
    patientInfo: PatientInfo,
    workflow: TDMWorkflow,
    onCalculate: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Review",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Patient ID: ${patientInfo.patientId}")
                    Text("Age: ${patientInfo.age}")
                    Text("Weight: ${patientInfo.weightKg} kg")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Workflow: ${workflow.name.replace('_', '+')}")

                    // TODO(Member 2/3): once TDMInput carries real clinical
                    // fields, display them here too.
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCalculate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
    }
}
