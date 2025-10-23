@file:OptIn(ExperimentalMaterial3Api::class)

package com.xdev.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Upgrade
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xdev.jetpack.ui.theme.JetpackTheme

class MainActivity : ComponentActivity() {

    var clicks by mutableIntStateOf(0)
    var level by mutableIntStateOf(1)

    val items = listOf("Clicks", "Upgrade")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackTheme {
              ScreenPreview()
            }
        }
    }

    //Preview function
    @Preview(showSystemUi = false, showBackground = true,
        wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE
    )
    @Composable
    fun ScreenPreview(){
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BottomAppBarDefaults.containerColor,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    title = {
                        Text("SClicker")
                    }
                )
            },
            bottomBar = { BottomNavigationBar(navController) }
        ) {
                paddingValues -> NavHostContainer(navController, paddingValues)
        }
    }

    @Composable
    fun NavHostContainer(navController: NavHostController, padding: PaddingValues) {
        NavHost(
            navController = navController,
            startDestination = items[0],
            modifier = Modifier.padding(paddingValues = padding),
            builder = {
                composable(items[0]){
                    ClickerScreen()
                }
                composable(items[1]){
                    UpgradeScreen(level, clicks) { clicksN, levelN ->
                        clicks = clicksN
                        level = levelN
                    }
                }
            }
        )
    }

    @Composable
    fun ClickerScreen(){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = clicks.toString()
                )

                Button(
                    {
                        if (clicks == 0) clicks++ else clicks = clicks + (1 * level)
                    },
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Text(
                        text = stringResource(R.string.clickBtn)
                    )
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        NavigationBar {
            var selectedItem by remember { mutableIntStateOf(0)}
            val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Upgrade)
            val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Upgrade)
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item,
                            )
                        },
                        label = { Text(item)},
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(items[index])
                        }
                    )
                }
            }
        }
    }
}
