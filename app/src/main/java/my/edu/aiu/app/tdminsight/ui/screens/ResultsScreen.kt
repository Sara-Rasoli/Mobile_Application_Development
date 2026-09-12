package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.components.AppFooter

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ResultsScreen(navController: NavController) {
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
        AppHeader(navController)
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 5)
            Spacer(modifier = Modifier.height(12.dp))
            ScreenTitleRow(icon = Icons.Filled.BarChart, title = "Results")
            Spacer(modifier = Modifier.height(16.dp))

            if (result != null) {
                SectionCard {
                    Text("Calculated Parameters", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ResultRow("Elimination Rate (Ke)", "%.4f /hr".format(result.ke))
                    ResultRow("Half-life", "%.2f hr".format(result.halfLifeHr))
                    ResultRow("Volume of Distribution (Vd)", "%.2f L".format(result.vd))
                    result.clearance?.let { ResultRow("Clearance", "%.2f L/hr".format(it)) }
                    result.expectedCmin?.let { ResultRow("Trough (Cmin)", "%.2f mg/L".format(it)) }
                    result.expectedCmax?.let { ResultRow("Peak (Cmax)", "%.2f mg/L".format(it)) }
                    result.auc24?.let { ResultRow("AUC24", "%.2f mg·hr/L".format(it)) }
                    result.newSuggestedDoseMg?.let { ResultRow("Suggested Dose", "%.0f mg".format(it)) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryAppButton(text = "View Explanation", modifier = Modifier.fillMaxWidth()) {
                    navController.navigate(AppRoutes.CALCULATION_EXPLANATION)
                }
            } else {
                Text("No input data found — please go back and complete previous steps.")
            }
            Spacer(modifier = Modifier.height(20.dp))
            AppFooter()
        }
    }
}