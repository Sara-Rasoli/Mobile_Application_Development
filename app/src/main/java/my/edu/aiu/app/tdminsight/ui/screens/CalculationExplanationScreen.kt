package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.model.TDMResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationExplanationScreen(
    result: TDMResult,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculation Breakdown") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                ResultDetailCard(title = "Elimination Rate Constant (Ke)", value = "%.4f /hr".format(result.ke))
            }
            item {
                ResultDetailCard(title = "Half-Life (t 1/2)", value = "%.2f hr".format(result.halfLifeHr))
            }
            item {
                ResultDetailCard(title = "Volume of Distribution (Vd)", value = "%.2f L".format(result.vd))
            }
            item {
                ResultDetailCard(title = "Clearance (Cl)", value = "%.2f L/hr".format(result.clearance))
            }
            result.expectedCmax?.let { cmax ->
                item { ResultDetailCard(title = "Expected Peak (Cmax)", value = "%.2f mg/L".format(cmax)) }
            }
            result.expectedCmin?.let { cmin ->
                item { ResultDetailCard(title = "Expected Trough (Cmin)", value = "%.2f mg/L".format(cmin)) }
            }
        }
    }
}

@Composable
private fun ResultDetailCard(title: String, value: String) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}