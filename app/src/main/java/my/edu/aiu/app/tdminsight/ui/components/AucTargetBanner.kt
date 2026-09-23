package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AucTargetBanner(
    auc24: Double,
    modifier: Modifier = Modifier
) {
    val targetLower = 400.0
    val targetUpper = 600.0

    val status: String
    val icon = when {
        auc24 in targetLower..targetUpper -> {
            status = "AUC24 Within Target"
            Icons.Filled.CheckCircle
        }

        auc24 < targetLower -> {
            status = "AUC24 Below Target"
            Icons.Filled.Info
        }

        else -> {
            status = "AUC24 Above Target"
            Icons.Filled.Warning
        }
    }

    val containerColor = when {
        auc24 in targetLower..targetUpper ->
            MaterialTheme.colorScheme.primaryContainer

        auc24 < targetLower ->
            MaterialTheme.colorScheme.secondaryContainer

        else ->
            MaterialTheme.colorScheme.errorContainer
    }

    val iconColor = when {
        auc24 in targetLower..targetUpper ->
            MaterialTheme.colorScheme.primary

        auc24 < targetLower ->
            MaterialTheme.colorScheme.secondary

        else ->
            MaterialTheme.colorScheme.error
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {
                Text(
                    text = status,
                    style = MaterialTheme.typography.titleMedium,
                    color = iconColor
                )

                Spacer(
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "Target: 400–600 mg·h/L",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}