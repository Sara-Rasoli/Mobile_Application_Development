package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.edu.aiu.app.tdminsight.model.TDMResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    result: TDMResult,
    onNavigateToExplanation: () -> Unit,
    onNavigateHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TDM Results") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Pharmacokinetic Parameters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Elimination Rate (Ke): %.4f /hr".format(result.ke))
                    Text("Half-Life (t½): %.2f hr".format(result.halfLifeHr))
                    Text("Volume of Distribution (Vd): %.2f L".format(result.vd))
                    Text("Clearance (Cl): %.2f L/hr".format(result.clearance))

                    result.expectedCmax?.let { Text("Expected Peak (Cmax): %.2f mg/L".format(it)) }
                    result.expectedCmin?.let { Text("Expected Trough (Cmin): %.2f mg/L".format(it)) }
                }
            }

            Button(
                onClick = onNavigateToExplanation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Calculation Breakdown")
            }

            OutlinedButton(
                onClick = onNavigateHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Return to Home")
            }
        }
    }
}