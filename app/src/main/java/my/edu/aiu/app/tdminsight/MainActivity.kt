package my.edu.aiu.app.tdminsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import my.edu.aiu.app.tdminsight.ui.navigation.AppNavigation
import my.edu.aiu.app.tdminsight.ui.theme.TDMInsightTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import my.edu.aiu.app.tdminsight.ui.theme.TDMInsightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TDMInsightTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}