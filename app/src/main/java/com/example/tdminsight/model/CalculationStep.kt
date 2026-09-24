package com.example.tdminsight.model

/**
 * A single step in the input -> intermediate -> final calculation chain,
 * used to power the "How was this calculated?" / Calculation Explanation screen.
 */
data class CalculationStep(
    val title: String,
    val value: String
)
