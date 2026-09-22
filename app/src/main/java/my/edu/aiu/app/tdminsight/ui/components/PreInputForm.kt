package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PreInputForm(
    doseMg: String,
    onDoseMgChange: (String) -> Unit,
    intervalHr: String,
    onIntervalHrChange: (String) -> Unit,
    infusionDurationHr: String,
    onInfusionDurationHrChange: (String) -> Unit,
    preLevelConc: String,
    onPreLevelConcChange: (String) -> Unit,
    errors: Map<String, String>
) {
    Column {
        SectionCard {
            Text("Dose Information", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = doseMg,
                onValueChange = onDoseMgChange,
                label = { Text("Dose (mg)") },
                isError = errors.containsKey("doseMg"),
                supportingText = { errors["doseMg"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SectionCard {
            Text("Dosing Schedule", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = intervalHr,
                onValueChange = onIntervalHrChange,
                label = { Text("Dosing Interval (hr)") },
                isError = errors.containsKey("intervalHr"),
                supportingText = { errors["intervalHr"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = infusionDurationHr,
                onValueChange = onInfusionDurationHrChange,
                label = { Text("Infusion Duration (hr)") },
                isError = errors.containsKey("infusionDurationHr"),
                supportingText = { errors["infusionDurationHr"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SectionCard {
            Text("Concentration Information", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            ConcentrationInputField(
                label = "Pre-dose (trough) Level (mg/L)",
                value = preLevelConc,
                onValueChange = onPreLevelConcChange,
                isError = errors.containsKey("preLevelConc"),
                errorText = errors["preLevelConc"]
            )
        }
    }
}
