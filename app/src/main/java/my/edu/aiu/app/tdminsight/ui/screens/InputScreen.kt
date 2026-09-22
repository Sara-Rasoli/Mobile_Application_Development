package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.edu.aiu.app.tdminsight.domain.model.FieldId

@Composable
fun InputScreen() {
    // Public container placeholder to hold the file
}

@Composable
private fun FieldInput(
    field: FieldId,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(field.name) },
        modifier = Modifier.fillMaxWidth()
    )
}
