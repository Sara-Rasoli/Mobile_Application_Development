package my.edu.aiu.app.tdminsight.ui.screens

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMResult
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.CalculationExplorer
import my.edu.aiu.app.tdminsight.ui.components.ClinicalPlausibilityCard
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary
import my.edu.aiu.app.tdminsight.util.PdfExporter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val TDM_STEPS = listOf(
    "Home",
    "Patient",
    "Workflow",
    "Inputs",
    "Review",
    "Results",
    "Explanation"
)

private fun buildTextSummary(
    patientInfo: PatientInfo?,
    workflow: TDMWorkflow?,
    result: TDMResult?
): String {

    val builder = StringBuilder()

    val timestamp =
        SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date())

    builder.append(
        "TDM Insight — Calculation Summary\n"
    )

    builder.append(
        "Generated: $timestamp\n\n"
    )

    if (patientInfo != null) {

        builder.append(
            "Case & Patient:\n"
        )

        builder.append(
            "• Case ID: ${patientInfo.caseId}\n"
        )

        builder.append(
            "• Patient: ${patientInfo.name} " +
                    "(${patientInfo.gender.name.lowercase().replaceFirstChar { it.uppercase() }}, " +
                    "${patientInfo.ageYears}y)\n"
        )

        builder.append(
            "• Height: ${patientInfo.heightCm} cm | " +
                    "Weight: ${patientInfo.weightKg} kg\n"
        )

        builder.append(
            "• Serum Creatinine: ${patientInfo.serumCreatinine} µmol/L | " +
                    "Paediatric: ${if (patientInfo.isPaediatric) "Yes" else "No"}\n\n"
        )
    }

    if (workflow != null) {

        builder.append(
            "Workflow: ${workflow.name.replace('_', '+')}\n\n"
        )
    }

    if (result != null) {

        builder.append(
            "Pharmacokinetic Parameters:\n"
        )

        builder.append(
            "• Elimination Rate (Ke): %.4f /hr\n"
                .format(result.ke)
        )

        builder.append(
            "• Half-life (t½): %.2f hr\n"
                .format(result.halfLifeHr)
        )

        builder.append(
            "• Volume of Distribution (Vd): %.2f L\n"
                .format(result.vd)
        )

        result.clearance?.let {
            builder.append(
                "• Clearance (CL): %.2f L/hr\n"
                    .format(it)
            )
        }

        result.aucTau?.let {
            builder.append(
                "• AUC (interval): %.2f mg·h/L\n"
                    .format(it)
            )
        }

        result.auc24?.let {
            builder.append(
                "• AUC (24h): %.2f mg·h/L\n"
                    .format(it)
            )
        }

        result.micMgL?.let {
            builder.append(
                "• MIC: %.2f mg/L\n"
                    .format(it)
            )
        }

        result.aucMic?.let {
            builder.append(
                "• AUC/MIC: %.2f\n"
                    .format(it)
            )
        }

        result.expectedCmin?.let {
            builder.append(
                "• Expected Trough (Cmin): %.2f mg/L\n"
                    .format(it)
            )
        }

        result.expectedCmax?.let {
            builder.append(
                "• Expected Peak (Cmax): %.2f mg/L\n"
                    .format(it)
            )
        }

        if (result.steps.isNotEmpty()) {

            builder.append(
                "\nCalculation Steps:\n"
            )

            result.steps.forEach { step ->

                builder.append(
                    "• ${step.label}: ${step.value}\n"
                )

                if (step.note.isNotBlank()) {

                    builder.append(
                        "  (${step.note})\n"
                    )
                }
            }
        }

    } else {

        builder.append(
            "No calculation results available.\n"
        )
    }

    builder.append(
        "\nAcademic prototype — fictional case only. " +
                "Not a clinically validated system."
    )

    return builder.toString()
}

@Composable
fun CalculationExplanationScreen(
    navController: NavController
) {

    val caseViewModel =
        rememberSharedCaseViewModel(
            navController
        )

    val context =
        LocalContext.current

    val input =
        caseViewModel.tdmInput

    val patientInfo =
        caseViewModel.patientInfo

    val workflow =
        caseViewModel.selectedWorkflow

    val result =
        caseViewModel.tdmResult

    val steps =
        result?.steps ?: emptyList()

    // =========================================================
    // PDF PERMISSION
    // =========================================================

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                try {

                    val saved =
                        PdfExporter.saveToDownloads(
                            context,
                            patientInfo,
                            workflow,
                            result
                        )

                    Toast.makeText(
                        context,
                        if (saved) {
                            "Saved to Downloads/TDMInsight"
                        } else {
                            "Save failed"
                        },
                        Toast.LENGTH_LONG
                    ).show()

                } catch (e: Exception) {

                    Toast.makeText(
                        context,
                        "Export error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {

                Toast.makeText(
                    context,
                    "Storage permission needed to save",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        AppHeader(
            navController
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
        ) {

            // =================================================
            // PROGRESS
            // =================================================

            StepProgressBar(
                steps = TDM_STEPS,
                currentStepIndex = 6
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =================================================
            // TITLE
            // =================================================

            ScreenTitleRow(
                icon = Icons.Filled.MenuBook,
                title = "Calculation Explanation"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Explore how each pharmacokinetic parameter was obtained from the entered data.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // CLINICAL PLAUSIBILITY
            // =================================================

            if (result != null) {

                ClinicalPlausibilityCard(
                    result = result,
                    input = input,
                    patientInfo = patientInfo
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            // =================================================
            // CALCULATION EXPLORER
            // =================================================

            if (steps.isNotEmpty()) {

                SectionCard {

                    CalculationExplorer(
                        steps = steps,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            } else {

                SectionCard {

                    Text(
                        text = "No calculation data available",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Please complete a TDM calculation before viewing the explanation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // SHARE SUMMARY
            // =================================================

            SecondaryAppButton(
                text = "Share Summary",
                modifier = Modifier.fillMaxWidth()
            ) {

                try {

                    val summaryText =
                        buildTextSummary(
                            patientInfo,
                            workflow,
                            result
                        )

                    val shareIntent =
                        Intent(
                            Intent.ACTION_SEND
                        ).apply {

                            type = "text/plain"

                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "TDM Insight Summary - ${patientInfo?.caseId ?: "Case"}"
                            )

                            putExtra(
                                Intent.EXTRA_TEXT,
                                summaryText
                            )
                        }

                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "Share Summary"
                        )
                    )

                } catch (e: Exception) {

                    Toast.makeText(
                        context,
                        "Unable to share summary: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // =================================================
            // EXPORT PDF
            // =================================================

            SecondaryAppButton(
                text = "Export as PDF",
                modifier = Modifier.fillMaxWidth()
            ) {

                try {

                    if (
                        Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.Q
                    ) {

                        val saved =
                            PdfExporter.saveToDownloads(
                                context,
                                patientInfo,
                                workflow,
                                result
                            )

                        Toast.makeText(
                            context,
                            if (saved) {
                                "Saved to Downloads/TDMInsight"
                            } else {
                                "Save failed"
                            },
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        permissionLauncher.launch(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        context,
                        "Export PDF failed: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // =================================================
            // SHARE PDF
            // =================================================

            SecondaryAppButton(
                text = "Share PDF",
                modifier = Modifier.fillMaxWidth()
            ) {

                try {

                    val pdfUri =
                        PdfExporter.generateSummaryPdf(
                            context = context,
                            patientInfo = patientInfo,
                            workflow = workflow,
                            result = result
                        )

                    val pdfShareIntent =
                        Intent(
                            Intent.ACTION_SEND
                        ).apply {

                            type = "application/pdf"

                            putExtra(
                                Intent.EXTRA_STREAM,
                                pdfUri
                            )

                            addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        }

                    context.startActivity(
                        Intent.createChooser(
                            pdfShareIntent,
                            "Share PDF summary"
                        )
                    )

                } catch (e: Exception) {

                    Toast.makeText(
                        context,
                        "Unable to share PDF: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =================================================
            // BACK TO RESULTS
            // =================================================

            PrimaryAppButton(
                text = "Back to Results",
                modifier = Modifier.fillMaxWidth(),
                showIcon = false
            ) {

                navController.navigate(
                    AppRoutes.RESULTS
                ) {

                    popUpTo(
                        AppRoutes.RESULTS
                    ) {
                        inclusive = true
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // =================================================
            // BACK TO HOME
            // =================================================

            SecondaryAppButton(
                text = "Back to Home",
                modifier = Modifier.fillMaxWidth()
            ) {

                navController.popBackStack(
                    route = AppRoutes.HOME,
                    inclusive = false
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            AppFooter()
        }
    }
}