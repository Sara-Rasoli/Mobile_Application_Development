package my.edu.aiu.app.tdminsight.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TDMDatabaseHelper(
    context: Context
) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE $TABLE_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CASE_ID TEXT NOT NULL UNIQUE,
                $COLUMN_PATIENT_NAME TEXT NOT NULL,
                $COLUMN_WORKFLOW TEXT NOT NULL,
                $COLUMN_CREATED_AT INTEGER NOT NULL,

                $COLUMN_IBW REAL NOT NULL,
                $COLUMN_DOSING_WEIGHT REAL NOT NULL,
                $COLUMN_CREATININE_CLEARANCE REAL NOT NULL,

                $COLUMN_KE REAL NOT NULL,
                $COLUMN_HALF_LIFE REAL NOT NULL,
                $COLUMN_VD REAL NOT NULL,
                $COLUMN_CLEARANCE REAL,

                $COLUMN_AUC24 REAL,
                $COLUMN_MIC REAL,
                $COLUMN_AUC_MIC REAL,
                $COLUMN_CMAX REAL,
                $COLUMN_CMIN REAL,

                $COLUMN_DOSE REAL,
                $COLUMN_INTERVAL REAL,
                $COLUMN_INFUSION_DURATION REAL,
                $COLUMN_PRE_LEVEL REAL,
                $COLUMN_POST_LEVEL REAL,
                $COLUMN_SAMPLING_TIME REAL,
                $COLUMN_INFUSION_TO_POST_GAP REAL,
                $COLUMN_PRE_TO_POST_GAP REAL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL(
            "DROP TABLE IF EXISTS $TABLE_HISTORY"
        )

        onCreate(db)
    }

    companion object {

        private const val DATABASE_NAME =
            "tdm_insight.db"

        private const val DATABASE_VERSION =
            1

        const val TABLE_HISTORY =
            "calculation_history"

        const val COLUMN_ID =
            "_id"

        const val COLUMN_CASE_ID =
            "case_id"

        const val COLUMN_PATIENT_NAME =
            "patient_name"

        const val COLUMN_WORKFLOW =
            "workflow"

        const val COLUMN_CREATED_AT =
            "created_at"

        const val COLUMN_IBW =
            "ideal_body_weight"

        const val COLUMN_DOSING_WEIGHT =
            "dosing_weight"

        const val COLUMN_CREATININE_CLEARANCE =
            "creatinine_clearance"

        const val COLUMN_KE =
            "ke"

        const val COLUMN_HALF_LIFE =
            "half_life"

        const val COLUMN_VD =
            "vd"

        const val COLUMN_CLEARANCE =
            "clearance"

        const val COLUMN_AUC24 =
            "auc24"

        const val COLUMN_MIC =
            "mic"

        const val COLUMN_AUC_MIC =
            "auc_mic"

        const val COLUMN_CMAX =
            "cmax"

        const val COLUMN_CMIN =
            "cmin"

        const val COLUMN_DOSE =
            "dose"

        const val COLUMN_INTERVAL =
            "interval"

        const val COLUMN_INFUSION_DURATION =
            "infusion_duration"

        const val COLUMN_PRE_LEVEL =
            "pre_level"

        const val COLUMN_POST_LEVEL =
            "post_level"

        const val COLUMN_SAMPLING_TIME =
            "sampling_time"

        const val COLUMN_INFUSION_TO_POST_GAP =
            "infusion_to_post_gap"

        const val COLUMN_PRE_TO_POST_GAP =
            "pre_to_post_gap"
    }
}