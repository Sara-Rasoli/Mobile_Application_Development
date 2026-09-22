package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppLogo
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

@Composable
fun HomeScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    var showInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo(size = 80.dp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "TDM Insight",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Therapeutic Drug Monitoring",
            style = MaterialTheme.typography.titleMedium,
            color = TealPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Guided Vancomycin TDM calculation with structured inputs, validation and explainable pharmacokinetic results.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        PrimaryAppButton(
            text = "Start New Case",
            modifier = Modifier.fillMaxWidth()
        ) {
            caseViewModel.reset()
            navController.navigate(AppRoutes.PATIENT_INFO)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryAppButton(
            text = "Learn More",
            modifier = Modifier.fillMaxWidth()
        ) {
            showInfoDialog = true
        }
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            "Academic Prototype — For Educational and Demonstration Purposes Only",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Use fictional demonstration data only",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) { Text("Close") }
            },
            title = { Text("About TDM Insight") },
            text = {
                Text(
                    "TDM Insight guides you through a fictional Vancomycin therapeutic drug monitoring case: entering patient details, choosing a sampling workflow, entering dosing and level data, and reviewing explainable pharmacokinetic calculations. This is an academic prototype for educational and demonstration purposes only, using fictional data only.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}