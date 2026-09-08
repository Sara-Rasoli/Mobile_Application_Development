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
                title = { Text("TDM Results — ${result.workflow.name}") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "24-Hour AUC Target Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${String.format("%.1f", result.auc24MgHourPerL)} mg·h/L",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Target therapeutic window: 400–600 mg·h/L",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Text(
                text = "Pharmacokinetic Parameters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // PK Parameter Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ParameterCard(
                    title = "Clearance (CL)",
                    value = "${String.format("%.2f", result.clearanceLitersPerHour)} L/h",
                    modifier = Modifier.weight(1f)
                )
                ParameterCard(
                    title = "Half-Life (t½)",
                    value = "${String.format("%.2f", result.halfLifeHours)} h",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ParameterCard(
                    title = "Est. Peak Concentration",
                    value = result.estimatedPeakMgL?.let { "${String.format("%.1f", it)} mg/L" } ?: "N/A",
                    modifier = Modifier.weight(1f)
                )
                ParameterCard(
                    title = "Est. Trough Concentration",
                    value = result.estimatedTroughMgL?.let { "${String.format("%.1f", it)} mg/L" } ?: "N/A",
                    modifier = Modifier.weight(1f)
                )
            }

            result.estimatedCrClMlMin?.let { crCl ->
                ParameterCard(
                    title = "Creatinine Clearance (CrCl)",
                    value = "${String.format("%.1f", crCl)} mL/min",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Button(
                onClick = onNavigateToExplanation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Step-by-Step Explanation")
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

@Composable
private fun ParameterCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}