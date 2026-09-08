package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes

// PLACEHOLDER — Member 3 builds the real explanation view here.
@Composable
fun CalculationExplanationScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Calculation Explanation", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("TODO(Member 3): render List<CalculationStep> here.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack(route = AppRoutes.HOME, inclusive = false) }) {
            Text("Back to Home")
        }
    }
}