package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import my.edu.aiu.app.tdminsight.ui.components.PostInputForm
import my.edu.aiu.app.tdminsight.ui.components.PreInputForm
import my.edu.aiu.app.tdminsight.ui.components.PrePostInputForm
import my.edu.aiu.app.tdminsight.ui.navigation.AppRoutes
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.validation.TDMValidator

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

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
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

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
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
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Next: Review") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}