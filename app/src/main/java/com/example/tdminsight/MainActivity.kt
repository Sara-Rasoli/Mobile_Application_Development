package com.example.tdminsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tdminsight.ui.navigation.AppNavigation
import com.example.tdminsight.ui.theme.TDMInsightTheme

/**
 * Per the group's revision notes:
 * - Removed the default Greeting()/@Preview scaffolding.
 * - MainActivity now just sets the theme and hands off entirely to
 *   AppNavigation(), which owns the whole screen flow.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TDMInsightTheme {
                AppNavigation()
            }
        }
    }
}
