package com.xdev.blurlab.preferences

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.xdev.blurlab.liquidglass.components.LiquidToggle
import com.xdev.blurlab.preferences.extensions.dataStore
import kotlinx.coroutines.flow.map
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

                val backgroundColor = MaterialTheme.colorScheme.background
                val backdrop = rememberLayerBackdrop {
                    drawRect(backgroundColor)
                    drawContent()
                }

                val a = true
                val liquidSwitch by (LocalContext.current).dataStore.data.map {
                    return@map it[booleanPreferencesKey("liquidSwitch")] ?: a
                }.collectAsState(initial = a)

                if (!liquidSwitch) {
                    Switch(
                        checked = value,
                        onCheckedChange = null,
                        modifier = Modifier.padding(end = theme.horizontalSpacing),
                        enabled = enabled,
                    )
                } else {
                    LiquidToggle(
                        selected = { value },
                        onSelect = { onValueChange(it) },
                        backdrop = backdrop,
                        enabled = enabled,
                        modifier = Modifier.padding(end = theme.horizontalSpacing)
                    )
                }
            },
        )
    }
}