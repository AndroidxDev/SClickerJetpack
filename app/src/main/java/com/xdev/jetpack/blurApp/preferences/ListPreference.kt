package com.xdev.jetpack.blurApp.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.xdev.jetpack.blurApp.preferences.extensions.copy
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.LocalPreferenceTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.rememberPreferenceState


object ListPreferenceDefaults {
    fun <T> item(
        valueToText: (T) -> AnnotatedString,
    ): @Composable (value: T, currentValue: T, onClick: () -> Unit) -> Unit =
        { value, currentValue, onClick ->
            DialogItem(value, currentValue, valueToText, onClick)
        }
}
@Composable
fun <T> listPreference(
    key: String,
    onValueChange: (T) -> Unit,
    defaultValue: T,
    values: List<T>,
    title: @Composable (T) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    rememberState: @Composable () -> MutableState<T> = {
        rememberPreferenceState(key, defaultValue)
    },
    enabled: (T) -> Boolean = { true },
    icon: @Composable ((T) -> Unit)? = null,
    summary: @Composable ((T) -> Unit)? = null,
    type: ListPreferenceType = ListPreferenceType.ALERT_DIALOG,
    valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    item: @Composable (value: T, currentValue: T, onClick: () -> Unit) -> Unit =
        ListPreferenceDefaults.item(valueToText),
) {
        val state = rememberState()
        val value by state
        ListPreference(
            onValueChange = { newV -> onValueChange(newV) },
            state = state,
            values = values,
            title = { title(value) },
            modifier = modifier,
            enabled = enabled(value),
            icon = icon?.let { { it(value) } },
            summary = summary?.let { { it(value) } },
            type = type,
            valueToText = valueToText,
            item = item,
        )
}

@Composable
fun <T> DialogItem(
    value: T,
    currentValue: T,
    valueToText: (T) -> AnnotatedString,
    onClick: () -> Unit,
) {
    val selected = value == currentValue
    Row(
        modifier =
            Modifier.fillMaxWidth()
                .heightIn(min = 48.dp)
                .selectable(selected = selected, enabled = true, role = Role.RadioButton, onClick = onClick)
                .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = valueToText(value),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}



@Composable
fun <T> ListPreference(
    value: T,
    onValueChange: (T) -> Unit,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    type: ListPreferenceType = ListPreferenceType.ALERT_DIALOG,
    valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    item: @Composable (value: T, currentValue: T, onClick: () -> Unit) -> Unit =
        ListPreferenceDefaults.item(valueToText),
) {
    var openSelector by rememberSaveable { mutableStateOf(false) }
    // Put DropdownMenu before Preference so that it can anchor to the right position.
    if (openSelector) {
        when (type) {
            ListPreferenceType.ALERT_DIALOG -> {
                PreferenceAlertDialog(
                    onDismissRequest = { openSelector = false },
                    title = title,
                    buttons = {
                        TextButton(onClick = { openSelector = false }) {
                            Text(text = "Cancel")
                        }
                    },
                ) {
                    val lazyListState = rememberLazyListState()
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().verticalScrollIndicators(lazyListState),
                        state = lazyListState,
                    ) {
                        items(values) { itemValue ->
                            item(itemValue, value) {
                                onValueChange(itemValue)
                                openSelector = false
                            }
                        }
                    }
                }
            }
            ListPreferenceType.DROPDOWN_MENU -> {
                val theme = LocalPreferenceTheme.current
                Box(
                    modifier = Modifier.fillMaxWidth().padding(theme.padding.copy(vertical = 0.dp))
                ) {
                    DropdownMenu(
                        expanded = openSelector,
                        onDismissRequest = { openSelector = false },
                    ) {
                        for (itemValue in values) {
                            item(itemValue, value) {
                                onValueChange(itemValue)
                                openSelector = false
                            }
                        }
                    }
                }
            }
        }
    }
    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
    ) {
        openSelector = true
    }
}


@Composable
fun <T> ListPreference(
    onValueChange: (T) -> Unit,
    state: MutableState<T>,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    type: ListPreferenceType = ListPreferenceType.ALERT_DIALOG,
    valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    item: @Composable (value: T, currentValue: T, onClick: () -> Unit) -> Unit =
        ListPreferenceDefaults.item(valueToText)
) {
    var value by state
    ListPreference(
        value = value,
        onValueChange = { newV -> onValueChange(newV) },
        values = values,
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
        type = type,
        valueToText = valueToText,
        item = item
    )
}