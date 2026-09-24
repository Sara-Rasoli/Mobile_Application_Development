package com.example.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * PLACEHOLDER — owned by Member 3 (Calculation Engine & Results, Section 6).
 *
 * Stub only, so the nav graph compiles today. Member 3: replace with the
 * real results display driven by a TDMResult, and call onViewExplanation
 * to go to CalculationExplanationScreen.
 */
@Composable
fun ResultsScreen(
    onViewExplanation: () -> Unit,
    onDone: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Results",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("TODO(Member 3): display TDMResult from TDMCalculationEngine here.")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onViewExplanation) {
                Text("How was this calculated?")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onDone) {
                Text("Done")
            }
        }
    }
}
