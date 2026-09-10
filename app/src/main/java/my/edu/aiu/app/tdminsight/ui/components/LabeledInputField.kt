package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

@Composable
fun LabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    helperText: String,
    modifier: Modifier = Modifier,
    unit: String? = null,
    required: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorText: String? = null
) {
    Column(modifier = modifier) {
        Row {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            if (required) {
                Text(
                    " *",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            trailingIcon = unit?.let {
                {
                    Text(
                        it,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (isError && errorText != null) {
            Text(
                errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    helperText,
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}