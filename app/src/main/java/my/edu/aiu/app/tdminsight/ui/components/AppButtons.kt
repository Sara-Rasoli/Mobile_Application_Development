package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.BorderLight
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
import my.edu.aiu.app.tdminsight.ui.theme.TextPrimary

@Composable
fun PrimaryAppButton(
    text: String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary, contentColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text)
            if (showIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
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