package com.xdev.jetpack.blurApp.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoryPreference(
    title: String,
    modifier: Modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    // ProvideTextStyle(value = MaterialTheme.typography.bodyLarge, content = title)
    Text(
        title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(
            start = 16.dp,
            end = 15.dp,
            top = 8.dp,
            bottom = 8.dp
        )
    )
    Column(
        modifier
            .fillMaxWidth()
            .background(
                color = Color.Gray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(25.dp)
            )
    ) {
        content()
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun Preview4() {
    LazyColumn {
        item {
            CategoryPreference(
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
    }
}