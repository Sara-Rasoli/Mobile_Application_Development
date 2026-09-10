package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PostInputForm
import my.edu.aiu.app.tdminsight.ui.components.PreInputForm
import my.edu.aiu.app.tdminsight.ui.components.PrePostInputForm
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.validation.TDMValidator
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun DynamicInputScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val workflow = caseViewModel.selectedWorkflow ?: TDMWorkflow.PRE

    var doseMg by remember { mutableStateOf("") }
    var intervalHr by remember { mutableStateOf("") }
    var infusionDurationHr by remember { mutableStateOf("") }
    var samplingTimeHr by remember { mutableStateOf("") }
    var infusionToPostGapHr by remember { mutableStateOf("") }
    var preToPostGapHr by remember { mutableStateOf("") }
    var preLevelConc by remember { mutableStateOf("") }
    var postLevelConc by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 3)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Enter Values (${workflow.name})", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            when (workflow) {
                TDMWorkflow.PRE -> PreInputForm(
                    doseMg = doseMg, onDoseMgChange = { doseMg = it },
                    intervalHr = intervalHr, onIntervalHrChange = { intervalHr = it },
                    infusionDurationHr = infusionDurationHr, onInfusionDurationHrChange = { infusionDurationHr = it },
                    preLevelConc = preLevelConc, onPreLevelConcChange = { preLevelConc = it },
                    errors = errors
                )
                TDMWorkflow.POST -> PostInputForm(
                    doseMg = doseMg, onDoseMgChange = { doseMg = it },
                    intervalHr = intervalHr, onIntervalHrChange = { intervalHr = it },
                    infusionDurationHr = infusionDurationHr, onInfusionDurationHrChange = { infusionDurationHr = it },
                    samplingTimeHr = samplingTimeHr, onSamplingTimeHrChange = { samplingTimeHr = it },
                    postLevelConc = postLevelConc, onPostLevelConcChange = { postLevelConc = it },
                    errors = errors
                )
                TDMWorkflow.PRE_POST -> PrePostInputForm(
                    doseMg = doseMg, onDoseMgChange = { doseMg = it },
                    intervalHr = intervalHr, onIntervalHrChange = { intervalHr = it },
                    infusionDurationHr = infusionDurationHr, onInfusionDurationHrChange = { infusionDurationHr = it },
                    infusionToPostGapHr = infusionToPostGapHr, onInfusionToPostGapHrChange = { infusionToPostGapHr = it },
                    preToPostGapHr = preToPostGapHr, onPreToPostGapHrChange = { preToPostGapHr = it },
                    preLevelConc = preLevelConc, onPreLevelConcChange = { preLevelConc = it },
                    postLevelConc = postLevelConc, onPostLevelConcChange = { postLevelConc = it },
                    errors = errors
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SecondaryAppButton(text = "Back", modifier = Modifier.weight(1f)) { navController.popBackStack() }
                PrimaryAppButton(text = "Next: Review", modifier = Modifier.weight(1f)) {
                    val result = when (workflow) {
                        TDMWorkflow.PRE -> TDMValidator.validatePre(doseMg, intervalHr, infusionDurationHr, preLevelConc)
                        TDMWorkflow.POST -> TDMValidator.validatePost(doseMg, intervalHr, infusionDurationHr, samplingTimeHr, postLevelConc)
                        TDMWorkflow.PRE_POST -> TDMValidator.validatePrePost(
                            doseMg, intervalHr, infusionDurationHr, infusionToPostGapHr, preToPostGapHr, preLevelConc, postLevelConc
                        )
                    }
                    errors = result.errors
                    if (result.input != null) {
                        caseViewModel.setTDMInput(result.input)
                        navController.navigate(AppRoutes.REVIEW)
                    }
                }
            }
        }
    }
}