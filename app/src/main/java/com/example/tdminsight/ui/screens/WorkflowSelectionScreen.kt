package com.example.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tdminsight.model.TDMWorkflow

/**
 * Workflow Selection screen — the user picks one of the three required
 * Vancomycin TDM workflows (Section 1 & 2): Pre, Post, or Pre + Post.
 */
@Composable
fun WorkflowSelectionScreen(
    onWorkflowSelected: (TDMWorkflow) -> Unit,
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
                text = "Select TDM Method",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            WorkflowOption(
                title = "Pre",
                description = "Pre-dose (trough-based) workflow.",
                onClick = { onWorkflowSelected(TDMWorkflow.PRE) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            WorkflowOption(
                title = "Post",
                description = "Post-dose (peak/level-based) workflow.",
                onClick = { onWorkflowSelected(TDMWorkflow.POST) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            WorkflowOption(
                title = "Pre + Post",
                description = "Combined pre- and post-dose workflow.",
                onClick = { onWorkflowSelected(TDMWorkflow.PRE_POST) }
            )

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }
    }
}

@Composable
private fun WorkflowOption(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
