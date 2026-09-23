package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

private val TDM_STEPS = listOf(
    "Home",
    "Patient",
    "Workflow",
    "Inputs",
    "Review",
    "Results",
    "Explanation"
)

@Composable
fun DynamicInputScreen(
    navController: NavController
) {
    val caseViewModel =
        rememberSharedCaseViewModel(navController)

    val workflow =
        caseViewModel.selectedWorkflow
            ?: TDMWorkflow.PRE

    val existing =
        caseViewModel.tdmInput

    var doseMg by remember {
        mutableStateOf(
            existing?.let {
                when (it) {
                    is TDMInput.Pre -> it.doseMg
                    is TDMInput.Post -> it.doseMg
                    is TDMInput.PrePost -> it.doseMg
                }
            }?.toString() ?: ""
        )
    }

    var intervalHr by remember {
        mutableStateOf(
            existing?.let {
                when (it) {
                    is TDMInput.Pre -> it.intervalHr
                    is TDMInput.Post -> it.intervalHr
                    is TDMInput.PrePost -> it.intervalHr
                }
            }?.toString() ?: ""
        )
    }

    var infusionDurationHr by remember {
        mutableStateOf(
            existing?.let {
                when (it) {
                    is TDMInput.Pre ->
                        it.infusionDurationHr

                    is TDMInput.Post ->
                        it.infusionDurationHr

                    is TDMInput.PrePost ->
                        it.infusionDurationHr
                }
            }?.toString() ?: ""
        )
    }

    var samplingTimeHr by remember {
        mutableStateOf(
            (existing as? TDMInput.Post)
                ?.samplingTimeHr
                ?.toString()
                ?: ""
        )
    }

    var infusionToPostGapHr by remember {
        mutableStateOf(
            (existing as? TDMInput.PrePost)
                ?.infusionToPostGapHr
                ?.toString()
                ?: ""
        )
    }

    var preToPostGapHr by remember {
        mutableStateOf(
            (existing as? TDMInput.PrePost)
                ?.preToPostGapHr
                ?.toString()
                ?: ""
        )
    }

    var preLevelConc by remember {
        mutableStateOf(
            existing?.let {
                when (it) {
                    is TDMInput.Pre ->
                        it.preLevelConc

                    is TDMInput.PrePost ->
                        it.preLevelConc

                    is TDMInput.Post ->
                        null
                }
            }?.toString() ?: ""
        )
    }

    var postLevelConc by remember {
        mutableStateOf(
            existing?.let {
                when (it) {
                    is TDMInput.Post ->
                        it.postLevelConc

                    is TDMInput.PrePost ->
                        it.postLevelConc

                    is TDMInput.Pre ->
                        null
                }
            }?.toString() ?: ""
        )
    }

    var micMgL by remember {
        mutableStateOf(
            existing?.let {
                when (it) {
                    is TDMInput.Pre ->
                        it.micMgL

                    is TDMInput.Post ->
                        it.micMgL

                    is TDMInput.PrePost ->
                        it.micMgL
                }
            }?.toString() ?: ""
        )
    }

    var errors by remember {
        mutableStateOf<Map<String, String>>(emptyMap())
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppHeader(navController)

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

            StepProgressBar(
                TDM_STEPS,
                currentStepIndex = 3
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ScreenTitleRow(
                icon = Icons.Filled.Edit,
                title = "Enter Values (${workflow.name})"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            when (workflow) {

                TDMWorkflow.PRE -> {

                    PreInputForm(
                        doseMg = doseMg,
                        onDoseMgChange = {
                            doseMg = it
                        },
                        intervalHr = intervalHr,
                        onIntervalHrChange = {
                            intervalHr = it
                        },
                        infusionDurationHr = infusionDurationHr,
                        onInfusionDurationHrChange = {
                            infusionDurationHr = it
                        },
                        preLevelConc = preLevelConc,
                        onPreLevelConcChange = {
                            preLevelConc = it
                        },
                        errors = errors
                    )
                }

                TDMWorkflow.POST -> {

                    PostInputForm(
                        doseMg = doseMg,
                        onDoseMgChange = {
                            doseMg = it
                        },
                        intervalHr = intervalHr,
                        onIntervalHrChange = {
                            intervalHr = it
                        },
                        infusionDurationHr = infusionDurationHr,
                        onInfusionDurationHrChange = {
                            infusionDurationHr = it
                        },
                        samplingTimeHr = samplingTimeHr,
                        onSamplingTimeHrChange = {
                            samplingTimeHr = it
                        },
                        postLevelConc = postLevelConc,
                        onPostLevelConcChange = {
                            postLevelConc = it
                        },
                        errors = errors
                    )
                }

                TDMWorkflow.PRE_POST -> {

                    PrePostInputForm(
                        doseMg = doseMg,
                        onDoseMgChange = {
                            doseMg = it
                        },
                        intervalHr = intervalHr,
                        onIntervalHrChange = {
                            intervalHr = it
                        },
                        infusionDurationHr = infusionDurationHr,
                        onInfusionDurationHrChange = {
                            infusionDurationHr = it
                        },
                        infusionToPostGapHr = infusionToPostGapHr,
                        onInfusionToPostGapHrChange = {
                            infusionToPostGapHr = it
                        },
                        preToPostGapHr = preToPostGapHr,
                        onPreToPostGapHrChange = {
                            preToPostGapHr = it
                        },
                        preLevelConc = preLevelConc,
                        onPreLevelConcChange = {
                            preLevelConc = it
                        },
                        postLevelConc = postLevelConc,
                        onPostLevelConcChange = {
                            postLevelConc = it
                        },
                        errors = errors
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =====================================================
            // MIC INPUT
            // =====================================================

            Text(
                text = "Pharmacodynamic Target"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = micMgL,
                onValueChange = {
                    micMgL = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("MIC")
                },
                placeholder = {
                    Text("e.g. 1.0")
                },
                suffix = {
                    Text("mg/L")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                isError = errors.containsKey("micMgL"),
                supportingText = {
                    errors["micMgL"]?.let {
                        Text(it)
                    } ?: Text(
                        "Minimum inhibitory concentration"
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                SecondaryAppButton(
                    text = "Back",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.popBackStack()
                }

                PrimaryAppButton(
                    text = "Next: Review",
                    modifier = Modifier.weight(1f)
                ) {

                    val validationResult =
                        when (workflow) {

                            TDMWorkflow.PRE ->
                                TDMValidator.validatePre(
                                    doseMg,
                                    intervalHr,
                                    infusionDurationHr,
                                    preLevelConc,
                                    micMgL
                                )

                            TDMWorkflow.POST ->
                                TDMValidator.validatePost(
                                    doseMg,
                                    intervalHr,
                                    infusionDurationHr,
                                    samplingTimeHr,
                                    postLevelConc,
                                    micMgL
                                )

                            TDMWorkflow.PRE_POST ->
                                TDMValidator.validatePrePost(
                                    doseMg,
                                    intervalHr,
                                    infusionDurationHr,
                                    infusionToPostGapHr,
                                    preToPostGapHr,
                                    preLevelConc,
                                    postLevelConc,
                                    micMgL
                                )
                        }

                    errors =
                        validationResult.errors

                    if (validationResult.input != null) {

                        caseViewModel.setTDMInput(
                            validationResult.input
                        )

                        navController.navigate(
                            AppRoutes.REVIEW
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            AppFooter()
        }
    }
}