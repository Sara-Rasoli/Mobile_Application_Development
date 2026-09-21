package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.theme.BorderLight
import my.edu.aiu.app.tdminsight.ui.theme.CardWhite
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

@Composable
fun AppHeader(navController: NavController) {
    var showInfoDialog by remember { mutableStateOf(false) }

    Surface(color = CardWhite, tonalElevation = 0.dp) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppLogo(size = 36.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("TDM Insight", style = MaterialTheme.typography.titleMedium)
                        Text("Vancomycin TDM", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        navController.popBackStack(route = AppRoutes.HOME, inclusive = false)
                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = TextSecondary)
                    }
                    IconButton(onClick = {
                        navController.navigate(AppRoutes.CALCULATION_HISTORY)
                    }) {
                        Icon(Icons.Filled.History, contentDescription = "History", tint = TextSecondary)
                    }
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "About", tint = TextSecondary)
                    }
                }
            }
            HorizontalDivider(color = BorderLight, thickness = 1.dp)
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) { Text("Close") }
            },
            title = { Text("About TDM Insight") },
            text = {
                Column {
                    Text(
                        "Academic Prototype — For Educational and Demonstration Purposes Only.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Use fictional demonstration data only. This tool is not a clinically validated medical system and must not be used for real patient care.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        )
    }
}