package com.example.tdminsight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tdminsight.model.TDMWorkflow
import com.example.tdminsight.ui.screens.CalculationExplanationScreen
import com.example.tdminsight.ui.screens.CreateCaseScreen
import com.example.tdminsight.ui.screens.DynamicInputScreen
import com.example.tdminsight.ui.screens.HomeScreen
import com.example.tdminsight.ui.screens.PatientInformationScreen
import com.example.tdminsight.ui.screens.ResultsScreen
import com.example.tdminsight.ui.screens.ReviewScreen
import com.example.tdminsight.ui.screens.WorkflowSelectionScreen

/**
 * Wires up how the app moves between screens (Member 1, Section 4):
 * Home -> Create Case -> Patient Information -> Workflow Selection ->
 * Dynamic Input Form -> Review -> Calculate -> Results ->
 * Calculation Explanation.
 *
 * A single [CaseViewModel], scoped to this whole nav graph, is shared by
 * every screen along the flow so patient info / workflow entered early on
 * are still available later (e.g. on the Review screen).
 *
 * Member 2/3: when your real screens are ready, swap the DynamicInputScreen/
 * ResultsScreen/CalculationExplanationScreen calls below for your finished
 * versions — the routes and CaseViewModel plumbing should not need to change.
 * Please flag any change to this file in the group chat first (Section 12).
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = AppRoutes.HOME) {

        composable(AppRoutes.HOME) {
            val caseViewModel: CaseViewModel = viewModel()
            HomeScreen(
                onStartNewCase = {
                    caseViewModel.reset()
                    navController.navigate(AppRoutes.CREATE_CASE)
                }
            )
        }

        composable(AppRoutes.CREATE_CASE) {
            CreateCaseScreen(
                onContinue = { navController.navigate(AppRoutes.PATIENT_INFORMATION) },
                onCancel = { navController.popBackStack(AppRoutes.HOME, inclusive = false) }
            )
        }

        composable(AppRoutes.PATIENT_INFORMATION) { backStackEntry ->
            val caseViewModel: CaseViewModel = backStackEntry.sharedCaseViewModel(navController)
            PatientInformationScreen(
                onContinue = { patientInfo ->
                    caseViewModel.setPatientInfo(patientInfo)
                    navController.navigate(AppRoutes.WORKFLOW_SELECTION)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.WORKFLOW_SELECTION) { backStackEntry ->
            val caseViewModel: CaseViewModel = backStackEntry.sharedCaseViewModel(navController)
            WorkflowSelectionScreen(
                onWorkflowSelected = { workflow ->
                    caseViewModel.setWorkflow(workflow)
                    navController.navigate(AppRoutes.DYNAMIC_INPUT)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.DYNAMIC_INPUT) { backStackEntry ->
            val caseViewModel: CaseViewModel = backStackEntry.sharedCaseViewModel(navController)
            val workflow = caseViewModel.selectedWorkflow ?: TDMWorkflow.PRE
            DynamicInputScreen(
                workflow = workflow,
                onSubmit = { navController.navigate(AppRoutes.REVIEW) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.REVIEW) { backStackEntry ->
            val caseViewModel: CaseViewModel = backStackEntry.sharedCaseViewModel(navController)
            val patientInfo = caseViewModel.patientInfo
            val workflow = caseViewModel.selectedWorkflow
            if (patientInfo != null && workflow != null) {
                ReviewScreen(
                    patientInfo = patientInfo,
                    workflow = workflow,
                    onCalculate = { navController.navigate(AppRoutes.RESULTS) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Defensive fallback: something was skipped, send the user back to start.
                navController.popBackStack(AppRoutes.HOME, inclusive = false)
            }
        }

        composable(AppRoutes.RESULTS) {
            ResultsScreen(
                onViewExplanation = { navController.navigate(AppRoutes.CALCULATION_EXPLANATION) },
                onDone = { navController.popBackStack(AppRoutes.HOME, inclusive = false) }
            )
        }

        composable(AppRoutes.CALCULATION_EXPLANATION) {
            CalculationExplanationScreen(
                onBackToResults = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Returns a [CaseViewModel] scoped to the Create Case -> ... -> Results flow,
 * so every screen in that flow shares the same instance instead of each
 * getting its own. Falls back to a screen-scoped instance if, for any
 * reason, the parent entry can't be found (e.g. deep-linking directly into
 * a screen during development).
 */
@Composable
private fun androidx.navigation.NavBackStackEntry.sharedCaseViewModel(
    navController: NavHostController
): CaseViewModel {
    val parentEntry = remember(this) {
        navController.getBackStackEntry(AppRoutes.CREATE_CASE)
    }
    return viewModel(parentEntry)
}
