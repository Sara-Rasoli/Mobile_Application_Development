package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.edu.aiu.app.tdminsight.data.CalculationHistoryRecord
import my.edu.aiu.app.tdminsight.data.TDMHistoryRepository
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CalculationHistoryScreen(
    navController: NavController
) {

    val context =
        LocalContext.current

    val repository =
        remember(context) {
            TDMHistoryRepository(
                context
            )
        }

    val scope =
        rememberCoroutineScope()

    var records by remember {
        mutableStateOf(
            emptyList<CalculationHistoryRecord>()
        )
    }

    var showClearDialog by remember {
        mutableStateOf(false)
    }

    // =============================================================
    // LOAD SAVED RECORDS
    // =============================================================

    LaunchedEffect(Unit) {

        records =
            withContext(
                Dispatchers.IO
            ) {

                repository.getAllCases()
            }
    }

    // =============================================================
    // CLEAR ALL CONFIRMATION
    // =============================================================

    if (showClearDialog) {

        AlertDialog(
            onDismissRequest = {
                showClearDialog = false
            },

            title = {
                Text(
                    text = "Clear History"
                )
            },

            text = {
                Text(
                    text =
                        "Are you sure you want to delete all saved calculation records?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        showClearDialog = false

                        scope.launch {

                            withContext(
                                Dispatchers.IO
                            ) {

                                repository.deleteAllCases()
                            }

                            records =
                                emptyList()
                        }
                    }
                ) {

                    Text(
                        text = "Clear All"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showClearDialog = false
                    }
                ) {

                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }

    // =============================================================
    // SCREEN
    // =============================================================

    Column(
        modifier =
            Modifier.fillMaxSize()
    ) {

        AppHeader(
            navController
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.History,
                    contentDescription = null
                )

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Text(
                    text = "Case History",
                    style =
                        MaterialTheme.typography
                            .headlineSmall
                )
            }

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Saved calculation records from this device.",
                style =
                    MaterialTheme.typography
                        .bodyMedium,
                color =
                    TextSecondary
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            if (records.isNotEmpty()) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.End
                ) {

                    TextButton(
                        onClick = {
                            showClearDialog = true
                        }
                    ) {

                        Text(
                            text = "Clear All"
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )
            }

            if (records.isEmpty()) {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Inbox,
                        contentDescription = null,
                        tint =
                            TextSecondary,
                        modifier =
                            Modifier.height(48.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "No saved cases yet",
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Completed calculations will appear here.",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            TextSecondary
                    )
                }

            } else {

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(
                            12.dp
                        )
                ) {

                    items(
                        items = records,
                        key = {
                            it.id
                        }
                    ) { record ->

                        HistoryRecordCard(
                            record = record,
                            onDelete = {

                                scope.launch {

                                    withContext(
                                        Dispatchers.IO
                                    ) {

                                        repository.deleteCase(
                                            record.id
                                        )
                                    }

                                    records =
                                        records.filter {
                                            it.id != record.id
                                        }
                                }
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            SecondaryAppButton(
                text = "Back",
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                navController.popBackStack()
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            AppFooter()
        }
    }
}

@Composable
private fun HistoryRecordCard(
    record: CalculationHistoryRecord,
    onDelete: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme
                        .surfaceVariant
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            record.patientName,
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            "Case ID: ${record.caseId}",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            TextSecondary
                    )
                }

                IconButton(
                    onClick = onDelete
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Delete,
                        contentDescription =
                            "Delete record"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Workflow: ${formatWorkflow(record.workflow)}",
                style =
                    MaterialTheme.typography
                        .bodyMedium
            )

            Text(
                text =
                    formatDate(record.createdAt),
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    TextSecondary
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // =====================================================
            // PATIENT CALCULATIONS
            // =====================================================

            Text(
                text =
                    "Patient Calculations",
                style =
                    MaterialTheme.typography
                        .titleSmall
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            HistoryValueRow(
                label = "Ideal Body Weight",
                value =
                    "%.2f kg"
                        .format(
                            record.idealBodyWeightKg
                        )
            )

            HistoryValueRow(
                label = "Dosing Weight",
                value =
                    "%.2f kg"
                        .format(
                            record.dosingWeightKg
                        )
            )

            HistoryValueRow(
                label = "Creatinine Clearance",
                value =
                    "%.2f mL/min"
                        .format(
                            record.creatinineClearanceMlMin
                        )
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // =====================================================
            // TDM RESULTS
            // =====================================================

            Text(
                text =
                    "TDM Results",
                style =
                    MaterialTheme.typography
                        .titleSmall
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            HistoryValueRow(
                label = "Elimination Rate",
                value =
                    "%.5f /hr"
                        .format(record.ke)
            )

            HistoryValueRow(
                label = "Half-Life",
                value =
                    "%.2f hours"
                        .format(record.halfLifeHr)
            )

            HistoryValueRow(
                label = "Volume of Distribution",
                value =
                    "%.2f L"
                        .format(record.vd)
            )

            record.clearance?.let {

                HistoryValueRow(
                    label = "Clearance",
                    value =
                        "%.2f L/hr"
                            .format(it)
                )
            }

            record.auc24?.let {

                HistoryValueRow(
                    label = "AUC₂₄",
                    value =
                        "%.2f mg•h/L"
                            .format(it)
                )
            }

            record.micMgL?.let {

                HistoryValueRow(
                    label = "MIC",
                    value =
                        "%.2f mg/L"
                            .format(it)
                )
            }

            record.aucMic?.let {

                HistoryValueRow(
                    label = "AUC/MIC",
                    value =
                        "%.2f"
                            .format(it)
                )
            }

            record.expectedCmax?.let {

                HistoryValueRow(
                    label = "Peak (Cmax)",
                    value =
                        "%.2f mg/L"
                            .format(it)
                )
            }

            record.expectedCmin?.let {

                HistoryValueRow(
                    label = "Trough (Cmin)",
                    value =
                        "%.2f mg/L"
                            .format(it)
                )
            }
        }
    }
}

@Composable
private fun HistoryValueRow(
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 2.dp
                ),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style =
                MaterialTheme.typography
                    .bodySmall,
            color =
                TextSecondary
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography
                    .bodySmall
        )
    }
}

private fun formatWorkflow(
    workflow: String
): String {

    return when (workflow) {

        "PRE" ->
            "Pre-dose"

        "POST" ->
            "Post-dose"

        "PRE_POST" ->
            "Pre + Post-dose"

        else ->
            workflow
    }
}

private fun formatDate(
    timestamp: Long
): String {

    val formatter =
        SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        )

    return formatter.format(
        Date(timestamp)
    )
}