package com.xdev.jetpack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xdev.jetpack.utils.InfoCard


@Preview(showBackground = true)
@Composable
fun Preview() {
    UpgradeScreen(1, 1, 1) { _, _, _ -> }

}


@Composable
fun UpgradeScreen(levelValue: Int, clicksValue: Int, price: Int, newValues: (Int, Int, Int) -> Unit) {

    var localPrice by remember { mutableIntStateOf(price) }

    var localClicks by remember { mutableIntStateOf(clicksValue) }

    var localLevel by remember { mutableIntStateOf(levelValue) }

    val localWindowInfo = LocalWindowInfo.current

    //upgradeLogic
    val upgrade = {
        if (clicksValue >= price) {
            localClicks = localClicks - price
            localPrice = price * 2
            localLevel++
            newValues(localClicks, localLevel, localPrice)
        }
    }

    //design
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            InfoCard("Clicks", localClicks)
            InfoCard("Level", localLevel)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                 upgrade,
                modifier = Modifier.padding(bottom = 8.dp, end = 15.dp, start = 15.dp).width(localWindowInfo.containerSize.width.dp)
            ) {
                Text("Upgrade")
            }

            Text(
                text = "to upgrade need $localPrice clicks",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}