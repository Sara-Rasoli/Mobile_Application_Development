package com.example.tdminsight.model

/**
 * Holds the calculated output of a TDM workflow.
 * Filled in by Member 3's TDMCalculationEngine, displayed on ResultsScreen.
 *
 * Fields are nullable because not every workflow produces every value
 * (e.g. a Pre-only calculation will not have an observed-level-derived AUC24).
 * Member 3 should extend this once the lecturer-approved formulas are agreed.
 *
 * Shared file — tell the group before modifying (Section 12).
 */
data class TDMResult(
    val workflow: TDMWorkflow,
    val auc24: Double? = null,
    val clearance: Double? = null,
    val volumeOfDistribution: Double? = null,
    val halfLife: Double? = null,
    val recommendedDose: Double? = null,
    val steps: List<CalculationStep> = emptyList()
    // TODO(Member 3): extend with any additional lecturer-approved outputs
)
