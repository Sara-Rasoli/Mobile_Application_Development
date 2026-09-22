package my.edu.aiu.app.tdminsight

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import my.edu.aiu.app.tdminsight.ui.navigation.AppNavigation
import my.edu.aiu.app.tdminsight.ui.theme.PageBackground
import my.edu.aiu.app.tdminsight.ui.theme.TDMInsightTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep the Android status bar visible.
        // This allows the phone clock, battery, Wi-Fi and other
        // system information to remain clearly visible.
        WindowCompat.setDecorFitsSystemWindows(window, true)

        window.statusBarColor = PageBackground.toArgbCompat()
        window.navigationBarColor = PageBackground.toArgbCompat()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        setContent {
            TDMInsightTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

/*
 * Converts a Compose Color to the Android color integer required
 * by Window.statusBarColor and Window.navigationBarColor.
 */
private fun androidx.compose.ui.graphics.Color.toArgbCompat(): Int {
    return android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}