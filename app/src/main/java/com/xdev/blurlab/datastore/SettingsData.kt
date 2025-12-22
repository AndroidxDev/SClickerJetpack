package com.xdev.blurlab.datastore

data class SettingsData(
    val blurRadius2: Float,
    val progress: Float,
    val blurEnable: Boolean,
    val gradientBlur: Boolean,
    val isLarge: Boolean,
    val navShow: Boolean,
    val blurType: String,
    val themeType: String,
    val liquidglassbar: Boolean,
    val liquidTopBar: Boolean,
    val liquidSwitch: Boolean,
    val liquidSlider: Boolean,
    val liquidDialog: Boolean,
    val iosSettings: Boolean
)