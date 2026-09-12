package my.edu.aiu.app.tdminsight.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMResult
import my.edu.aiu.app.tdminsight.model.TDMWorkflow
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun generateSummaryPdf(
        context: Context,
        patientInfo: PatientInfo?,
        workflow: TDMWorkflow?,
        result: TDMResult?
    ): Uri {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true }
        val sectionPaint = Paint().apply { textSize = 14f; isFakeBoldText = true }
        val bodyPaint = Paint().apply { textSize = 12f }
        val smallPaint = Paint().apply { textSize = 10f; color = Color.GRAY }

        var y = 40f
        canvas.drawText("TDM Insight — Calculation Summary", 40f, y, titlePaint); y += 20f
        canvas.drawText("Academic prototype — fictional case only", 40f, y, smallPaint); y += 30f

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

        val exportDir = File(context.cacheDir, "pdf_exports")
        if (!exportDir.exists()) exportDir.mkdirs()
        val file = File(exportDir, "TDM_Insight_Summary.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()

        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
}