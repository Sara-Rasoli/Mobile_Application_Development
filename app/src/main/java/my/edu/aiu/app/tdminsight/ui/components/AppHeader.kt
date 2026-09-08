package my.edu.aiu.app.tdminsight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.edu.aiu.app.tdminsight.ui.theme.BorderLight
import my.edu.aiu.app.tdminsight.ui.theme.CardWhite
import my.edu.aiu.app.tdminsight.ui.theme.TealPrimary
import my.edu.aiu.app.tdminsight.ui.theme.TextSecondary

@Composable
fun AppHeader() {
    Surface(color = CardWhite, tonalElevation = 0.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppLogo(size = 36.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("TDM Insight", style = MaterialTheme.typography.titleMedium)
                    Text("Vancomycin TDM", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
        }
        HorizontalDivider(color = BorderLight, thickness = 1.dp)
    }
}