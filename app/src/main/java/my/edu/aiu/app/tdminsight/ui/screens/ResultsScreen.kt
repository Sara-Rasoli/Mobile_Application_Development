package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.ui.components.ClinicalPlausibilityCard
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

@Composable
fun ResultsScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val input = caseViewModel.tdmInput
    val patientInfo = caseViewModel.patientInfo

    LaunchedEffect(input, patientInfo) {
        if (input != null && caseViewModel.tdmResult == null) {
            val weightKg = patientInfo?.weightKg ?: 70.0
            val result = TDMCalculationEngine().calculate(input, weightKg)
            caseViewModel.updateTdmResult(result)
        }
    }

    val result = caseViewModel.tdmResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text("Results", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (result != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Elimination Rate (Ke): %.4f /hr".format(result.ke))
                    Text("Half-life: %.2f hr".format(result.halfLifeHr))
                    Text("Volume of Distribution (Vd): %.2f L".format(result.vd))
                    result.clearance?.let { Text("Clearance: %.2f L/hr".format(it)) }
                    result.aucTau?.let { Text("AUC (interval): %.2f mg·h/L".format(it)) }
                    result.auc24?.let { Text("AUC (24h): %.2f mg·h/L".format(it)) }
                    result.expectedCmin?.let { Text("Trough (Cmin): %.2f mg/L".format(it)) }
                    result.expectedCmax?.let { Text("Peak (Cmax): %.2f mg/L".format(it)) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            ClinicalPlausibilityCard(
                result = result,
                input = input,
                patientInfo = patientInfo
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryAppButton(
                    text = "Explore Dosing",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(AppRoutes.EXPLORE_DOSING)
                }
                PrimaryAppButton(
                    text = "View Explanation",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(AppRoutes.CALCULATION_EXPLANATION)
                }
            }
        } else {
            Text("No input data found — please go back and complete previous steps.")
        }
    }
}
