package my.edu.aiu.app.tdminsight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMWorkflow

class CaseViewModel : ViewModel() {
    var patientInfo: PatientInfo? = null
        private set

    var selectedWorkflow: TDMWorkflow? = null
        private set

    var tdmInput: TDMInput? = null
        private set

    fun setPatientInfo(info: PatientInfo) { patientInfo = info }
    fun setWorkflow(workflow: TDMWorkflow) { selectedWorkflow = workflow }
    fun setTDMInput(input: TDMInput) { tdmInput = input }
    fun reset() {
        patientInfo = null
        selectedWorkflow = null
        tdmInput = null
    }
}

@Composable
fun rememberSharedCaseViewModel(navController: NavController): CaseViewModel {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(AppRoutes.HOME)
    }
    return viewModel(parentEntry)
}