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

    var currentInput by remember { mutableStateOf<TDMInput?>(null) }
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

        composable(AppRoutes.DYNAMIC_INPUT) {
            DynamicInputScreen(navController = navController)
        }

        composable(AppRoutes.REVIEW) {
            ReviewScreen(navController = navController)
        }

        composable(AppRoutes.RESULTS) {
            currentResult?.let { result ->
                ResultsScreen(
                    result = result,
                    onNavigateToExplanation = {
                        navController.navigate(AppRoutes.CALCULATION_EXPLANATION)
                    },
                    onNavigateHome = {
                        navController.popBackStack(AppRoutes.HOME, inclusive = false)
                    }
                )
            }
        }

        composable(AppRoutes.CALCULATION_EXPLANATION) {
            currentResult?.let { result ->
                CalculationExplanationScreen(
                    result = result,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}