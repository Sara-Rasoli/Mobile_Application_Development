package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary

@Composable
fun AppLogo(
    size: Dp = 40.dp
) {
    Canvas(
        modifier = Modifier
            .size(size)
            .background(
                color = TealPrimary,
                shape = RoundedCornerShape(size * 0.24f)
            )
    ) {

        val width = this.size.width
        val height = this.size.height

        val path = Path().apply {

            moveTo(
                width * 0.15f,
                height * 0.53f
            )

            lineTo(
                width * 0.34f,
                height * 0.53f
            )

            lineTo(
                width * 0.44f,
                height * 0.30f
            )

            lineTo(
                width * 0.54f,
                height * 0.70f
            )

            lineTo(
                width * 0.65f,
                height * 0.45f
            )

            lineTo(
                width * 0.85f,
                height * 0.45f
            )
        }

        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(
                width = width * 0.075f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}