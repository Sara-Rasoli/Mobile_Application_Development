package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.domain.model.FieldId
import my.edu.aiu.app.tdminsight.ui.components.ConcentrationInputField

@Composable
fun InputScreen() {
    var sampleId by remember { mutableStateOf("") }
    var preConc by remember { mutableStateOf("") }
    var postConc by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FieldInput(field = FieldId.SAMPLE_ID, value = sampleId) { sampleId = it }
        FieldInput(field = FieldId.PRE_CONC, value = preConc) { preConc = it }
        FieldInput(field = FieldId.POST_CONC, value = postConc) { postConc = it }
    }
}

@Composable
private fun FieldInput(
    field: FieldId,
    value: String,
    onChange: (String) -> Unit,
) {
    val isConcentrationField = (field == FieldId.PRE_CONC) || (field == FieldId.POST_CONC)

    if (isConcentrationField) {
        ConcentrationInputField(
            label = field.name,
            value = value,
            onValueChange = onChange,
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(field.name) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
