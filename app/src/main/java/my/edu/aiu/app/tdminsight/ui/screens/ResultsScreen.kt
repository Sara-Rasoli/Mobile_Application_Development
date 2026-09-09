package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

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

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Results", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (result != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Elimination Rate (Ke): %.4f /hr".format(result.ke))
                    Text("Half-life: %.2f hr".format(result.halfLifeHr))
                    Text("Volume of Distribution (Vd): %.2f L".format(result.vd))
                    result.clearance?.let { Text("Clearance: %.2f L/hr".format(it)) }
                    result.expectedCmin?.let { Text("Trough (Cmin): %.2f mg/L".format(it)) }
                    result.expectedCmax?.let { Text("Peak (Cmax): %.2f mg/L".format(it)) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(AppRoutes.CALCULATION_EXPLANATION) }) {
                Text("View Explanation")
            }
        } else {
            Text("No input data found — please go back and complete previous steps.")
        }
    }
}