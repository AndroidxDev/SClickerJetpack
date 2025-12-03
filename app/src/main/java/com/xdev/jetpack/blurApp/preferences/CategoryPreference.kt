package com.xdev.jetpack.blurApp.preferences

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun LazyListScope.categoryPreference(
    key: String,
    title: String,
    modifier: Modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    item(key = key, contentType = "Category") {
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 15.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
            fontSize = 17.sp
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
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun Preview4() {
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
}