package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppLogo
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

@Composable
fun HomeScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo(size = 72.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("TDM Insight", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Vancomycin Therapeutic Drug Monitoring", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
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
        PrimaryAppButton(
            text = "Create New Case",
            modifier = Modifier.fillMaxWidth()
        ) {
            caseViewModel.reset()
            navController.navigate(AppRoutes.PATIENT_INFO)
        }
    }
}