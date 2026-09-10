package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
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
            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputField(
                label = "Dose",
                value = doseMg,
                onValueChange = onDoseMgChange,
                unit = "mg",
                helperText = "Vancomycin dose administered.",
                keyboardType = KeyboardType.Decimal,
                isError = errors.containsKey("doseMg"),
                errorText = errors["doseMg"]
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        SectionCard {
            Text("Dosing Schedule", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputField(
                label = "Dosing Interval",
                value = intervalHr,
                onValueChange = onIntervalHrChange,
                unit = "h",
                helperText = "Time between consecutive doses.",
                keyboardType = KeyboardType.Decimal,
                isError = errors.containsKey("intervalHr"),
                errorText = errors["intervalHr"]
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledInputField(
                label = "Infusion Duration",
                value = infusionDurationHr,
                onValueChange = onInfusionDurationHrChange,
                unit = "h",
                helperText = "How long the infusion runs for.",
                keyboardType = KeyboardType.Decimal,
                isError = errors.containsKey("infusionDurationHr"),
                errorText = errors["infusionDurationHr"]
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        SectionCard {
            Text(
                "Concentration Information",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputField(
                label = "Pre-dose (Trough) Concentration",
                value = preLevelConc,
                onValueChange = onPreLevelConcChange,
                unit = "mg/L",
                helperText = "Measured concentration immediately before the next dose.",
                keyboardType = KeyboardType.Decimal,
                isError = errors.containsKey("preLevelConc"),
                errorText = errors["preLevelConc"]
            )
        }
    }
}