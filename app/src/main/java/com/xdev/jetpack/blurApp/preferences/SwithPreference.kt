package com.xdev.jetpack.blurApp.preferences

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import me.zhanghai.compose.preference.LocalPreferenceTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@Composable
fun SwitchPreference(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
) {
    ProvidePreferenceLocals {
        Preference(
            title = title,
            modifier = modifier.toggleable(
                value = value,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onValueChange
            ),
            enabled = enabled,
            icon = icon,
            summary = summary,
            widgetContainer = {
                val theme = LocalPreferenceTheme.current
                Switch(
                    checked = value,
                    onCheckedChange = null,
                    modifier = Modifier.padding(end = theme.horizontalSpacing),
                    enabled = enabled,
                )
            },
        )
    }
}