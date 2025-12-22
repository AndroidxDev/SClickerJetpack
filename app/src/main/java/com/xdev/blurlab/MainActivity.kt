package com.xdev.blurlab

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xdev.blurlab.datastore.DataStoreManager
import com.xdev.blurlab.ui.theme.BlurLabTheme

class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sharedPreferences = this.getSharedPreferences("data", MODE_PRIVATE)

        val isNavShow = sharedPreferences.getBoolean("navShow", true)

        if (!isNavShow) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            insetsController.apply {
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        val dataStoreManager = DataStoreManager(applicationContext)

        setContent {
            var colorValue by remember { mutableStateOf("default") }
            BlurLabTheme(colorTheme = colorValue) {
                CustomScaffold(
                    onThemeChange = { colorValue = it },
                    navShow = { sharedPreferences.edit { putBoolean("navShow", it) } },
                    dataStoreManager
                )
            }
        }
    }
}