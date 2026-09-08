package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
        OutlinedTextField(
            value = doseMg,
            onValueChange = onDoseMgChange,
            label = { Text("Dose (mg)") },
            isError = errors.containsKey("doseMg"),
            supportingText = { errors["doseMg"]?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = intervalHr,
            onValueChange = onIntervalHrChange,
            label = { Text("Dosing Interval (hr)") },
            isError = errors.containsKey("intervalHr"),
            supportingText = { errors["intervalHr"]?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = infusionDurationHr,
            onValueChange = onInfusionDurationHrChange,
            label = { Text("Infusion Duration (hr)") },
            isError = errors.containsKey("infusionDurationHr"),
            supportingText = { errors["infusionDurationHr"]?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = preLevelConc,
            onValueChange = onPreLevelConcChange,
            label = { Text("Pre-dose (trough) Level (mg/L)") },
            isError = errors.containsKey("preLevelConc"),
            supportingText = { errors["preLevelConc"]?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
    }
}