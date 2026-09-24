package my.edu.aiu.app.tdminsight.data

import android.content.ContentValues
import android.content.Context
import my.edu.aiu.app.tdminsight.calculation.PatientPharmacokinetics
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import my.edu.aiu.app.tdminsight.model.TDMWorkflow

class TDMHistoryRepository(
    context: Context
) {

    private val databaseHelper =
        TDMDatabaseHelper(
            context.applicationContext
        )

    fun saveCase(
        patient: PatientInfo,
        workflow: TDMWorkflow,
        input: TDMInput,
        result: TDMResult
    ) {

        val pharmacokinetics =
            PatientPharmacokinetics.calculate(
                patient
            )

        val values =
            ContentValues()

        values.put(
            TDMDatabaseHelper.COLUMN_CASE_ID,
            patient.caseId
        )

        values.put(
            TDMDatabaseHelper.COLUMN_PATIENT_NAME,
            patient.name
        )

        values.put(
            TDMDatabaseHelper.COLUMN_WORKFLOW,
            workflow.name
        )

        values.put(
            TDMDatabaseHelper.COLUMN_CREATED_AT,
            System.currentTimeMillis()
        )

        values.put(
            TDMDatabaseHelper.COLUMN_IBW,
            pharmacokinetics.idealBodyWeightKg
        )

        values.put(
            TDMDatabaseHelper.COLUMN_DOSING_WEIGHT,
            pharmacokinetics.dosingWeightKg
        )

        values.put(
            TDMDatabaseHelper.COLUMN_CREATININE_CLEARANCE,
            pharmacokinetics.creatinineClearanceMlMin
        )

        values.put(
            TDMDatabaseHelper.COLUMN_KE,
            result.ke
        )

        values.put(
            TDMDatabaseHelper.COLUMN_HALF_LIFE,
            result.halfLifeHr
        )

        values.put(
            TDMDatabaseHelper.COLUMN_VD,
            result.vd
        )

        result.clearance?.let {
            values.put(
                TDMDatabaseHelper.COLUMN_CLEARANCE,
                it
            )
        }

        result.auc24?.let {
            values.put(
                TDMDatabaseHelper.COLUMN_AUC24,
                it
            )
        }

        result.micMgL?.let {
            values.put(
                TDMDatabaseHelper.COLUMN_MIC,
                it
            )
        }

        result.aucMic?.let {
            values.put(
                TDMDatabaseHelper.COLUMN_AUC_MIC,
                it
            )
        }

        result.expectedCmax?.let {
            values.put(
                TDMDatabaseHelper.COLUMN_CMAX,
                it
            )
        }

        result.expectedCmin?.let {
            values.put(
                TDMDatabaseHelper.COLUMN_CMIN,
                it
            )
        }

        when (input) {

            is TDMInput.Pre -> {

                values.put(
                    TDMDatabaseHelper.COLUMN_DOSE,
                    input.doseMg
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INTERVAL,
                    input.intervalHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INFUSION_DURATION,
                    input.infusionDurationHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_PRE_LEVEL,
                    input.preLevelConc
                )
            }

            is TDMInput.Post -> {

                values.put(
                    TDMDatabaseHelper.COLUMN_DOSE,
                    input.doseMg
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INTERVAL,
                    input.intervalHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INFUSION_DURATION,
                    input.infusionDurationHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_SAMPLING_TIME,
                    input.samplingTimeHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_POST_LEVEL,
                    input.postLevelConc
                )
            }

            is TDMInput.PrePost -> {

                values.put(
                    TDMDatabaseHelper.COLUMN_DOSE,
                    input.doseMg
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INTERVAL,
                    input.intervalHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INFUSION_DURATION,
                    input.infusionDurationHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_INFUSION_TO_POST_GAP,
                    input.infusionToPostGapHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_PRE_TO_POST_GAP,
                    input.preToPostGapHr
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_PRE_LEVEL,
                    input.preLevelConc
                )

                values.put(
                    TDMDatabaseHelper.COLUMN_POST_LEVEL,
                    input.postLevelConc
                )
            }
        }

        val db =
            databaseHelper.writableDatabase

        db.insertWithOnConflict(
            TDMDatabaseHelper.TABLE_HISTORY,
            null,
            values,
            android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        )

        db.close()
    }

    fun getAllCases(): List<CalculationHistoryRecord> {

        val records =
            mutableListOf<CalculationHistoryRecord>()

        val db =
            databaseHelper.readableDatabase

        val cursor =
            db.query(
                TDMDatabaseHelper.TABLE_HISTORY,
                null,
                null,
                null,
                null,
                null,
                "${TDMDatabaseHelper.COLUMN_CREATED_AT} DESC"
            )

        cursor.use {

            while (it.moveToNext()) {

                records.add(
                    CalculationHistoryRecord(

                        id =
                            it.getLong(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_ID
                                )
                            ),

                        caseId =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_CASE_ID
                                )
                            ),

                        patientName =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_PATIENT_NAME
                                )
                            ),

                        workflow =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_WORKFLOW
                                )
                            ),

                        createdAt =
                            it.getLong(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_CREATED_AT
                                )
                            ),

                        idealBodyWeightKg =
                            it.getDouble(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_IBW
                                )
                            ),

                        dosingWeightKg =
                            it.getDouble(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_DOSING_WEIGHT
                                )
                            ),

                        creatinineClearanceMlMin =
                            it.getDouble(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_CREATININE_CLEARANCE
                                )
                            ),

                        ke =
                            it.getDouble(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_KE
                                )
                            ),

                        halfLifeHr =
                            it.getDouble(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_HALF_LIFE
                                )
                            ),

                        vd =
                            it.getDouble(
                                it.getColumnIndexOrThrow(
                                    TDMDatabaseHelper.COLUMN_VD
                                )
                            ),

                        clearance =
                            getNullableDouble(
                                it,
                                TDMDatabaseHelper.COLUMN_CLEARANCE
                            ),

                        auc24 =
                            getNullableDouble(
                                it,
                                TDMDatabaseHelper.COLUMN_AUC24
                            ),

                        micMgL =
                            getNullableDouble(
                                it,
                                TDMDatabaseHelper.COLUMN_MIC
                            ),

                        aucMic =
                            getNullableDouble(
                                it,
                                TDMDatabaseHelper.COLUMN_AUC_MIC
                            ),

                        expectedCmax =
                            getNullableDouble(
                                it,
                                TDMDatabaseHelper.COLUMN_CMAX
                            ),

                        expectedCmin =
                            getNullableDouble(
                                it,
                                TDMDatabaseHelper.COLUMN_CMIN
                            )
                    )
                )
            }
        }

        db.close()

        return records
    }

    fun deleteCase(
        id: Long
    ) {

        val db =
            databaseHelper.writableDatabase

        db.delete(
            TDMDatabaseHelper.TABLE_HISTORY,
            "${TDMDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(
                id.toString()
            )
        )

        db.close()
    }

    fun deleteAllCases() {

        val db =
            databaseHelper.writableDatabase

        db.delete(
            TDMDatabaseHelper.TABLE_HISTORY,
            null,
            null
        )

        db.close()
    }

    private fun getNullableDouble(
        cursor: android.database.Cursor,
        columnName: String
    ): Double? {

        val index =
            cursor.getColumnIndexOrThrow(
                columnName
            )

        return if (
            cursor.isNull(index)
        ) {
            null
        } else {
            cursor.getDouble(index)
        }
    }
}