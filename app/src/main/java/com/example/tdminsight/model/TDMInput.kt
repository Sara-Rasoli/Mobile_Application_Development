package com.example.tdminsight.model

/**
 * TDMInput — the single agreed name for "the input to a TDM calculation"
 * (Section 8: we will NOT create TDMData / InputData / duplicate names).
 *
 * Per the group's navigation-design refinement: this is a sealed class with
 * one variant per workflow, instead of a single flat data class. This keeps
 * one consistent name (TDMInput) while letting each workflow carry different
 * clinical fields.
 *
 * Member 2 (Dynamic Forms & Validation) owns filling in the real clinical
 * fields for each variant, once the group agrees on the lecturer-approved
 * calculation spec (Section 7). Do NOT invent medical formulas or fields
 * ahead of that approval — the TODOs below are placeholders only.
 *
 * Member 3 (Calculation Engine) will pattern-match on the sealed subtype to
 * decide which calculator (Pre/Post/PrePost) to run.
 *
 * Shared file — tell the group before modifying (Section 12).
 */
sealed class TDMInput {
    abstract val patientInfo: PatientInfo
    abstract val workflow: TDMWorkflow

    data class Pre(
        override val patientInfo: PatientInfo
        // TODO(Member 2): add Pre-dose clinical fields once lecturer-approved
        // (e.g. dose, infusion time, dosing interval, relevant labs)
    ) : TDMInput() {
        override val workflow: TDMWorkflow = TDMWorkflow.PRE
    }

    data class Post(
        override val patientInfo: PatientInfo
        // TODO(Member 2): add Post-dose clinical fields once lecturer-approved
        // (e.g. measured trough/peak level, sample time)
    ) : TDMInput() {
        override val workflow: TDMWorkflow = TDMWorkflow.POST
    }

    data class PrePost(
        override val patientInfo: PatientInfo
        // TODO(Member 2): add combined Pre+Post clinical fields once
        // lecturer-approved
    ) : TDMInput() {
        override val workflow: TDMWorkflow = TDMWorkflow.PRE_POST
    }
}
