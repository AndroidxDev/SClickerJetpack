package com.xdev.jetpack

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xdev.jetpack.utils.InfoCard
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun ListScreen(returnHazeState: (HazeState) -> Unit) {

    val hazeState = rememberHazeState()

    LazyColumn(
        modifier = Modifier.hazeSource(state = hazeState)
    ) {
        returnHazeState(hazeState)
        items(20) { index ->
            InfoCard("Card number $index", index)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListScreen() {
    ListScreen {_ ->}
}