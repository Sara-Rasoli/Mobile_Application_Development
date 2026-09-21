package my.edu.aiu.app.tdminsight.ui.screens

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary
import my.edu.aiu.app.tdminsight.util.PdfExporter

private val TDM_STEPS = listOf(
    "Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation"
)

@Composable
fun CalculationExplanationScreen(navController: NavController) {

    val caseViewModel = rememberSharedCaseViewModel(navController)
    val steps = caseViewModel.tdmResult?.steps ?: emptyList()
    val context = LocalContext.current
    val patientInfo = caseViewModel.patientInfo
    val workflow = caseViewModel.selectedWorkflow
    val result = caseViewModel.tdmResult

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val saved = PdfExporter.saveToDownloads(context, patientInfo, workflow, result)
            Toast.makeText(
                context,
                if (saved) "Saved to Downloads/TDMInsight" else "Save failed",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(context, "Storage permission needed to save", Toast.LENGTH_LONG).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AppHeader(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {

            StepProgressBar(steps = TDM_STEPS, currentStepIndex = 6)
            Spacer(modifier = Modifier.height(12.dp))

            ScreenTitleRow(icon = Icons.Filled.MenuBook, title = "Calculation Explanation")
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Review how the pharmacokinetic parameters were calculated from the entered data.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (steps.isNotEmpty()) {
                steps.forEach { step ->
                    SectionCard {
                        Text(text = step.label, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = step.value, style = MaterialTheme.typography.bodyMedium)
                        if (step.note.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = step.note, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            } else {
                SectionCard {
                    Text(text = "No calculation data available", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please complete a TDM calculation before viewing the explanation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Share Summary button (text)
            Spacer(modifier = Modifier.height(10.dp))

            // Export as PDF button (share)
            SecondaryAppButton(
                text = "Share Summary",
                modifier = Modifier.fillMaxWidth()
            ) {
                val pdfUri = PdfExporter.generateSummaryPdf(
                    context = context,
                    patientInfo = patientInfo,
                    workflow = workflow,
                    result = result
                )
                val pdfShareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, pdfUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(pdfShareIntent, "Share PDF summary"))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Save PDF to Device button
            SecondaryAppButton(
                text = "Export as PDF",
                modifier = Modifier.fillMaxWidth()
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val saved = PdfExporter.saveToDownloads(context, patientInfo, workflow, result)
                    Toast.makeText(
                        context,
                        if (saved) "Saved to Downloads/TDMInsight" else "Save failed",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Back to Results button
            PrimaryAppButton(
                text = "Back to Results",
                modifier = Modifier.fillMaxWidth(),
                showIcon = false
            ) {
                navController.navigate(AppRoutes.RESULTS) {
                    popUpTo(AppRoutes.RESULTS) { inclusive = true }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Back to Home button
            SecondaryAppButton(
                text = "Back to Home",
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.popBackStack(route = AppRoutes.HOME, inclusive = false)
            }

            Spacer(modifier = Modifier.height(20.dp))
            AppFooter()
        }
    }
}