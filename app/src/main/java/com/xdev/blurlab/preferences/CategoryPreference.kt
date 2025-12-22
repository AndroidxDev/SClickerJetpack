package com.xdev.blurlab.preferences

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.xdev.blurlab.preferences.extensions.dataStore
import com.xdev.blurlab.ui.theme.LocalBlurLabThemeTheme
import kotlinx.coroutines.flow.map


fun LazyListScope.categoryPreference(
    key: String,
    title: String,
    modifier: Modifier = Modifier.padding(
        start = 16.dp,
        end = 15.dp,
        top = 0.dp,
        bottom = 15.dp
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    item(key = key, contentType = "Category") {
        val a = true
        val isIos26 by (LocalContext.current).dataStore.data.map {
            return@map it[booleanPreferencesKey("iosSettings")] ?: a
        }.collectAsState(initial = a)

        val currentColorScheme = LocalBlurLabThemeTheme.current.colorTheme

        val shape = RoundedCornerShape(if (!isIos26) 15.dp else 32.dp)
        val bg = if (isSystemInDarkTheme()) Color.Gray.copy(alpha = 0.2f) else {
            if (isIos26) {
                if (currentColorScheme == "dark" || currentColorScheme == "dynamic dark") {
                    Color.Gray.copy(alpha = 0.2f)
                } else Color.White
            } else {
                Color.Gray.copy(alpha = 0.2f)
            }
        }

    val showShadow = if (!isSystemInDarkTheme() && currentColorScheme != "dark" && currentColorScheme != "dynamic dark") Modifier.shadow(
            elevation = 0.5.dp,
            shape = shape
        ) else {
            Modifier
        }

        if (isIos26) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(
                    start = 35.dp,
                    end = 15.dp,
                    top = 0.dp,
                    bottom = 8.dp
                ),
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color(0xFFABABAB) else Color(0xFF8A8A8A),
                fontWeight = FontWeight.W700
            )
        }
        Column(
            if (!isIos26) {
                modifier
                    .fillMaxWidth()
                    .background(
                        color = bg,
                        shape = shape
                    )
            } else {

                modifier
                    .fillMaxWidth()
                    .then(showShadow)
                    .background(
                        color = bg,
                        shape = shape
                    )
            }
        ) {
            if (!isIos26) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 15.dp,
                        top = 15.dp,
                        bottom = 0.dp
                    ),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.W500
                )
            }
            content()
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Preview4() {
    //JetpackTheme(colorTheme = "red") {
    LazyColumn {
        categoryPreference(
            key = "asa",
            title = "MEGA TEXT"
        ) {
            SwitchPreference(
                value = true,
                title = { Text(text = "enable large top bar") },
                summary = { Text("enable large top bar in this app") },
                onValueChange = {}
            )
            SwitchPreference(
                value = true,
                title = { Text(text = "enable large top bar") },
                summary = { Text("enable large top bar in this app") },
                onValueChange = {}
            )
        }
    }
//}
}