package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.calculation.ClinicalPlausibilityChecker
import my.edu.aiu.app.tdminsight.calculation.PlausibilityItem
import my.edu.aiu.app.tdminsight.calculation.PlausibilityLevel
import my.edu.aiu.app.tdminsight.model.PatientInfo
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.model.TDMResult

private fun levelColors(level: PlausibilityLevel): Pair<Color, Color> {
    return when (level) {
        PlausibilityLevel.GOOD -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        PlausibilityLevel.REVIEW -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        PlausibilityLevel.CONCERNING -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
    }
}

private fun levelLabel(level: PlausibilityLevel): String {
    return when (level) {
        PlausibilityLevel.GOOD -> "✓ GOOD"
        PlausibilityLevel.REVIEW -> "⚠ REVIEW"
        PlausibilityLevel.CONCERNING -> "⛔ CONCERNING"
    }
}

@Composable
fun PlausibilityBadge(level: PlausibilityLevel) {
    val (bgColor, textColor) = levelColors(level)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, textColor)
    ) {
        Text(
            text = levelLabel(level),
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ClinicalPlausibilityCard(
    result: TDMResult,
    input: TDMInput? = null,
    patientInfo: PatientInfo? = null,
    modifier: Modifier = Modifier
) {
    val report = ClinicalPlausibilityChecker.evaluate(result, input, patientInfo)
    val (overallBg, overallText) = levelColors(report.overallLevel)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = overallBg.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Clinical Plausibility Check",
                        style = MaterialTheme.typography.titleMedium,
                        color = overallText
                    )
                    Text(
                        text = "Physiological heuristic evaluation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                PlausibilityBadge(level = report.overallLevel)
            }

            Spacer(modifier = Modifier.height(12.dp))

            report.items.forEach { item ->
                PlausibilityItemRow(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Note: These checks use general reference ranges (Cockcroft-Gault & Matzke equations) for educational review only, not a diagnostic or dosing tool.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlausibilityItemRow(item: PlausibilityItem) {
    val (bgColor, textColor) = levelColors(item.level)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = bgColor.copy(alpha = 0.5f),
        border = BorderStroke(0.5.dp, textColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.parameterName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                PlausibilityBadge(level = item.level)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.valueFormatted,
                style = MaterialTheme.typography.labelLarge,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
