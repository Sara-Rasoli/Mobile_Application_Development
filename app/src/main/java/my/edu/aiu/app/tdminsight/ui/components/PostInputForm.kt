package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PostInputForm(
    doseMg: String,
    onDoseMgChange: (String) -> Unit,
    intervalHr: String,
    onIntervalHrChange: (String) -> Unit,
    infusionDurationHr: String,
    onInfusionDurationHrChange: (String) -> Unit,
    samplingTimeHr: String,
    onSamplingTimeHrChange: (String) -> Unit,
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
                value = samplingTimeHr,
                onValueChange = onSamplingTimeHrChange,
                label = { Text("Sampling Time After Infusion Start (hr)") },
                isError = errors.containsKey("samplingTimeHr"),
                supportingText = { errors["samplingTimeHr"]?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SectionCard {
            Text("Concentration Information", style = MaterialTheme.typography.titleMedium)
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