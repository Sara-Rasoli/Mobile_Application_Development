package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.PostInputForm
import my.edu.aiu.app.tdminsight.ui.components.PreInputForm
import my.edu.aiu.app.tdminsight.ui.components.PrePostInputForm
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.StepProgressBar
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.validation.TDMValidator
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable

private val TDM_STEPS = listOf("Home", "Patient", "Workflow", "Inputs", "Review", "Results", "Explanation")

@Composable
fun DynamicInputScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val workflow = caseViewModel.selectedWorkflow ?: TDMWorkflow.PRE
    val existing = caseViewModel.tdmInput

    var doseMg by remember { mutableStateOf(existing?.let { (it as? TDMInput.Pre)?.doseMg ?: (it as? TDMInput.Post)?.doseMg ?: (it as? TDMInput.PrePost)?.doseMg }?.toString() ?: "") }
    var intervalHr by remember { mutableStateOf(existing?.let { (it as? TDMInput.Pre)?.intervalHr ?: (it as? TDMInput.Post)?.intervalHr ?: (it as? TDMInput.PrePost)?.intervalHr }?.toString() ?: "") }
    var infusionDurationHr by remember { mutableStateOf(existing?.let { (it as? TDMInput.Pre)?.infusionDurationHr ?: (it as? TDMInput.Post)?.infusionDurationHr ?: (it as? TDMInput.PrePost)?.infusionDurationHr }?.toString() ?: "") }
    var samplingTimeHr by remember { mutableStateOf((existing as? TDMInput.Post)?.samplingTimeHr?.toString() ?: "") }
    var infusionToPostGapHr by remember { mutableStateOf((existing as? TDMInput.PrePost)?.infusionToPostGapHr?.toString() ?: "") }
    var preToPostGapHr by remember { mutableStateOf((existing as? TDMInput.PrePost)?.preToPostGapHr?.toString() ?: "") }
    var preLevelConc by remember { mutableStateOf(existing?.let { (it as? TDMInput.Pre)?.preLevelConc ?: (it as? TDMInput.PrePost)?.preLevelConc }?.toString() ?: "") }
    var postLevelConc by remember { mutableStateOf(existing?.let { (it as? TDMInput.Post)?.postLevelConc ?: (it as? TDMInput.PrePost)?.postLevelConc }?.toString() ?: "") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(navController)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
            StepProgressBar(TDM_STEPS, currentStepIndex = 3)
            Spacer(modifier = Modifier.height(12.dp))
            ScreenTitleRow(icon = Icons.Filled.Edit, title = "Enter Values (${workflow.name})")
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
            Spacer(modifier = Modifier.height(20.dp))
            AppFooter()
        }
    }
}