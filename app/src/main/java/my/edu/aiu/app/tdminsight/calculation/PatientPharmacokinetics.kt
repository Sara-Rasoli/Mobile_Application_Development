package my.edu.aiu.app.tdminsight.calculation

import my.edu.aiu.app.tdminsight.model.Gender
import my.edu.aiu.app.tdminsight.model.PatientInfo
import kotlin.math.max

data class PatientPharmacokineticValues(
    val idealBodyWeightKg: Double,
    val dosingWeightKg: Double,
    val creatinineClearanceMlMin: Double
)

object PatientPharmacokinetics {

    fun calculate(
        patient: PatientInfo
    ): PatientPharmacokineticValues {

        val ibw =
            calculateIdealBodyWeight(
                patient = patient
            )

        val dosingWeight =
            calculateDosingWeight(
                actualWeightKg =
                    patient.weightKg,
                idealWeightKg =
                    ibw
            )

        val creatinineClearance =
            calculateCreatinineClearance(
                patient = patient,
                dosingWeightKg =
                    dosingWeight
            )

        return PatientPharmacokineticValues(
            idealBodyWeightKg = ibw,
            dosingWeightKg = dosingWeight,
            creatinineClearanceMlMin =
                creatinineClearance
        )
    }

    // =============================================================
    // IDEAL BODY WEIGHT
    // =============================================================
    //
    // Devine equation:
    //
    // Male:
    // IBW = 50 + 2.3 × inches over 5 feet
    //
    // Female:
    // IBW = 45.5 + 2.3 × inches over 5 feet
    //
    // Height is entered by the application in centimetres.
    // =============================================================

    private fun calculateIdealBodyWeight(
        patient: PatientInfo
    ): Double {

        val heightInches =
            patient.heightCm / 2.54

        val baseHeightInches =
            60.0

        val inchesOverFiveFeet =
            max(
                0.0,
                heightInches -
                        baseHeightInches
            )

        val ibw =
            when (patient.gender) {

                Gender.MALE -> {
                    50.0 +
                            2.3 *
                            inchesOverFiveFeet
                }

                Gender.FEMALE -> {
                    45.5 +
                            2.3 *
                            inchesOverFiveFeet
                }

                Gender.OTHER -> {
                    // For an unspecified gender, use the
                    // midpoint of the male/female constants.
                    47.75 +
                            2.3 *
                            inchesOverFiveFeet
                }
            }

        return ibw.coerceAtLeast(0.0)
    }

    // =============================================================
    // DOSING WEIGHT
    // =============================================================
    //
    // Adjusted body weight:
    //
    // AdjBW = IBW + 0.4 × (TBW - IBW)
    //
    // If actual body weight is not above IBW, actual body
    // weight is retained.
    // =============================================================

    private fun calculateDosingWeight(
        actualWeightKg: Double,
        idealWeightKg: Double
    ): Double {

        if (
            actualWeightKg <= idealWeightKg
        ) {
            return actualWeightKg
                .coerceAtLeast(0.0)
        }

        return (
                idealWeightKg +
                        0.4 *
                        (
                                actualWeightKg -
                                        idealWeightKg
                                )
                )
            .coerceAtLeast(0.0)
    }

    // =============================================================
    // CREATININE CLEARANCE
    // =============================================================
    //
    // Cockcroft-Gault:
    //
    // CrCl =
    // ((140 - age) × weight) /
    // (72 × serum creatinine)
    //
    // Female:
    //
    // CrCl × 0.85
    //
    // Serum creatinine entered by the user is µmol/L.
    // Convert it to mg/dL:
    //
    // mg/dL = µmol/L / 88.4
    //
    // The dosing weight calculated above is used as the
    // body-weight input.
    // =============================================================

    private fun calculateCreatinineClearance(
        patient: PatientInfo,
        dosingWeightKg: Double
    ): Double {

        val serumCreatinineMgDl =
            patient.serumCreatinine /
                    88.4

        if (
            patient.ageYears <= 0 ||
            dosingWeightKg <= 0.0 ||
            serumCreatinineMgDl <= 0.0
        ) {
            return 0.0
        }

        var clearance =
            (
                    (
                            140.0 -
                                    patient.ageYears
                            ) *
                            dosingWeightKg
                    ) /
                    (
                            72.0 *
                                    serumCreatinineMgDl
                            )

        if (
            patient.gender ==
            Gender.FEMALE
        ) {
            clearance *= 0.85
        }

        return clearance
            .coerceAtLeast(0.0)
    }
}