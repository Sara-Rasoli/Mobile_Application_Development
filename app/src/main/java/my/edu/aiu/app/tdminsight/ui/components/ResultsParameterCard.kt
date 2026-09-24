package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ResultParameter(
    val label: String,
    val value: String
)

@Composable
fun ResultsParameterCard(
    title: String,
    parameters: List<ResultParameter>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.padding(top = 6.dp)
            )

            parameters.forEach { parameter ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {
                    Text(
                        text = parameter.label,
                        style =
                            MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = parameter.value,
                        style =
                            MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}