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
 * PLACEHOLDER — owned by Member 3 (Section 6).
 * Stub only, so the nav graph compiles today. Replace with a list of
 * CalculationStep items showing the input -> intermediate -> final chain.
 */
@Composable
fun CalculationExplanationScreen(
    onBackToResults: () -> Unit
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
                text = "Calculation Explanation",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("TODO(Member 3): render List<CalculationStep> here.")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onBackToResults) {
                Text("Back to Results")
            }
        }
    }
}
