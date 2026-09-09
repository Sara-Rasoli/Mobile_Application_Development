package my.edu.aiu.app.tdminsight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import my.edu.aiu.app.tdminsight.model.TDMWorkflow

class CaseViewModel : ViewModel() {
    var patientInfo: PatientInfo? by mutableStateOf<PatientInfo?>(null)
        private set

    var selectedWorkflow: TDMWorkflow? by mutableStateOf<TDMWorkflow?>(null)
        private set

    var tdmInput: TDMInput? by mutableStateOf<TDMInput?>(null)
        private set

    var tdmResult: TDMResult? by mutableStateOf<TDMResult?>(null)
        private set

    fun updatePatientInfo(info: PatientInfo) { patientInfo = info }
    fun setWorkflow(workflow: TDMWorkflow) { selectedWorkflow = workflow }
    fun setTDMInput(input: TDMInput) { tdmInput = input }
    fun updateTdmResult(result: TDMResult) { tdmResult = result }

    fun reset() {
        patientInfo = null
        selectedWorkflow = null
        tdmInput = null
        tdmResult = null
    }
}

@Composable
fun rememberSharedCaseViewModel(navController: NavController): CaseViewModel {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(AppRoutes.HOME)
    }
    return viewModel(parentEntry)
}