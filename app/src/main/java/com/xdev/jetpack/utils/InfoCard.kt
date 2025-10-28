package com.xdev.jetpack.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Preview(showBackground = true)
@Composable
fun Preview() {
    InfoCard("title", 123456789)
   // CenterCardAnimation()
}


@Composable
fun InfoCard(title: String, value: Int) {
    Box(
        modifier = Modifier
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = value.toString(),
                fontSize = 15.sp
            )
        }
    }
}
/*
@Composable
fun CenterCardAnimation() {
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    val density = LocalDensity.current
    var availableHeightPx by remember { mutableFloatStateOf(0f) }
    var centerYPx by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                availableHeightPx = it.size.height.toFloat()
                centerYPx = availableHeightPx / 2f
            }
    ) {
        val baseWidth = 180.dp
        val baseHeight = 100.dp
        val spacing = 36.dp

        val baseHeightPx = with(density) { baseHeight.toPx() }
        val spacingPx = with(density) { spacing.toPx() }

        val offsetsYPx = listOf(
            -baseHeightPx - spacingPx,
            0f,
            baseHeightPx + spacingPx
        )

        (0..3).forEach { index ->
            val targetYPx =
                if (expandedIndex == index) 0f else offsetsYPx[index]

            val animYPx by animateFloatAsState(targetValue = targetYPx, label = "")
            val animScale by animateFloatAsState(
                targetValue = if (expandedIndex == index) 1.6f else 1f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = centerYPx + animYPx - baseHeightPx / 2
                        scaleX = animScale
                        scaleY = animScale
                    }
                    .zIndex(if (expandedIndex == index) 1f else 0f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(baseWidth, baseHeight)
                        .background(
                            color = when (index) {
                                0 -> Color(0xFF6200EE)
                                1 -> Color(0xFF03DAC5)
                                else -> Color(0xFFFF5722)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            expandedIndex = if (expandedIndex == index) null else index
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Карточка ${index + 1}", color = Color.White)
                }
            }
        }
    }
}
*/