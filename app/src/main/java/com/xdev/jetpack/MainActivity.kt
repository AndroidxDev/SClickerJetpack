@file:OptIn(ExperimentalMaterial3Api::class)

package com.xdev.jetpack

import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xdev.jetpack.blurApp.CustomScaffold
import com.xdev.jetpack.blurApp.NewScreen
import com.xdev.jetpack.blurApp.datastore.DataStoreManager
import com.xdev.jetpack.ui.theme.JetpackTheme
import com.xdev.jetpack.utils.InfoCard
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState


object Navs {
    const val HOME = "Clicks"
    const val UPGRADE = "Upgrade"
    const val LIST = "List"
    val items = listOf(HOME, UPGRADE, LIST)
    val selectedIcons = listOf(
        Icons.Filled.Home, Icons.Filled.Upgrade,
        Icons.AutoMirrored.Filled.List
    )
    val unselectedIcons = listOf(
        Icons.Outlined.Home, Icons.Outlined.Upgrade,
        Icons.AutoMirrored.Outlined.List
    )
}

class MainActivity : ComponentActivity() {

    var clicks by mutableIntStateOf(0)
    var level by mutableIntStateOf(1)

    var price by mutableIntStateOf(10)

    var hazeState: HazeState = HazeState()

    // val items = listOf("Clicks", "Upgrade")

    private lateinit var sharedPreferences: SharedPreferences

    private val roundCorners: Dp = 60.dp

    private val versionName = BuildConfig.VERSION_NAME

    /*  companion object {
          lateinit var dataStoreManager: DataStoreManager
      }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sharedPreferences = this.getSharedPreferences("data", MODE_PRIVATE)

        clicks = sharedPreferences.getInt("clicks", 0)
        level = sharedPreferences.getInt("level", 1)
        price = sharedPreferences.getInt("price", 10)

        val isNavShow = sharedPreferences.getBoolean("navShow", true)

        if (!isNavShow) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            insetsController.apply {
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        val dataStoreManager = DataStoreManager(applicationContext)

        setContent {
            var colorValue by remember { mutableStateOf("default") }
            JetpackTheme(colorTheme = colorValue) {
                CustomScaffold(
                    onThemeChange = { colorValue = it },
                    navShow = { sharedPreferences.edit { putBoolean("navShow", it) } },
                    dataStoreManager
                )
                // NewScreen(onThemeChange = {colorValue = it}, navShow = { sharedPreferences.edit { putBoolean("navShow", it) } })

                // ScreenPreview()
                /*  UpgradeScreen(level, clicks, price) { clicksN, levelN, priceN ->
                      clicks = clicksN
                      level = levelN
                      price = priceN } */
            }
        }
    }


    fun saveData() {
        sharedPreferences.edit {
            putInt("clicks", clicks)
            putInt("level", level)
            putInt("price", price)
        }
    }

    //Preview function
    @Preview(
        showSystemUi = true, showBackground = true,
        wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    )
    @Composable
    fun ScreenPreview() {

        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.statusBars,
            topBar = {
                TopAppBar(
                    /*colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BottomAppBarDefaults.containerColor,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),*/
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    modifier = Modifier.hazeEffect(state = hazeState) {
                        // progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    },
                    /* modifier = Modifier
                         .clip(shape = RoundedCornerShape(roundCorners))
                         .padding(14.dp)
                         .background(MaterialTheme.colorScheme.surface),*/
                    title = {
                        Row(Modifier.padding(end = 15.dp)) {
                            Text("SClicker")
                            Spacer(modifier = Modifier.weight(1f))
                            Text("Ver: $versionName", fontSize = 15.sp)
                        }
                    }
                )
            },
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            //paddingValues -> NavHostContainer(navController, PaddingValues(top = paddingValues.calculateTopPadding()))
                paddingValues ->
            NavHostContainer(navController, paddingValues)

        }
    }

    @Composable
    fun NavHostContainer(navController: NavHostController, padding: PaddingValues) {
        hazeState = rememberHazeState()

        NavHost(
            navController = navController,
            startDestination = Navs.LIST,
            //  modifier = Modifier.padding(paddingValues = padding),
            builder = {
                composable(Navs.HOME) {
                    ClickerScreen()
                }
                composable(Navs.UPGRADE) {
                    UpgradeScreen(level, clicks, price) { clicksN, levelN, priceN ->
                        clicks = clicksN
                        level = levelN
                        price = priceN
                        saveData()
                    }
                }
                composable(Navs.LIST) {
                    //ListScreen {hazeState = it}

                    LazyColumn(
                        modifier = Modifier.hazeSource(state = hazeState)
                    ) {
                        // returnHazeState(hazeState)
                        items(20) { index ->
                            InfoCard("Card number $index", index)
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun ClickerScreen() {
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
                        saveData()
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

        var selectedItem by remember { mutableIntStateOf(0) }

        NavigationBar(
            modifier = Modifier
                .padding(14.dp) /*.background(
                shape = RoundedCornerShape(15.dp),
                color = MaterialTheme.colorScheme.surface.copy(1f)
            )*/
                .clip(shape = RoundedCornerShape(roundCorners))
                .background(Color.Transparent),
            /*    .graphicsLayer {
                    renderEffect = RenderEffect.createBlurEffect(
                        20f, // радиус blur по X
                        20f, // радиус blur по Y
                        Shader.TileMode.CLAMP
                    ).asComposeRenderEffect()
                },*/

            windowInsets = WindowInsets(0)
        ) {


            Navs.items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == index) Navs.selectedIcons[index] else Navs.unselectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(Navs.items[index])
                    }
                )
            }
        }
    }
}
