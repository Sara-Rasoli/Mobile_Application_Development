package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes

@Composable
fun CalculationExplanationScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Calculation Explanation", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack(route = AppRoutes.HOME, inclusive = false) }) {
            Text("Back to Home")
        }
    }
}