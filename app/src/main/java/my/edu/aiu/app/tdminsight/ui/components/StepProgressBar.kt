package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.StepInactive
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

@Composable
fun StepProgressBar(steps: List<String>, currentStepIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val isDone = index < currentStepIndex
            val isCurrent = index == currentStepIndex
            val circleColor = if (isDone || isCurrent) TealPrimary else StepInactive
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                } else {
                    Text("${index + 1}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = if (isCurrent) TealPrimary else TextSecondary
            )
            if (index != steps.lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.width(20.dp).height(1.dp).background(StepInactive))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}