package my.edu.aiu.app.tdminsight.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import my.edu.aiu.app.tdminsight.calculation.PkMath
import my.edu.aiu.app.tdminsight.model.TDMInput
import my.edu.aiu.app.tdminsight.ui.components.AppFooter
import my.edu.aiu.app.tdminsight.ui.components.AppHeader
import my.edu.aiu.app.tdminsight.ui.components.ConcentrationChart
import my.edu.aiu.app.tdminsight.ui.components.PrimaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.ScreenTitleRow
import my.edu.aiu.app.tdminsight.ui.components.SecondaryAppButton
import my.edu.aiu.app.tdminsight.ui.components.SectionCard
import my.edu.aiu.app.tdminsight.ui.navigation.rememberSharedCaseViewModel
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun ExploreDosingScreen(navController: NavController) {
    val caseViewModel = rememberSharedCaseViewModel(navController)
    val result = caseViewModel.tdmResult
    val input = caseViewModel.tdmInput

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            ScreenTitleRow(icon = Icons.Filled.Timeline, title = "Explore Dosing")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Simulate how changing the regimen affects peak, trough, and exposure.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (result != null && input != null) {
                val initialDose = when (input) {
                    is TDMInput.Pre -> input.doseMg
                    is TDMInput.Post -> input.doseMg
                    is TDMInput.PrePost -> input.doseMg
                }
                val initialInterval = when (input) {
                    is TDMInput.Pre -> input.intervalHr
                    is TDMInput.Post -> input.intervalHr
                    is TDMInput.PrePost -> input.intervalHr
                }
                val initialTinf = when (input) {
                    is TDMInput.Pre -> input.infusionDurationHr
                    is TDMInput.Post -> input.infusionDurationHr
                    is TDMInput.PrePost -> input.infusionDurationHr
                }

                val ke = result.ke
                val vd = result.vd

                var currentDose by remember { mutableStateOf(initialDose) }
                var currentInterval by remember { mutableStateOf(initialInterval) }
                var currentTinf by remember { mutableStateOf(initialTinf) }

                val cmaxSs = PkMath.calculateCmaxSs(currentDose, currentTinf, currentInterval, ke, vd)
                val cminSs = PkMath.calculateCminSs(cmaxSs, ke, currentInterval, currentTinf)
                val clearance = PkMath.calculateClearance(ke, vd)
                val aucTau = PkMath.calculateAucTau(currentDose, clearance)
                val auc24 = PkMath.calculateAuc24(aucTau, currentInterval)
                val points = PkMath.generateCurvePoints(currentDose, currentTinf, currentInterval, ke, vd)

                SectionCard {
                    Text("Concentration vs Time Curve (2.5 Intervals)", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    ConcentrationChart(
                        points = points,
                        cmaxSs = cmaxSs,
                        cminSs = cminSs,
                        maxTimeHr = currentInterval * 2.5
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionCard {
                    Text("Simulated Steady-State Exposure", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Cmax,ss: %.1f mg/L".format(cmaxSs), style = MaterialTheme.typography.bodyMedium)
                            Text("Cmin,ss: %.1f mg/L".format(cminSs), style = MaterialTheme.typography.bodyMedium)
                        }
                        Column {
                            Text("AUC (interval): %.1f mg·h/L".format(aucTau), style = MaterialTheme.typography.bodyMedium)
                            Text("AUC (24h): %.1f mg·h/L".format(auc24), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionCard {
                    Text("Interactive Regimen Sliders", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Dose Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dose", style = MaterialTheme.typography.bodyMedium)
                        Text("%.0f mg".format(currentDose), style = MaterialTheme.typography.titleSmall)
                    }
                    Slider(
                        value = currentDose.toFloat(),
                        onValueChange = { currentDose = (it / 50f).roundToInt() * 50.0 },
                        valueRange = 250f..3000f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dosing Interval Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dosing Interval (tau)", style = MaterialTheme.typography.bodyMedium)
                        Text("%.1f hr".format(currentInterval), style = MaterialTheme.typography.titleSmall)
                    }
                    Slider(
                        value = currentInterval.toFloat(),
                        onValueChange = { currentInterval = (it * 2f).roundToInt() / 2.0 },
                        valueRange = 4f..48f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Infusion Duration Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Infusion Duration (tinf)", style = MaterialTheme.typography.bodyMedium)
                        Text("%.1f hr".format(currentTinf), style = MaterialTheme.typography.titleSmall)
                    }
                    Slider(
                        value = currentTinf.toFloat(),
                        onValueChange = { currentTinf = (it * 2f).roundToInt() / 2.0 },
                        valueRange = 0.5f..4.0f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryAppButton(
                        text = "Reset to entered values",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        currentDose = initialDose
                        currentInterval = initialInterval
                        currentTinf = initialTinf
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Exploring hypothetical dosing changes using this patient's own calculated Ke and Vd. This is for educational visualization only — it is not a dosing recommendation.",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                PrimaryAppButton(
                    text = "Back to Results",
                    modifier = Modifier.fillMaxWidth(),
                    showIcon = false
                ) {
                    navController.popBackStack()
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No calculation data found — please go back and complete a calculation first.")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            AppFooter()
        }
    }
}
