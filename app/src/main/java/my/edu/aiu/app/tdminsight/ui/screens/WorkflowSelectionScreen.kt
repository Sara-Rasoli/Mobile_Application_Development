package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorkflowSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select Vancomycin Workflow", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(AppRoutes.DYNAMIC_INPUT) }) {
            Text("Pre")
        }
        Button(onClick = { navController.navigate(AppRoutes.DYNAMIC_INPUT) }) {
            Text("Post")
        }
        Button(onClick = { navController.navigate(AppRoutes.DYNAMIC_INPUT) }) {
            Text("Pre + Post")
        }
    }
}