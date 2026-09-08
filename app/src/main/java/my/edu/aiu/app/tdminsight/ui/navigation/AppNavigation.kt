package my.edu.aiu.app.tdminsight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.edu.aiu.app.tdminsight.ui.screens.CalculationExplanationScreen
import my.edu.aiu.app.tdminsight.ui.screens.DynamicInputScreen
import my.edu.aiu.app.tdminsight.ui.screens.HomeScreen
import my.edu.aiu.app.tdminsight.ui.screens.PatientInformationScreen
import my.edu.aiu.app.tdminsight.ui.screens.ResultsScreen
import my.edu.aiu.app.tdminsight.ui.screens.ReviewScreen
import my.edu.aiu.app.tdminsight.ui.screens.WorkflowSelectionScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = AppRoutes.HOME) {

        composable(AppRoutes.HOME) {
            HomeScreen(navController)
        }

        composable(AppRoutes.PATIENT_INFO) {
            PatientInformationScreen(navController)
        }

        composable(AppRoutes.WORKFLOW_SELECTION) {
            WorkflowSelectionScreen(navController)
        }

        composable(AppRoutes.DYNAMIC_INPUT) {
            DynamicInputScreen(navController)
        }

        composable(AppRoutes.REVIEW) {
            ReviewScreen(navController)
        }

        composable(AppRoutes.RESULTS) {
            ResultsScreen(navController)
        }

        composable(AppRoutes.CALCULATION_EXPLANATION) {
            CalculationExplanationScreen(navController)
        }
    }
}