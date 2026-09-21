package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun WorkflowSelectionScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val selectedWorkflow = caseViewModel.selectedWorkflow

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 2)
            Spacer(modifier = Modifier.height(16.dp))
            ScreenTitleRow(icon = Icons.Filled.Rule, title = "Select TDM Workflow")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Choose the calculation mode based on available blood sample data.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            val workflows = listOf(
                TDMWorkflow.PRE to Pair("Pre-Dose Trough Only", "Calculate pharmacokinetics using a single trough level taken before the next dose."),
                TDMWorkflow.POST to Pair("Post-Dose Peak Only", "Calculate pharmacokinetics using a peak concentration level taken after infusion."),
                TDMWorkflow.PRE_POST to Pair("Pre & Post Dose Levels", "Calculate precise elimination rate and volume of distribution using two measured serum levels.")
            )

            workflows.forEach { (workflow, info) ->
                val isSelected = selectedWorkflow == workflow
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { caseViewModel.setWorkflow(workflow) }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFF2B6B7C) else Color(0xFFE5E7EB),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFF0F7F9) else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = info.first,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF2B6B7C) else Color(0xFF1F2937)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = info.second, fontSize = 13.sp, color = Color(0xFF6B7280))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryAppButton(
                text = "Continue",
                modifier = Modifier.fillMaxWidth()
            ) {
                if (selectedWorkflow != null) {
                    navController.navigate(AppRoutes.DYNAMIC_INPUT)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryAppButton(text = "Back", modifier = Modifier.fillMaxWidth()) {
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.height(20.dp))
            AppFooter()
        }
    }
}