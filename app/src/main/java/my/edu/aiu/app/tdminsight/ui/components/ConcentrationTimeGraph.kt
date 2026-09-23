package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.roundToInt

private data class GraphPoint(
    val time: Double,
    val concentration: Double,
    val label: String,
    val measured: Boolean = false
)

private data class SelectedGraphPoint(
    val time: Double,
    val concentration: Double
)

@Composable
fun ConcentrationTimeGraph(
    input: TDMInput,
    result: TDMResult,
    modifier: Modifier = Modifier
) {

    // ---------------------------------------------------------
    // CREATE GRAPH DATA
    // ---------------------------------------------------------

    val points = remember(input, result) {
        createGraphPoints(
            input = input,
            result = result
        )
    }

    if (points.isEmpty()) {

        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {

            Text(
                text = "No concentration-time data available.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        return
    }

    // ---------------------------------------------------------
    // GRAPH LIMITS
    // ---------------------------------------------------------

    val maxTime =
        points
            .maxOf { it.time }
            .coerceAtLeast(1.0)

    val maxConcentration =
        points
            .maxOf { it.concentration }
            .coerceAtLeast(1.0)

    // ---------------------------------------------------------
    // EXACT SELECTED POINT
    //
    // This is NOT an index anymore.
    //
    // It stores the exact time and concentration where
    // the user tapped.
    // ---------------------------------------------------------

    var selectedPoint by remember(input, result) {
        mutableStateOf<SelectedGraphPoint?>(null)
    }

    // ---------------------------------------------------------
    // COLORS
    // ---------------------------------------------------------

    val primaryColor =
        MaterialTheme.colorScheme.primary

    val errorColor =
        MaterialTheme.colorScheme.error

    val outlineColor =
        MaterialTheme.colorScheme.outlineVariant

    val selectedColor =
        MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = "Concentration-Time Profile",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text =
                "Tap anywhere on the graph to view the exact time and concentration.",
            style = MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =====================================================
        // GRAPH
        // =====================================================

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 2.dp
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .padding(12.dp)
            ) {

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(255.dp)

                        // -------------------------------------
                        // TAP HANDLER
                        // -------------------------------------

                        .pointerInput(
                            points,
                            maxTime,
                            maxConcentration,
                            result.ke
                        ) {

                            detectTapGestures { tapOffset ->

                                val selectedTime =
                                    timeFromTap(
                                        tapOffset = tapOffset,
                                        maxTime = maxTime,
                                        canvasWidth =
                                            size.width.toFloat(),
                                        canvasHeight =
                                            size.height.toFloat()
                                    )

                                if (selectedTime != null) {

                                    val concentration =
                                        concentrationAtTime(
                                            time =
                                                selectedTime,
                                            points =
                                                points,
                                            ke =
                                                result.ke
                                        )

                                    selectedPoint =
                                        SelectedGraphPoint(
                                            time =
                                                selectedTime,
                                            concentration =
                                                concentration
                                        )
                                }
                            }
                        }
                ) {

                    val leftPadding = 52f
                    val rightPadding = 18f
                    val topPadding = 20f
                    val bottomPadding = 38f

                    val graphWidth =
                        size.width -
                                leftPadding -
                                rightPadding

                    val graphHeight =
                        size.height -
                                topPadding -
                                bottomPadding

                    if (
                        graphWidth <= 0f ||
                        graphHeight <= 0f
                    ) {
                        return@Canvas
                    }

                    // =================================================
                    // POSITION FUNCTIONS
                    // =================================================

                    fun xPosition(
                        time: Double
                    ): Float {

                        return leftPadding +
                                (
                                        time / maxTime
                                        ).toFloat() *
                                graphWidth
                    }

                    fun yPosition(
                        concentration: Double
                    ): Float {

                        return topPadding +
                                graphHeight -
                                (
                                        concentration /
                                                maxConcentration
                                        ).toFloat() *
                                graphHeight
                    }

                    // =================================================
                    // GRID
                    // =================================================

                    val gridLines = 4

                    for (i in 0..gridLines) {

                        val ratio =
                            i.toFloat() /
                                    gridLines

                        val y =
                            topPadding +
                                    graphHeight *
                                    (1f - ratio)

                        drawLine(
                            color =
                                outlineColor.copy(
                                    alpha = 0.30f
                                ),
                            start =
                                Offset(
                                    leftPadding,
                                    y
                                ),
                            end =
                                Offset(
                                    leftPadding +
                                            graphWidth,
                                    y
                                ),
                            strokeWidth = 1f
                        )
                    }

                    // =================================================
                    // Y AXIS
                    // =================================================

                    drawLine(
                        color = outlineColor,
                        start =
                            Offset(
                                leftPadding,
                                topPadding
                            ),
                        end =
                            Offset(
                                leftPadding,
                                topPadding +
                                        graphHeight
                            ),
                        strokeWidth = 2f
                    )

                    // =================================================
                    // X AXIS
                    // =================================================

                    drawLine(
                        color = outlineColor,
                        start =
                            Offset(
                                leftPadding,
                                topPadding +
                                        graphHeight
                            ),
                        end =
                            Offset(
                                leftPadding +
                                        graphWidth,
                                topPadding +
                                        graphHeight
                            ),
                        strokeWidth = 2f
                    )

                    // =================================================
                    // X AXIS TICKS
                    // =================================================

                    for (i in 0..5) {

                        val ratio =
                            i.toFloat() / 5f

                        val x =
                            leftPadding +
                                    ratio *
                                    graphWidth

                        drawLine(
                            color = outlineColor,
                            start =
                                Offset(
                                    x,
                                    topPadding +
                                            graphHeight
                                ),
                            end =
                                Offset(
                                    x,
                                    topPadding +
                                            graphHeight +
                                            5f
                                ),
                            strokeWidth = 1f
                        )
                    }

                    // =================================================
                    // CONCENTRATION CURVE
                    // =================================================
                    //
                    // We draw many small points instead of only
                    // connecting the two measured points.
                    //
                    // This creates a smooth concentration-time curve.
                    // =================================================

                    val curvePath =
                        Path()

                    val sampleCount = 150

                    for (i in 0..sampleCount) {

                        val time =
                            maxTime *
                                    i.toDouble() /
                                    sampleCount

                        val concentration =
                            concentrationAtTime(
                                time =
                                    time,
                                points =
                                    points,
                                ke =
                                    result.ke
                            )

                        val x =
                            xPosition(time)

                        val y =
                            yPosition(
                                concentration
                                    .coerceAtLeast(0.0)
                            )

                        if (i == 0) {

                            curvePath.moveTo(
                                x,
                                y
                            )

                        } else {

                            curvePath.lineTo(
                                x,
                                y
                            )
                        }
                    }

                    drawPath(
                        path = curvePath,
                        color = primaryColor,
                        style =
                            Stroke(
                                width = 5f,
                                cap = StrokeCap.Round
                            )
                    )

                    // =================================================
                    // ORIGINAL MEASURED / REFERENCE POINTS
                    // =================================================

                    points.forEach { point ->

                        val x =
                            xPosition(
                                point.time
                            )

                        val y =
                            yPosition(
                                point.concentration
                            )

                        drawCircle(
                            color =
                                if (point.measured) {
                                    errorColor
                                } else {
                                    primaryColor
                                },
                            radius = 6f,
                            center =
                                Offset(
                                    x,
                                    y
                                )
                        )
                    }

                    // =================================================
                    // SELECTED EXACT POINT
                    // =================================================
                    //
                    // This is the important part.
                    //
                    // The marker is placed at the EXACT time the
                    // user tapped, not at 0 hr or the next measured
                    // point.
                    // =================================================

                    selectedPoint?.let { selected ->

                        val x =
                            xPosition(
                                selected.time
                            )

                        val y =
                            yPosition(
                                selected.concentration
                            )

                        // ---------------------------------------------
                        // VERTICAL GUIDE
                        // ---------------------------------------------

                        drawLine(
                            color =
                                selectedColor.copy(
                                    alpha = 0.45f
                                ),
                            start =
                                Offset(
                                    x,
                                    topPadding
                                ),
                            end =
                                Offset(
                                    x,
                                    topPadding +
                                            graphHeight
                                ),
                            strokeWidth = 2f
                        )

                        // ---------------------------------------------
                        // HORIZONTAL GUIDE
                        // ---------------------------------------------

                        drawLine(
                            color =
                                selectedColor.copy(
                                    alpha = 0.35f
                                ),
                            start =
                                Offset(
                                    leftPadding,
                                    y
                                ),
                            end =
                                Offset(
                                    leftPadding +
                                            graphWidth,
                                    y
                                ),
                            strokeWidth = 2f
                        )

                        // ---------------------------------------------
                        // OUTER CIRCLE
                        // ---------------------------------------------

                        drawCircle(
                            color = selectedColor,
                            radius = 10f,
                            center =
                                Offset(
                                    x,
                                    y
                                ),
                            style =
                                Stroke(
                                    width = 3f
                                )
                        )

                        // ---------------------------------------------
                        // INNER CIRCLE
                        // ---------------------------------------------

                        drawCircle(
                            color = primaryColor,
                            radius = 6f,
                            center =
                                Offset(
                                    x,
                                    y
                                )
                        )
                    }
                }

                // =====================================================
                // Y AXIS LABEL
                // =====================================================

                Text(
                    text = "Concentration (mg/L)",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant,
                    modifier =
                        Modifier
                            .align(
                                Alignment.TopStart
                            )
                            .padding(
                                start = 2.dp
                            )
                )

                // =====================================================
                // X AXIS LABEL
                // =====================================================

                Text(
                    text = "Time (hr)",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant,
                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomEnd
                            )
                            .padding(
                                end = 4.dp
                            )
                )
            }
        }

        // =============================================================
        // X AXIS VALUES
        // =============================================================

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 52.dp,
                        end = 18.dp
                    ),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = "0",
                style =
                    MaterialTheme.typography.labelSmall
            )

            Text(
                text =
                    formatNumber(maxTime),
                style =
                    MaterialTheme.typography.labelSmall
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // =============================================================
        // LEGEND
        // =============================================================

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(18.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            LegendItem(
                label = "Calculated profile",
                color = primaryColor
            )

            LegendItem(
                label = "Measured level",
                color = errorColor
            )
        }

        // =============================================================
        // SELECTED POINT INFORMATION
        // =============================================================

        selectedPoint?.let { selected ->

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(10.dp),
                color =
                    MaterialTheme.colorScheme
                        .secondaryContainer
            ) {

                Column(
                    modifier =
                        Modifier.padding(14.dp)
                ) {

                    Text(
                        text = "Selected Point",
                        style =
                            MaterialTheme.typography
                                .titleSmall
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Time: ${
                                formatNumber(
                                    selected.time
                                )
                            } hr",
                        style =
                            MaterialTheme.typography
                                .bodyLarge
                    )

                    Text(
                        text =
                            "Concentration: ${
                                formatNumber(
                                    selected.concentration
                                )
                            } mg/L",
                        style =
                            MaterialTheme.typography
                                .bodyLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Calculated at the selected time",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Ke: ${
                                formatNumber(
                                    result.ke
                                )
                            } /hr",
                        style =
                            MaterialTheme.typography
                                .bodySmall
                    )
                }
            }
        }
    }
}

// ============================================================================
// LEGEND ITEM
// ============================================================================

@Composable
private fun LegendItem(
    label: String,
    color: Color
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Canvas(
            modifier =
                Modifier
                    .padding(end = 6.dp)
                    .height(10.dp)
        ) {

            drawCircle(
                color = color,
                radius = 5f,
                center =
                    Offset(
                        5f,
                        size.height / 2f
                    )
            )
        }

        Text(
            text = label,
            style =
                MaterialTheme.typography.labelSmall
        )
    }
}

// ============================================================================
// CREATE GRAPH POINTS
// ============================================================================

private fun createGraphPoints(
    input: TDMInput,
    result: TDMResult
): List<GraphPoint> {

    return when (input) {

        // =============================================================
        // PRE
        // =============================================================

        is TDMInput.Pre -> {

            val trough =
                input.preLevelConc

            if (trough <= 0.0) {

                emptyList()

            } else {

                val peakEstimate =
                    result.expectedCmax
                        ?.takeIf {
                            it > 0.0
                        }
                        ?: calculatePeakFromTrough(
                            trough =
                                trough,
                            ke =
                                result.ke,
                            interval =
                                input.intervalHr
                        )

                listOf(

                    GraphPoint(
                        time = 0.0,
                        concentration =
                            peakEstimate,
                        label =
                            "Estimated peak",
                        measured = false
                    ),

                    GraphPoint(
                        time =
                            input.intervalHr
                                .coerceAtLeast(
                                    1.0
                                ),
                        concentration =
                            trough,
                        label =
                            "Measured trough",
                        measured = true
                    )
                )
            }
        }

        // =============================================================
        // POST
        // =============================================================

        is TDMInput.Post -> {

            val peak =
                input.postLevelConc

            if (peak <= 0.0) {

                emptyList()

            } else {

                val samplingTime =
                    input.samplingTimeHr
                        .coerceAtLeast(0.1)

                val endTime =
                    max(
                        input.intervalHr,
                        samplingTime + 1.0
                    )

                val measuredPoint =
                    GraphPoint(
                        time =
                            samplingTime,
                        concentration =
                            peak,
                        label =
                            "Measured post-dose level",
                        measured = true
                    )

                val troughEstimate =
                    concentrationAtTime(
                        time =
                            input.intervalHr,
                        points =
                            listOf(
                                measuredPoint
                            ),
                        ke =
                            result.ke
                    )

                listOf(

                    GraphPoint(
                        time = 0.0,
                        concentration =
                            peak *
                                    exp(
                                        result.ke *
                                                samplingTime
                                    ),
                        label =
                            "Estimated concentration at dose time",
                        measured = false
                    ),

                    measuredPoint,

                    GraphPoint(
                        time = endTime,
                        concentration =
                            troughEstimate
                                .coerceAtLeast(
                                    0.0
                                ),
                        label =
                            "Estimated later concentration",
                        measured = false
                    )
                )
            }
        }

        // =============================================================
        // PRE + POST
        // =============================================================

        is TDMInput.PrePost -> {

            val trough =
                input.preLevelConc

            val peak =
                input.postLevelConc

            if (
                trough <= 0.0 ||
                peak <= 0.0
            ) {

                emptyList()

            } else {

                val gap =
                    input.preToPostGapHr
                        .coerceAtLeast(0.1)

                listOf(

                    GraphPoint(
                        time = 0.0,
                        concentration =
                            peak,
                        label =
                            "Measured post-dose level",
                        measured = true
                    ),

                    GraphPoint(
                        time = gap,
                        concentration =
                            trough,
                        label =
                            "Measured pre-dose level",
                        measured = true
                    )
                )
            }
        }
    }
}

// ============================================================================
// CALCULATE PEAK FROM TROUGH
// ============================================================================

private fun calculatePeakFromTrough(
    trough: Double,
    ke: Double,
    interval: Double
): Double {

    if (
        trough <= 0.0 ||
        ke <= 0.0 ||
        interval <= 0.0
    ) {
        return trough
    }

    return trough *
            exp(
                ke * interval
            )
}

// ============================================================================
// CONCENTRATION AT EXACT TIME
// ============================================================================
//
// This function is used both:
// 1. To draw the curve.
// 2. To calculate the value when the user taps.
//
// Therefore the displayed selected value corresponds to the curve.
// ============================================================================

private fun concentrationAtTime(
    time: Double,
    points: List<GraphPoint>,
    ke: Double
): Double {

    if (points.isEmpty()) {
        return 0.0
    }

    // -------------------------------------------------------------
    // BEFORE FIRST POINT
    // -------------------------------------------------------------

    val firstPoint =
        points.minByOrNull {
            it.time
        } ?: return 0.0

    if (time <= firstPoint.time) {

        return if (ke > 0.0) {

            firstPoint.concentration *
                    exp(
                        -ke *
                                (
                                        time -
                                                firstPoint.time
                                        )
                    )

        } else {

            firstPoint.concentration
        }.coerceAtLeast(0.0)
    }

    // -------------------------------------------------------------
    // AFTER LAST POINT
    // -------------------------------------------------------------

    val lastPoint =
        points.maxByOrNull {
            it.time
        } ?: return 0.0

    if (time >= lastPoint.time) {

        return if (ke > 0.0) {

            lastPoint.concentration *
                    exp(
                        -ke *
                                (
                                        time -
                                                lastPoint.time
                                        )
                    )

        } else {

            lastPoint.concentration
        }.coerceAtLeast(0.0)
    }

    // -------------------------------------------------------------
    // FIND THE TWO POINTS AROUND THE SELECTED TIME
    // -------------------------------------------------------------

    val sorted =
        points.sortedBy {
            it.time
        }

    for (i in 0 until sorted.lastIndex) {

        val left =
            sorted[i]

        val right =
            sorted[i + 1]

        if (
            time >= left.time &&
            time <= right.time
        ) {

            // -----------------------------------------------------
            // EXPONENTIAL INTERPOLATION
            //
            // This keeps the concentration-time relationship
            // smooth between the two known points.
            // -----------------------------------------------------

            if (
                left.concentration > 0.0 &&
                right.concentration > 0.0 &&
                right.time > left.time
            ) {

                val fraction =
                    (
                            time -
                                    left.time
                            ) /
                            (
                                    right.time -
                                            left.time
                                    )

                val logLeft =
                    ln(
                        left.concentration
                    )

                val logRight =
                    ln(
                        right.concentration
                    )

                val interpolatedLog =
                    logLeft +
                            fraction *
                            (
                                    logRight -
                                            logLeft
                                    )

                return exp(
                    interpolatedLog
                ).coerceAtLeast(0.0)
            }

            // -----------------------------------------------------
            // FALLBACK LINEAR INTERPOLATION
            // -----------------------------------------------------

            val fraction =
                (
                        time -
                                left.time
                        ) /
                        (
                                right.time -
                                        left.time
                                )

            return (
                    left.concentration +
                            fraction *
                            (
                                    right.concentration -
                                            left.concentration
                                    )
                    ).coerceAtLeast(0.0)
        }
    }

    return lastPoint.concentration
        .coerceAtLeast(0.0)
}

// ============================================================================
// CONVERT TAP POSITION TO EXACT TIME
// ============================================================================

private fun timeFromTap(
    tapOffset: Offset,
    maxTime: Double,
    canvasWidth: Float,
    canvasHeight: Float
): Double? {

    val leftPadding = 52f
    val rightPadding = 18f
    val topPadding = 20f
    val bottomPadding = 38f

    val graphWidth =
        canvasWidth -
                leftPadding -
                rightPadding

    val graphHeight =
        canvasHeight -
                topPadding -
                bottomPadding

    if (
        graphWidth <= 0f ||
        graphHeight <= 0f
    ) {
        return null
    }

    // -------------------------------------------------------------
    // MAKE SURE TAP IS INSIDE GRAPH
    // -------------------------------------------------------------

    if (
        tapOffset.x < leftPadding ||
        tapOffset.x >
        leftPadding + graphWidth ||
        tapOffset.y < topPadding ||
        tapOffset.y >
        topPadding + graphHeight
    ) {
        return null
    }

    // -------------------------------------------------------------
    // CONVERT X POSITION TO EXACT TIME
    // -------------------------------------------------------------

    val ratio =
        (
                tapOffset.x -
                        leftPadding
                ) /
                graphWidth

    return (
            ratio.coerceIn(0f, 1f)
                .toDouble() *
                    maxTime
            )
}

// ============================================================================
// FORMAT NUMBER
// ============================================================================

private fun formatNumber(
    value: Double
): String {

    return if (
        value ==
        value.roundToInt().toDouble()
    ) {

        value
            .roundToInt()
            .toString()

    } else {

        "%.2f".format(value)
    }
}