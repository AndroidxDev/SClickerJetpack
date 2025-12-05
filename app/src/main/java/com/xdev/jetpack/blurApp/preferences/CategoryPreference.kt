package com.xdev.jetpack.blurApp.preferences

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


fun LazyListScope.categoryPreference(
    key: String,
    title: String,
    modifier: Modifier = Modifier.padding(start = 16.dp,
        end = 15.dp,
        top = 0.dp,
        bottom = 15.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    item(key = key, contentType = "Category") {
        Column(
            modifier
                .fillMaxWidth()
                .background(
                    color = Color.Gray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 15.dp,
                    top = 15.dp,
                    bottom = 0.dp
                ),
                fontSize = 16.sp,
              //  color = ((WallpaperManager.getInstance(LocalContext.current)).getWallpaperColors(WallpaperManager.FLAG_SYSTEM))?.primaryColor?.toComposeColor() ?: Color.White,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W500
            )

            content()
        }
    }
}

@Preview(showSystemUi = true,
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