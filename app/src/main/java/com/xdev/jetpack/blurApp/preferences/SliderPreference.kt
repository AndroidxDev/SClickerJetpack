package com.xdev.jetpack.blurApp.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.zhanghai.compose.preference.LocalPreferenceTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.rememberPreferenceState

@Composable
inline fun sliderPreference(
    key: String,
    defaultValue: Float,
    crossinline title: @Composable (Float) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    crossinline rememberState: @Composable () -> MutableState<Float> = {
        rememberPreferenceState(key, defaultValue)
    },
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    crossinline rememberSliderState: @Composable (Float) -> MutableFloatState = {
        remember { mutableFloatStateOf(it) }
    },
    crossinline enabled: (Float) -> Boolean = { true },
    noinline icon: @Composable ((Float) -> Unit)? = null,
    noinline summary: @Composable ((Float) -> Unit)? = null,
    noinline valueText: @Composable ((Float) -> Unit)? = null,
    crossinline onValueChange: (Float) -> Unit
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

    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = {
            Column {
                summary?.invoke()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var latestSliderValue = sliderValue
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
                    if (valueText != null) {
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

