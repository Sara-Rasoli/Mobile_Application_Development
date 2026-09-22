package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.edu.aiu.app.tdminsight.calculation.CurvePoint

@Composable
fun ConcentrationChart(
    points: List<CurvePoint>,
    cmaxSs: Double,
    cminSs: Double,
    maxTimeHr: Double,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurfaceVariant
    val lineTextColor = MaterialTheme.colorScheme.onSurface
    val cmaxLineColor = MaterialTheme.colorScheme.error
    val cminLineColor = MaterialTheme.colorScheme.tertiary

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        val width = size.width
        val height = size.height

        val paddingLeft = 50.dp.toPx()
        val paddingRight = 20.dp.toPx()
        val paddingTop = 25.dp.toPx()
        val paddingBottom = 35.dp.toPx()

        val graphWidth = width - paddingLeft - paddingRight
        val graphHeight = height - paddingTop - paddingBottom

        if (points.isEmpty() || graphWidth <= 0 || graphHeight <= 0) return@Canvas

        val maxY = maxOf(cmaxSs * 1.25, 1.0)

        fun xToPx(timeHr: Double): Float {
            return paddingLeft + ((timeHr / maxTimeHr) * graphWidth).toFloat()
        }

        fun yToPx(concMgL: Double): Float {
            val ratio = (concMgL / maxY).coerceIn(0.0, 1.5)
            return paddingTop + graphHeight - (ratio * graphHeight).toFloat()
        }

        // Draw Axes
        drawLine(
            color = axisColor,
            start = Offset(paddingLeft, paddingTop),
            end = Offset(paddingLeft, height - paddingBottom),
            strokeWidth = 2f
        )
        drawLine(
            color = axisColor,
            start = Offset(paddingLeft, height - paddingBottom),
            end = Offset(width - paddingRight, height - paddingBottom),
            strokeWidth = 2f
        )

        // Draw Cmax,ss dashed line
        val cmaxYPx = yToPx(cmaxSs)
        drawLine(
            color = cmaxLineColor,
            start = Offset(paddingLeft, cmaxYPx),
            end = Offset(width - paddingRight, cmaxYPx),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        val cmaxLayout = textMeasurer.measure(
            text = "Cmax: %.1f mg/L".format(cmaxSs),
            style = TextStyle(fontSize = 11.sp, color = cmaxLineColor, fontWeight = FontWeight.Bold)
        )
        drawText(cmaxLayout, topLeft = Offset(paddingLeft + 8f, cmaxYPx - cmaxLayout.size.height - 2f))

        // Draw Cmin,ss dashed line
        val cminYPx = yToPx(cminSs)
        drawLine(
            color = cminLineColor,
            start = Offset(paddingLeft, cminYPx),
            end = Offset(width - paddingRight, cminYPx),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        val cminLayout = textMeasurer.measure(
            text = "Cmin: %.1f mg/L".format(cminSs),
            style = TextStyle(fontSize = 11.sp, color = cminLineColor, fontWeight = FontWeight.Bold)
        )
        drawText(cminLayout, topLeft = Offset(paddingLeft + 8f, cminYPx - cminLayout.size.height - 2f))

        // Draw Concentration Curve Path
        val path = Path()
        points.forEachIndexed { index, point ->
            val px = xToPx(point.timeHr)
            val py = yToPx(point.concentrationMgL)
            if (index == 0) {
                path.moveTo(px, py)
            } else {
                path.lineTo(px, py)
            }
        }
        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 3.5f, cap = StrokeCap.Round)
        )

        // Draw X-axis ticks (0, 0.5tau, 1tau, 1.5tau, 2tau, 2.5tau)
        val numTicks = 5
        for (i in 0..numTicks) {
            val tVal = maxTimeHr * (i.toDouble() / numTicks)
            val px = xToPx(tVal)
            drawLine(
                color = axisColor,
                start = Offset(px, height - paddingBottom),
                end = Offset(px, height - paddingBottom + 5f),
                strokeWidth = 2f
            )
            val tLayout = textMeasurer.measure(
                text = "%.1fh".format(tVal),
                style = TextStyle(fontSize = 10.sp, color = lineTextColor)
            )
            drawText(tLayout, topLeft = Offset(px - tLayout.size.width / 2f, height - paddingBottom + 8f))
        }
    }
}
