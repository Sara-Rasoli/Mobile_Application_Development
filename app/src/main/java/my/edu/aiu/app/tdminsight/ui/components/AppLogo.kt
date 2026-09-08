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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary

@Composable
fun AppLogo(size: Dp = 40.dp) {
    Canvas(
        modifier = Modifier
            .size(size)
            .background(TealPrimary, RoundedCornerShape(size / 4))
    ) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.55f)
            lineTo(w * 0.35f, h * 0.55f)
            lineTo(w * 0.45f, h * 0.3f)
            lineTo(w * 0.55f, h * 0.7f)
            lineTo(w * 0.65f, h * 0.45f)
            lineTo(w * 0.85f, h * 0.45f)
        }
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(width = w * 0.06f, cap = StrokeCap.Round)
        )
    }
}