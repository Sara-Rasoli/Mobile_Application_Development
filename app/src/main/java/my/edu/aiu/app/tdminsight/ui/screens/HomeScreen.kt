package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("TDM Insight", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Vancomycin Therapeutic Drug Monitoring", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "You're about to start a new fictional Vancomycin TDM case. Next, you'll enter basic patient information.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Academic prototype — fictional cases only. Not a clinically validated medical system.",
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            caseViewModel.reset()
            navController.navigate(AppRoutes.PATIENT_INFO)
        }) {
            Text("Create New Case")
        }
    }
}