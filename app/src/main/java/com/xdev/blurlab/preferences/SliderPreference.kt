package com.xdev.blurlab.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.xdev.blurlab.liquidglass.components.LiquidSlider
import com.xdev.blurlab.preferences.extensions.dataStore
import kotlinx.coroutines.flow.map
import me.zhanghai.compose.preference.LocalPreferenceTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.rememberPreferenceState

@Composable
fun SliderPreference(
    key: String,
    defaultValue: Float,
    title: @Composable (Float) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
     rememberState: @Composable () -> MutableState<Float> = {
        rememberPreferenceState(key, defaultValue)
    },
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
     rememberSliderState: @Composable (Float) -> MutableFloatState = {
        remember { mutableFloatStateOf(it) }
    },
     enabled: (Float) -> Boolean = { true },
     icon: @Composable ((Float) -> Unit)? = null,
     summary: @Composable ((Float) -> Unit)? = null,
     valueText: @Composable ((Float) -> Unit)? = null,
     onValueChange: (Float) -> Unit

) {
   // item(key = key, contentType = "SliderPreference") {
        val state = rememberState()
        val value by state
        val sliderState = rememberSliderState(value)
        val sliderValue by sliderState
        SliderPreference(
            state = state,
            title = { title(sliderValue) },
            modifier = modifier,
            valueRange = valueRange,
            valueSteps = valueSteps,
            sliderState = sliderState,
            enabled = enabled(value),
            icon = icon?.let { { it(sliderValue) } },
            summary = summary?.let { { it(sliderValue) } },
            valueText = valueText?.let { { it(sliderValue) } },
            onValueChange = { onValueChange(it) }
        )
    //}
}

@Composable
fun SliderPreference(
    state: MutableState<Float>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    sliderState: MutableFloatState = remember { mutableFloatStateOf(state.value) },
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueText: @Composable (() -> Unit)? = null,
    onValueChange: (Float) -> Unit
) {
    var value by state
    var sliderValue by sliderState
    SliderPreference(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        sliderValue = sliderValue,
        onSliderValueChange = { sliderValue = it },
        title = title,
        modifier = modifier,
        valueRange = valueRange,
        valueSteps = valueSteps,
        enabled = enabled,
        icon = icon,
        summary = summary,
        valueText = valueText,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderPreference(
    value: Float,
    onValueChange: (Float) -> Unit,
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueText: @Composable (() -> Unit)? = null,
) {
    var lastValue by remember { mutableFloatStateOf(value) }
    SideEffect {
        if (value != lastValue) {
            onSliderValueChange(value)
            lastValue = value
        }
    }

    var latestSliderValue by remember { mutableFloatStateOf(sliderValue) }

    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = {
            Column {
                summary?.invoke()
                Row(verticalAlignment = Alignment.CenterVertically) {

                    val backgroundColor = MaterialTheme.colorScheme.background
                    val backdrop = rememberLayerBackdrop {
                        drawRect(backgroundColor)
                        drawContent()
                    }

                    val a = false
                    val liquidSlider by (LocalContext.current).dataStore.data.map {
                        return@map it[booleanPreferencesKey("liquidSlider")] ?: a
                    }.collectAsState(initial = a)

                    if (!liquidSlider) {
                        Slider(
                            value = sliderValue,
                            onValueChange = {
                                onSliderValueChange(it)
                                latestSliderValue = it
                            },
                            modifier = Modifier.weight(1f),
                            enabled = enabled,
                            valueRange = valueRange,
                            steps = valueSteps,
                            onValueChangeFinished = { onValueChange(latestSliderValue) }
                        )
                    } else {
                        LiquidSlider(
                            value = { latestSliderValue },
                            onValueChange = {
                                onValueChange(it)
                                latestSliderValue = it
                            },
                            valueRange = valueRange,
                            visibilityThreshold = 0.01f,
                            backdrop = backdrop,
                            enabled = enabled,
                            modifier = Modifier.weight(1f).padding(top = 5.dp)
                        )
                    }
                    if (valueText != null ) {
                        val theme = LocalPreferenceTheme.current
                        Box(modifier = Modifier.padding(start = theme.horizontalSpacing)) {
                            valueText()
                        }
                    }
                }
            }
        },
    )
}

