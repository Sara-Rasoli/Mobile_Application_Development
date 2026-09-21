package my.edu.aiu.app.tdminsight.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMResult
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    private fun buildPdf(
        patientInfo: PatientInfo?,
        workflow: TDMWorkflow?,
        result: TDMResult?
    ): PdfDocument {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true }
        val sectionPaint = Paint().apply { textSize = 14f; isFakeBoldText = true }
        val bodyPaint = Paint().apply { textSize = 12f }
        val smallPaint = Paint().apply { textSize = 10f; color = Color.GRAY }

        var y = 40f
        val timestamp = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText("TDM Insight — Calculation Summary", 40f, y, titlePaint); y += 20f
        canvas.drawText("Generated: $timestamp", 40f, y, smallPaint); y += 14f
        canvas.drawText("Academic prototype — fictional case only. Not a clinically validated system.", 40f, y, smallPaint); y += 30f

        patientInfo?.let {
            canvas.drawText("Case ID: ${it.caseId}", 40f, y, bodyPaint); y += 18f
            canvas.drawText("Patient: ${it.name}, ${it.gender}, ${it.ageYears}y", 40f, y, bodyPaint); y += 18f
            canvas.drawText("Height: ${it.heightCm} cm   Weight: ${it.weightKg} kg", 40f, y, bodyPaint); y += 18f
            canvas.drawText(
                "Serum Creatinine: ${it.serumCreatinine} µmol/L   Paediatric: ${if (it.isPaediatric) "Yes" else "No"}",
                40f, y, bodyPaint
            ); y += 26f
        }
        workflow?.let {
            canvas.drawText("Workflow: ${it.name.replace('_', '+')}", 40f, y, bodyPaint); y += 26f
        }
        result?.let {
            canvas.drawText("Ke: %.4f /hr".format(it.ke), 40f, y, bodyPaint); y += 18f
            canvas.drawText("Half-life: %.2f hr".format(it.halfLifeHr), 40f, y, bodyPaint); y += 18f
            canvas.drawText("Vd: %.2f L".format(it.vd), 40f, y, bodyPaint); y += 18f
            it.clearance?.let { c -> canvas.drawText("Clearance: %.2f L/hr".format(c), 40f, y, bodyPaint); y += 18f }
            it.expectedCmin?.let { c -> canvas.drawText("Trough (Cmin): %.2f mg/L".format(c), 40f, y, bodyPaint); y += 18f }
            it.expectedCmax?.let { c -> canvas.drawText("Peak (Cmax): %.2f mg/L".format(c), 40f, y, bodyPaint); y += 18f }
            y += 14f

            if (it.steps.isNotEmpty()) {
                canvas.drawText("Calculation Steps:", 40f, y, sectionPaint); y += 20f
                it.steps.forEach { step ->
                    canvas.drawText("• ${step.label}: ${step.value}", 40f, y, bodyPaint); y += 16f
                    if (step.note.isNotBlank()) {
                        canvas.drawText("   ${step.note}", 40f, y, smallPaint); y += 14f
                    }
                }
            }
        }

        document.finishPage(page)
        return document
    }

    /** Saves the PDF into the phone's Downloads folder, visible in the Files app. Returns true on success. */
    fun saveToDownloads(
        context: Context,
        patientInfo: PatientInfo?,
        workflow: TDMWorkflow?,
        result: TDMResult?
    ): Boolean {
        val document = buildPdf(patientInfo, workflow, result)
        val fileName = "TDM_Insight_${System.currentTimeMillis()}.pdf"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+: use MediaStore, no special permission needed.
                val resolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/TDMInsight")
                }
                val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    resolver.openOutputStream(it)?.use { out: OutputStream -> document.writeTo(out) }
                } ?: return false
            } else {
                // Android 9 and below: write directly (requires WRITE_EXTERNAL_STORAGE permission, requested by the caller).
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appDir = File(downloadsDir, "TDMInsight")
                if (!appDir.exists()) appDir.mkdirs()
                val file = File(appDir, fileName)
                FileOutputStream(file).use { document.writeTo(it) }
            }
            document.close()
            true
        } catch (e: Exception) {
            document.close()
            false
        }
    }

    /** Generates a PDF into the app cache and returns a shareable content:// URI. */
    fun generateSummaryPdf(
        context: Context,
        patientInfo: PatientInfo?,
        workflow: TDMWorkflow?,
        result: TDMResult?
    ): Uri {
        val document = buildPdf(patientInfo, workflow, result)
        val exportDir = File(context.cacheDir, "pdf_exports")
        if (!exportDir.exists()) exportDir.mkdirs()
        val file = File(exportDir, "TDM_Insight_Summary.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
}