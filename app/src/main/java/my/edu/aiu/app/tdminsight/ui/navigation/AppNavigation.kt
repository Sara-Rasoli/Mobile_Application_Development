package my.edu.aiu.app.tdminsight.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.edu.aiu.app.tdminsight.calculation.TDMCalculationEngine
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import my.edu.aiu.app.tdminsight.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val calculationEngine = remember { TDMCalculationEngine() }

    // Holds current TDM calculation input state across screens
    var currentInput by remember { mutableStateOf(TDMInput()) }
    // Holds the latest calculated result
    var currentResult by remember { mutableStateOf<TDMResult?>(null) }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        composable(AppRoutes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(AppRoutes.PATIENT_INFO) {
            PatientInformationScreen(navController = navController)
        }

        composable(AppRoutes.WORKFLOW_SELECTION) {
            WorkflowSelectionScreen(navController = navController)
        }

        composable(AppRoutes.DYNAMIC_INPUT) {
            DynamicInputScreen(navController = navController)
        }

        composable(AppRoutes.REVIEW) {
            ReviewScreen(navController = navController)
        }

        composable(AppRoutes.RESULTS) {
            // Renders Member 3 calculation output if available
            currentResult?.let { result ->
                ResultsScreen(
                    result = result,
                    onNavigateToExplanation = {
                        navController.navigate(AppRoutes.EXPLANATION)
                    },
                    onNavigateHome = {
                        currentInput = TDMInput()
                        currentResult = null
                        navController.popBackStack(AppRoutes.HOME, inclusive = false)
                    }
                )
            } ?: ResultsScreen(navController = navController) // Fallback to basic view if result isn't calculated yet
        }

        composable(AppRoutes.EXPLANATION) {
            currentResult?.let { result ->
                CalculationExplanationScreen(
                    result = result,
                    onNavigateBack = { navController.popBackStack() }
                )
            } ?: CalculationExplanationScreen(navController = navController)
        }
    }
}