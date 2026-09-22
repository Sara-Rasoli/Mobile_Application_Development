package my.edu.aiu.app.tdminsight.ui.camera

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

@Composable
fun LabReportOcrScanner(
    onValueSelected: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoFile by remember { mutableStateOf<File?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var candidates by remember { mutableStateOf<List<Double>>(emptyList()) }

    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            try {
                val image = InputImage.fromFilePath(context, photoUri!!)
                textRecognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val text = visionText.text
                        val list = extractConcentrationCandidates(text)
                        if (list.isEmpty()) {
                            Toast.makeText(context, "No concentration found, please type it manually", Toast.LENGTH_LONG).show()
                        } else {
                            candidates = list
                            showDialog = true
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "OCR failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(context, "Error processing photo: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    TextButton(
        onClick = {
            try {
                val directory = File(context.externalCacheDir, "Pictures")
                if (!directory.exists()) directory.mkdirs()
                val file = File.createTempFile("lab_report_", ".jpg", directory)
                photoFile = file
                photoUri = FileProvider.getUriForFile(
                    context,
                    "my.edu.aiu.app.tdminsight.fileprovider",
                    file
                )
                takePictureLauncher.launch(photoUri!!)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to launch camera: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier
    ) {
        Text("Scan Report")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Scanned Concentration") },
            text = {
                Column {
                    Text("We detected the following potential values. Tap the one to use:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn {
                        items(candidates) { candidate ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onValueSelected(candidate)
                                        showDialog = false
                                    }
                            ) {
                                Text(
                                    text = "$candidate mg/L",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
