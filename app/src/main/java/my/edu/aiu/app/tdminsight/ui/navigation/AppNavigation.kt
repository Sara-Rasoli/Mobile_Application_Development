package my.edu.aiu.app.tdminsight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.edu.aiu.app.tdminsight.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.HOME) {
        composable(AppRoutes.HOME) { HomeScreen(navController) }
        composable(AppRoutes.PATIENT_INFO) { PatientInformationScreen(navController) }
        composable(AppRoutes.WORKFLOW_SELECTION) { WorkflowSelectionScreen(navController) }
        composable(AppRoutes.DYNAMIC_INPUT) { DynamicInputScreen(navController) }
        composable(AppRoutes.REVIEW) { ReviewScreen(navController) }
        composable(AppRoutes.RESULTS) { ResultsScreen(navController) }
        composable(AppRoutes.EXPLANATION) { CalculationExplanationScreen(navController) }
    }
}