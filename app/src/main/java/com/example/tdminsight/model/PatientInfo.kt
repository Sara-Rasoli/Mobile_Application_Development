package com.example.tdminsight.model

/**
 * Basic patient/case information collected on the Patient Information screen.
 * Shared model — do not duplicate (Section 8). Tell the group before editing (Section 12).
 *
 * NOTE: This is fictional-case data only (Section 1). This app is an academic
 * prototype and is NOT a clinically validated medical system.
 */
data class PatientInfo(
    val patientId: String,
    val age: Int,
    val weightKg: Double
)
