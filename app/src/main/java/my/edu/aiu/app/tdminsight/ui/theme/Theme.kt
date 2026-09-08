package my.edu.aiu.app.tdminsight.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TDMColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = CardWhite,
    secondaryContainer = TealIconBg,
    onSecondaryContainer = TealPrimary,
    background = PageBackground,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    outline = BorderLight,
    surfaceVariant = PageBackground,
    onSurfaceVariant = TextSecondary
)

private val TDMShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

private val TDMTypography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextPrimary),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, color = TextPrimary),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, color = TextSecondary),
    labelSmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, color = TextSecondary)
)

@Composable
fun TDMInsightTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TDMColorScheme,
        shapes = TDMShapes,
        typography = TDMTypography,
        content = content
    )
}