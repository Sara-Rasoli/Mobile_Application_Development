package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.BorderLight
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
import my.edu.aiu.app.tdminsight.ui.theme.TextPrimary

@Composable
fun PrimaryAppButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary, contentColor = androidx.compose.ui.graphics.Color.White)
    ) { Text(text) }
}

@Composable
fun SecondaryAppButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, BorderLight),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
    ) { Text(text) }
}