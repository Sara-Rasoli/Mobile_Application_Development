package com.example.tdminsight.ui.navigation

/**
 * Route name constants only — kept separate from AppNavigation.kt so this
 * file can exist before all 7+ screens are built (per the group's revision
 * notes: AppRoutes.kt holds route names, AppNavigation.kt holds the graph).
 *
 * Order follows Section 2 of the Group Coding Agreement:
 * Home -> Create Case -> Patient Information -> Workflow Selection ->
 * Dynamic Input -> Review -> Results -> Calculation Explanation.
 */
object AppRoutes {
    const val HOME = "home"
    const val CREATE_CASE = "create_case"
    const val PATIENT_INFORMATION = "patient_information"
    const val WORKFLOW_SELECTION = "workflow_selection"
    const val DYNAMIC_INPUT = "dynamic_input"       // Member 2
    const val REVIEW = "review"
    const val RESULTS = "results"                    // Member 3
    const val CALCULATION_EXPLANATION = "calculation_explanation" // Member 3

    // Optional, per Section 16 — only after core flow is done
    const val CALCULATION_HISTORY = "calculation_history"
}
