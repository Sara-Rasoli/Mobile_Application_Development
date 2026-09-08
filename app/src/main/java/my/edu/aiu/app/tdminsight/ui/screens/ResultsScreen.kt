package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes

// PLACEHOLDER — Member 3 builds the real results display here.
@Composable
fun ResultsScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Results", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("TODO(Member 3): display TDMResult from TDMCalculationEngine here.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(AppRoutes.CALCULATION_EXPLANATION) }) {
            Text("View Explanation")
        }
    }
}