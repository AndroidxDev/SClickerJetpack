package com.xdev.blurlab.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xdev.blurlab.preferences.extensions.dataStore
import kotlinx.coroutines.flow.map
class DataStoreManager(context2: Context) {
    private val context = context2.applicationContext

    suspend fun saveAll(settingsData: SettingsData) {
        context.dataStore.edit {
            //floats
            it[floatPreferencesKey("blur_radius")] = settingsData.blurRadius2
            it[floatPreferencesKey("progress")] = settingsData.progress

            //booleans
            it[booleanPreferencesKey("blur_enable")] = settingsData.blurEnable
            it[booleanPreferencesKey("gradient_blur")] = settingsData.gradientBlur
            it[booleanPreferencesKey("is_large")] = settingsData.isLarge
            it[booleanPreferencesKey("nav_show")] = settingsData.navShow
            it[booleanPreferencesKey("liquidglassbar")] = settingsData.liquidglassbar
            it[booleanPreferencesKey("liquidTopBar")] = settingsData.liquidTopBar

            //strings
            it[stringPreferencesKey("blur_type")] = settingsData.blurType
            it[stringPreferencesKey("theme_type")] = settingsData.themeType
            it[booleanPreferencesKey("liquidSwitch")] = settingsData.liquidSwitch
            it[booleanPreferencesKey("liquidSlider")] = settingsData.liquidSlider
            it[booleanPreferencesKey("iosSettings")] = settingsData.iosSettings
            it[booleanPreferencesKey("liquidDialog")] = settingsData.liquidDialog
        }
    }

    fun getAll() = context.dataStore.data.map {
        return@map SettingsData(
            //floats
            it[floatPreferencesKey("blur_radius")] ?: 25f,
            it[floatPreferencesKey("progress")] ?: 0.5f,

            //booleans
            it[booleanPreferencesKey("blur_enable")] ?: true,
            it[booleanPreferencesKey("gradient_blur")] ?: false,
            it[booleanPreferencesKey("is_large")] ?: false,
            it[booleanPreferencesKey("nav_show")] ?: true,

            //strings
            it[stringPreferencesKey("blur_type")] ?: "Ultra Thin",
            it[stringPreferencesKey("theme_type")] ?: "default",
            it[booleanPreferencesKey("liquidglassbar")] ?: false,
            it[booleanPreferencesKey("liquidTopBar")] ?: false,
            it[booleanPreferencesKey("liquidSwitch")] ?: false,
            it[booleanPreferencesKey("liquidSlider")] ?: false,
            it[booleanPreferencesKey("liquidDialog")] ?: false,
            it[booleanPreferencesKey("iosSettings")] ?: false
        )
    }

    suspend fun saveBlurRadius(radius: Float) {
        context.dataStore.edit {
            it[floatPreferencesKey("blur_radius")] = radius
        }
    }

    suspend fun saveProgress(radius: Float) {
        context.dataStore.edit {
            it[floatPreferencesKey("progress")] = radius
        }
    }

    suspend fun saveBlurEnable(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("blur_enable")] = enable
        }
    }

    suspend fun saveGradientBlur(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("gradient_blur")] = enable
        }
    }

    suspend fun saveIsLarge(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("is_large")] = enable
        }
    }

    suspend fun saveNavShow(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("nav_show")] = enable
        }
    }

    suspend fun saveLiquidBar(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("liquidglassbar")] = enable
        }
    }
    suspend fun saveLiquidTopBar(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("liquidTopBar")] = enable
        }
    }

    suspend fun saveLiquidSwitch(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("liquidSwitch")] = enable
        }
    }
    suspend fun saveLiquidSlider(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("liquidSlider")] = enable
        }
    }

    suspend fun saveIosSettings(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("iosSettings")] = enable
        }
    }

    suspend fun saveLiquidDialog(enable: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("liquidDialog")] = enable
        }
    }

    suspend fun saveBlurType(string: String) {
        context.dataStore.edit {
            it[stringPreferencesKey("blur_type")] = string
        }
    }

    suspend fun saveThemeType(string: String) {
        context.dataStore.edit {
            it[stringPreferencesKey("theme_type")] = string
        }
    }
}