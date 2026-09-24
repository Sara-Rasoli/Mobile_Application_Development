package com.example.tdminsight.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tdminsight.model.PatientInfo
import com.example.tdminsight.model.TDMResult
import com.example.tdminsight.model.TDMWorkflow

/**
 * Holds the in-progress case as the user moves through the screen flow:
 * Create Case -> Patient Information -> Workflow Selection -> Dynamic Input
 * -> Review -> Results -> Calculation Explanation.
 *
 * This is scoped to the navigation graph (see AppNavigation.kt) so every
 * screen along that flow shares the SAME instance instead of each screen
 * getting its own. This is what lets Patient Information set data that the
 * Workflow Selection screen (and later, Member 2/3's screens) can read.
 *
 * Member 2 and Member 3: feel free to extend this with setters for the
 * dynamic-form input / calculation result once your screens are ready
 * (e.g. a `tdmInput` / `tdmResult` holder). Please flag it in the group chat
 * first since this file sits on the shared navigation path (Section 12).
 */
class CaseViewModel : ViewModel() {

    var patientInfo: PatientInfo? = null
        private set

    var selectedWorkflow: TDMWorkflow? = null
        private set

    // TODO(Member 2/3): add a holder for the built TDMInput and the TDMResult
    // once the Dynamic Input Form and Calculation Engine are wired up, e.g.:
    // var tdmResult: TDMResult? = null

    fun setPatientInfo(info: PatientInfo) {
        patientInfo = info
    }

    fun setWorkflow(workflow: TDMWorkflow) {
        selectedWorkflow = workflow
    }

    fun reset() {
        patientInfo = null
        selectedWorkflow = null
    }
}
