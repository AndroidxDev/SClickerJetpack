package com.xdev.jetpack.blurApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xdev.jetpack.R
import com.xdev.jetpack.blurApp.preferences.CategoryPreference
import com.xdev.jetpack.blurApp.preferences.SliderPreference
import com.xdev.jetpack.blurApp.preferences.SwitchPreference
import com.xdev.jetpack.blurApp.preferences.listPreference
import com.xdev.jetpack.blurApp.preferences.sliderPreference
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import kotlin.math.roundToInt

object Nav {
    const val HOME = "home"
    const val SETTINGS = "settings"
    val items = listOf(HOME, SETTINGS)
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Settings)
}

@Composable
fun BottomNavBar(modifier: Modifier, navController: NavController, blurEnable: Boolean) {

    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar(
        modifier = modifier,
        if (blurEnable) {
            Color.Transparent
        } else {
            BottomAppBarDefaults.containerColor
        }
    ) {
        Nav.items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) Nav.selectedIcons[index] else Nav.unselectedIcons[index],
                        contentDescription = item,
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(Nav.items[index])
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TopBar(
    blurEnable: Boolean,
    gradientBlur: Boolean,
    isLarge: Boolean,
    hazeState: HazeState,
    blurStyle: HazeStyle,
    progress: (Float),
    blurRadius2: (Float),
    topBarHeight: (Int) -> Unit
) {

    val colors = if (blurEnable) {
        TopAppBarDefaults.topAppBarColors(Color.Transparent)
    } else {
        TopAppBarDefaults.topAppBarColors(
            containerColor = BottomAppBarDefaults.containerColor,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    val modifier = Modifier
        .hazeEffect(
            state = hazeState,
            style = blurStyle
        ) {
            if (gradientBlur) {
                progressive =
                    HazeProgressive.verticalGradient(
                        startIntensity = progress,
                        endIntensity = 0f
                    )
            }
            blurEnabled = blurEnable
            blurRadius = blurRadius2.dp
        }
        .onGloballyPositioned { topBarHeight(it.size.height) }

    val title = @Composable {
        Row(Modifier.padding(end = 15.dp)) {
            Text("SClicker")
            Spacer(modifier = Modifier.weight(1f))
            Text("Ver: Blur", fontSize = 15.sp)
        }
    }
    if (isLarge) {
        LargeTopAppBar(colors = colors, modifier = modifier, title = title)
    } else {
        TopAppBar(colors = colors, modifier = modifier, title = title)
    }
}

@Composable
fun CustomCard(index: Int) {
    val imagePainter = painterResource(R.drawable.ic_my_icon2)

    Card(
        modifier = Modifier
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .padding(5.dp)
            )
            Text(
                text = "card: $index",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = index.toString(),
                fontSize = 15.sp
            )
        }
    }

}


@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewScreen() {

    val hazeState = rememberHazeState()
    val navController = rememberNavController()
    val listState = rememberLazyListState()

    var topBarHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    var blurEnable by remember { mutableStateOf(true) }
    var gradientBlur by remember { mutableStateOf(false) }

    var blurRadius2 by remember { mutableFloatStateOf(20f) }

    var blurType by remember { mutableStateOf("Ultra Thin") }

    val blurStyle = when (blurType) {
        "Ultra Thin" -> HazeMaterials.ultraThin()
        "Thin" -> HazeMaterials.thin()
        "Regular" -> HazeMaterials.regular()
        "Thick" -> HazeMaterials.thick()
        "Ultra Thick" -> HazeMaterials.ultraThick()
        else -> HazeStyle.Unspecified
    }

    var isLarge by remember { mutableStateOf(false) }

    var progress by remember { mutableFloatStateOf(0f) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                modifier = Modifier.padding(),
                navController = navController,
                // startDestination = Nav.HOME,
                 startDestination = Nav.SETTINGS,
                builder = {
                    composable(Nav.HOME) {
                        LazyColumn(
                            modifier = Modifier.hazeSource(hazeState),
                            contentPadding = PaddingValues(top = with(density) { topBarHeight.toDp() }),
                            state = listState
                        ) {
                            items(40, key = { it }) { index ->
                                CustomCard(index)
                            }
                        }
                    }
                    composable(Nav.SETTINGS) {
                        ProvidePreferenceLocals {
                            LazyColumn(
                                modifier = Modifier
                                .hazeSource(state = hazeState),
                                contentPadding = PaddingValues(top = with(density) { topBarHeight.toDp() })
                            ) {

                                item {
                                    CategoryPreference("Blur settings") {
                                        SwitchPreference(
                                            value = blurEnable,
                                            title = { Text(text = "Blur enable") },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text("Enable blur with this app") },
                                            onValueChange = { newValue ->
                                                blurEnable = newValue
                                            }
                                        )

                                        sliderPreference(
                                            key = "slider_preference2",
                                            enabled = { blurEnable },
                                            defaultValue = 20f,
                                            title = { Text(text = "Blur radius") },
                                            valueRange = 1f..150f,
                                            //valueSteps = 9,
                                            summary = { Text(text = "change blur radius") },
                                            valueText = {
                                                Text(text = ((it / 0.5f).roundToInt() * 0.5f).toString())
                                            },
                                            onValueChange = {
                                                blurRadius2 = (it)
                                            }
                                        )

                                        listPreference(
                                            enabled = { blurEnable },
                                            key = "aa",
                                            onValueChange = {
                                                blurType = it
                                            },
                                            defaultValue = blurType,
                                            values = listOf(
                                                "Ultra Thin",
                                                "Thin",
                                                "Regular",
                                                "Thick",
                                                "Ultra Thick"
                                            ),
                                            title = { Text("BlurType") },
                                            summary = { Text(text = it) }
                                        )
                                    }
                                }

                                item {
                                    CategoryPreference("Gradient blur") {
                                        SwitchPreference(
                                            value = if (blurEnable) gradientBlur else false,
                                            title = { Text(text = "Gradient blur") },
                                            summary = { Text("Enable gradient blur on top bar") },
                                            onValueChange = { newValue ->
                                                gradientBlur = newValue
                                            },
                                            enabled = blurEnable
                                        )


                                        sliderPreference(
                                            key = "slider_preference",
                                            enabled = { blurEnable and gradientBlur },
                                            defaultValue = 50f,
                                            title = { Text(text = "Blur progress2") },
                                            valueRange = 1f..100f,
                                            //valueSteps = 9,
                                            summary = { Text(text = "change blur progress on topbar (only for gradient)") },
                                            valueText = { Text(text = ((progress / 0.05f).roundToInt() * 0.05f).toString()) },
                                            onValueChange = {
                                                progress = (it / 100f)
                                            }
                                        )
                                    }
                                }

                                item {
                                    CategoryPreference("Top Bar") {
                                        SwitchPreference(
                                            value = isLarge,
                                            title = { Text(text = "enable large top bar") },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text("enable large top bar in this app") },
                                            onValueChange = { isLarge = it }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )

            TopBar(blurEnable, gradientBlur, isLarge, hazeState, blurStyle, progress, blurRadius2) {
                topBarHeight = it
            }

            BottomNavBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .hazeEffect(
                        state = hazeState,
                        style = blurStyle
                    ) {
                        blurEnabled = blurEnable
                        blurRadius = blurRadius2.dp
                      },
                navController,
                blurEnable
            )

        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview3() {
    NewScreen()
}