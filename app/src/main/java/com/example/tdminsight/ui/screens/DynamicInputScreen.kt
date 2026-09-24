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
import com.example.tdminsight.model.TDMWorkflow

/**
 * PLACEHOLDER — owned by Member 2 (Dynamic Forms & Validation, Section 5).
 *
 * This stub only exists so the navigation graph compiles end-to-end today.
 * Member 2: replace the body with PreInputForm / PostInputForm /
 * PrePostInputForm (from ui/components/) based on [workflow], run
 * TDMValidator on submit, and call [onSubmit] with the resulting TDMInput.
 * Please don't change the function signature without flagging it in the
 * group chat, since AppNavigation.kt wires it (Section 12).
 */
@Composable
fun DynamicInputScreen(
    workflow: TDMWorkflow,
    onSubmit: () -> Unit,
    onBack: () -> Unit
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
                text = "Dynamic Input Form (${workflow.name})",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("TODO(Member 2): build the real input form + validation here.")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onSubmit) {
                Text("Continue (placeholder)")
            }
        }
    }
}
