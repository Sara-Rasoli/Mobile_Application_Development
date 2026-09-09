package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PrePostInputForm(
    doseMg: String,
    onDoseMgChange: (String) -> Unit,
    intervalHr: String,
    onIntervalHrChange: (String) -> Unit,
    infusionDurationHr: String,
    onInfusionDurationHrChange: (String) -> Unit,
    infusionToPostGapHr: String,
    onInfusionToPostGapHrChange: (String) -> Unit,
    preToPostGapHr: String,
    onPreToPostGapHrChange: (String) -> Unit,
    preLevelConc: String,
    onPreLevelConcChange: (String) -> Unit,
    postLevelConc: String,
    onPostLevelConcChange: (String) -> Unit,
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
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = infusionToPostGapHr,
                onValueChange = onInfusionToPostGapHrChange,
                label = { Text("Infusion End to Post-level Gap (hr)") },
                isError = errors.containsKey("infusionToPostGapHr"),
                supportingText = { errors["infusionToPostGapHr"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = preToPostGapHr,
                onValueChange = onPreToPostGapHrChange,
                label = { Text("Pre-level to Post-level Gap (hr)") },
                isError = errors.containsKey("preToPostGapHr"),
                supportingText = { errors["preToPostGapHr"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SectionCard {
            Text("Concentration Information", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = preLevelConc,
                onValueChange = onPreLevelConcChange,
                label = { Text("Pre-dose (trough) Level (mg/L)") },
                isError = errors.containsKey("preLevelConc"),
                supportingText = { errors["preLevelConc"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = postLevelConc,
                onValueChange = onPostLevelConcChange,
                label = { Text("Post-dose (peak) Level (mg/L)") },
                isError = errors.containsKey("postLevelConc"),
                supportingText = { errors["postLevelConc"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}