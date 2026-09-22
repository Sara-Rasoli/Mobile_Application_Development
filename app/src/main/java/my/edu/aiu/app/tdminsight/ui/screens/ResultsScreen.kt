package my.edu.aiu.app.tdminsight.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun ResultsScreen(navController: NavController) {
    val context = LocalContext.current
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val input = caseViewModel.tdmInput

    LaunchedEffect(input) {
        if (input != null && caseViewModel.tdmResult == null) {
            val result = TDMCalculationEngine().calculate(input)
            caseViewModel.updateTdmResult(result)
        }
    }

    val result = caseViewModel.tdmResult

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 5)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Results", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            if (result != null) {
                SectionCard {
                    Text("Elimination Rate (Ke): %.4f /hr".format(result.ke), style = MaterialTheme.typography.bodyLarge)
                    Text("Half-life: %.2f hr".format(result.halfLifeHr), style = MaterialTheme.typography.bodyLarge)
                    Text("Volume of Distribution (Vd): %.2f L".format(result.vd), style = MaterialTheme.typography.bodyLarge)
                    result.clearance?.let { Text("Clearance: %.2f L/hr".format(it), style = MaterialTheme.typography.bodyLarge) }
                    result.expectedCmin?.let { Text("Trough (Cmin): %.2f mg/L".format(it), style = MaterialTheme.typography.bodyLarge) }
                    result.expectedCmax?.let { Text("Peak (Cmax): %.2f mg/L".format(it), style = MaterialTheme.typography.bodyLarge) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                PrimaryAppButton(
                    text = "View Explanation",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(AppRoutes.CALCULATION_EXPLANATION) }
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                SectionCard {
                    Text("Export & Share Options", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Download the calculation metrics or share them via external healthcare communication apps.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryAppButton(
                            text = "Download PDF",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                Toast.makeText(context, "Downloading results as PDF document...", Toast.LENGTH_LONG).show()
                            }
                        )
                        SecondaryAppButton(
                            text = "Save Picture",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                Toast.makeText(context, "Saving results snapshot as Image...", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val shareText = buildString {
                        appendLine("TDM Insight - Fictional Vancomycin TDM Report")
                        appendLine("--------------------------------------------")
                        appendLine("Elimination Rate (Ke): %.4f /hr".format(result.ke))
                        appendLine("Half-life: %.2f hr".format(result.halfLifeHr))
                        appendLine("Volume of Distribution (Vd): %.2f L".format(result.vd))
                        result.clearance?.let { appendLine("Clearance: %.2f L/hr".format(it)) }
                        result.expectedCmin?.let { appendLine("Trough (Cmin): %.2f mg/L".format(it)) }
                        result.expectedCmax?.let { appendLine("Peak (Cmax): %.2f mg/L".format(it)) }
                        appendLine("--------------------------------------------")
                        appendLine("Academic Prototype - For Fictional Cases Only.")
                    }
                    
                    PrimaryAppButton(
                        text = "Share via System Sharesheet",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share TDM Results")
                            context.startActivity(shareIntent)
                        }
                    )
                }
            } else {
                Text("No input data found — please go back and complete previous steps.")
            }
        }
    }
}