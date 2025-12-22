package com.xdev.jetpack.blurApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import com.xdev.jetpack.R
import com.xdev.jetpack.blurApp.datastore.DataStoreManager
import com.xdev.jetpack.blurApp.datastore.SettingsData
import com.xdev.jetpack.blurApp.liquidglass.components.LiquidBottomTab
import com.xdev.jetpack.blurApp.liquidglass.components.LiquidBottomTabs
import com.xdev.jetpack.blurApp.preferences.SwitchPreference
import com.xdev.jetpack.blurApp.preferences.categoryPreference
import com.xdev.jetpack.blurApp.preferences.extensions.dataStore
import com.xdev.jetpack.blurApp.preferences.ListPreference
import com.xdev.jetpack.blurApp.preferences.SliderPreference
import com.xdev.jetpack.ui.theme.JetpackTheme
import com.xdev.jetpack.ui.theme.LocalJetpackTheme
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
fun BottomNavBar(
    modifier: Modifier,
    navController: NavController,
    blurEnable: Boolean,
    backdrop: LayerBackdrop,
    liquidglassbar: Boolean,
    hazeState: HazeState,
    blurStyle: HazeStyle,
    blurRadius2: Float,
    bottomBarHeight: (Int) -> Unit
) {

    var selectedItem by remember { mutableIntStateOf(0) }
    val modifier2 =
        if (liquidglassbar) {
            Modifier.hazeEffect(
                state = hazeState,
                style = blurStyle
            ) {
                progressive =
                    HazeProgressive.verticalGradient(
                        startIntensity = 0f,
                        endIntensity = 0.5f
                    )

                blurEnabled = blurEnable
                blurRadius = 15f.dp
            }
        } else { Modifier }

    if (liquidglassbar) {

        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .then(modifier2)
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        bottomBarHeight(it.size.height)
                    }
            ) {
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .safeContentPadding()
                ) {
                    val contentColor = if (!isSystemInDarkTheme()) Color.Black else Color.White

                    LiquidBottomTabs(
                        selectedTabIndex = { selectedItem },
                        onTabSelected = {
                            selectedItem = it
                            navController.navigate(Nav.items[it]) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        backdrop = backdrop,
                        tabsCount = 2,
                        modifier = Modifier.padding(horizontal = 36f.dp)
                    ) {
                        Nav.items.forEachIndexed { index, item ->
                            LiquidBottomTab({
                                selectedItem = index
                                navController.navigate(Nav.items[index]) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }) {
                                Icon(
                                    if (selectedItem == index) Nav.selectedIcons[index] else Nav.unselectedIcons[index],
                                    contentDescription = item,
                                    modifier = Modifier.size(24f.dp),
                                    tint = contentColor
                                )
                                BasicText(
                                    item,
                                    style = TextStyle(contentColor, 12f.sp)
                                )
                            }
                        }
                    }
                }
            }
        }

    } else {
        NavigationBar(
            modifier = modifier2
                .then(modifier)
                /*  .hazeEffect(
                      state = hazeState,
                      style = blurStyle
                  ) {
                      blurEnabled = blurEnable
                      blurRadius = blurRadius2.dp
                  }*/
                .onGloballyPositioned {
                    bottomBarHeight(it.size.height)
                },
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
                        navController.navigate(Nav.items[index]) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
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
    backdrop: LayerBackdrop,
    liquidTopBar: Boolean,
    topBarHeight: (Int) -> Unit
) {

    val modifier = Modifier
        .hazeEffect(
            state = hazeState,
            style = blurStyle
        ) {
            progressive = if (gradientBlur or liquidTopBar) {
                HazeProgressive.verticalGradient(
                    startIntensity = if (!liquidTopBar) progress else 0.5f,
                    endIntensity = 0f
                )
            } else {
                null
            }

            blurEnabled = blurEnable
            blurRadius = if (!liquidTopBar) blurRadius2.dp else 15f.dp
        }
        .onGloballyPositioned { topBarHeight(it.size.height) }

    if (!liquidTopBar) {
        val colors = if (blurEnable) {
            TopAppBarDefaults.topAppBarColors(Color.Transparent)
        } else {
            TopAppBarDefaults.topAppBarColors(
                containerColor = BottomAppBarDefaults.containerColor,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
        val title = @Composable {
            Row(Modifier.padding(end = 15.dp)) {
                Text("SClicker")
                Spacer(modifier = Modifier.weight(1f))
                Text("Ver: Blur", fontSize = 15.sp)
            }
        }

        if (!isLarge) {
            TopAppBar(title = title, modifier = modifier, colors = colors)
        } else {
            LargeTopAppBar(title = title, modifier = modifier, colors = colors)
        }
    } else {

        val color =
            if (!isSystemInDarkTheme() && (LocalJetpackTheme.current.colorTheme != "dark" || LocalJetpackTheme.current.colorTheme != "dynamic dark")) Color.White.copy(
                alpha = 0.5f
            ) else Color.DarkGray.copy(alpha = 0.5f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 35.dp, end = 15.dp)
                    .onGloballyPositioned { topBarHeight((it.size.height) + 70) }
            ) {
                Box(
                    Modifier
                        //.safeContentPadding()
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { ContinuousCapsule },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = { drawRect(color) }
                        )
                        .padding(top = 9.dp, bottom = 9.dp, end = 15.dp, start = 15.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text("SClicker", style = MaterialTheme.typography.titleLarge)
                }

                Box(
                    Modifier
                        //.safeContentPadding()
                        .drawBackdrop(
                            backdrop = backdrop,
                            shape = { ContinuousCapsule },
                            effects = {
                                vibrancy()
                                blur(2f.dp.toPx())
                                lens(16f.dp.toPx(), 32f.dp.toPx())
                            },
                            onDrawSurface = { drawRect(color) }
                        )
                        .padding(top = 9.dp, bottom = 9.dp, end = 15.dp, start = 15.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text("Ver: Blur", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomScaffold(
    onThemeChange: (String) -> Unit,
    navShow: (Boolean) -> Unit,
    dataStoreManager: DataStoreManager
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        NewScreen(
            onThemeChange = { onThemeChange(it) },
            navShow = { navShow(it) },
            scope,
            snackBarHostState,
            dataStoreManager
        )
    }
}

@Composable
fun Divider() {
    val a = true
    val isIos26 by (LocalContext.current).dataStore.data.map {
        return@map it[booleanPreferencesKey("iosSettings")] ?: a
    }.collectAsState(initial = a)
    if (isIos26) {
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            color = Color(0x41ABABAB),
            thickness = 2.dp
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewScreen(
    onThemeChange: (String) -> Unit,
    navShow: (Boolean) -> Unit,
    scope: CoroutineScope,
    snackBarState: SnackbarHostState,
    dataStoreManager: DataStoreManager
) {

    val settings by dataStoreManager.getAll().collectAsState(
        SettingsData(
            blurRadius2 = 20f,
            blurEnable = true,
            blurType = "Ultra Thin",
            themeType = "default",
            gradientBlur = false,
            progress = 0.5f,
            isLarge = false,
            navShow = true,
            liquidglassbar = false,
            liquidTopBar = false,
            liquidSwitch = false,
            liquidSlider = false,
            liquidDialog = false,
            iosSettings = false
        )
    )

    LaunchedEffect(settings.themeType) {
        onThemeChange(settings.themeType)
    }


    // Locals
    val density = LocalDensity.current
    val context = LocalContext.current
    // States
    val hazeState = rememberHazeState()
    val navController = rememberNavController()
    val listState = rememberLazyListState()

    // Blur
    var blurRadius2 = settings.blurRadius2
    var blurEnable = settings.blurEnable

    // BlurType
    var blurType = settings.blurType
    val blurStyle = when (blurType) {
        "Ultra Thin" -> HazeMaterials.ultraThin()
        "Thin" -> HazeMaterials.thin()
        "Regular" -> HazeMaterials.regular()
        "Thick" -> HazeMaterials.thick()
        "Ultra Thick" -> HazeMaterials.ultraThick()
        else -> HazeStyle.Unspecified
    }

    //theme
    var themeType = settings.themeType

    // GradientBlur
    var gradientBlur = settings.gradientBlur
    var progress = settings.progress

    // TopBar
    var isLarge = settings.isLarge
    var topBarHeight by remember { mutableIntStateOf(0) }
    var bottomBarHeight by remember { mutableIntStateOf(0) }

    // Nav
    var navShow = settings.navShow

    // Luiquidglass
    var liquidglassbar = settings.liquidglassbar
    var liquidTopBar = settings.liquidTopBar
    var liquidSwitch = settings.liquidSwitch
    var liquidSlider = settings.liquidSlider
    var liquidDialog = settings.liquidDialog

    var iosSettings = settings.iosSettings


    val backgroundColor = MaterialTheme.colorScheme.background
    val backdrop = rememberLayerBackdrop {
        drawRect(backgroundColor)
        drawContent()
    }

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
                //startDestination = Nav.HOME,
                startDestination = Nav.SETTINGS,
                builder = {
                    composable(Nav.HOME) {
                        LazyColumn(
                            modifier = Modifier
                                .hazeSource(hazeState)
                                .layerBackdrop(backdrop),
                            contentPadding = PaddingValues(
                                top = with(density) { (topBarHeight + 30).toDp() },
                                bottom = with(density) { (bottomBarHeight).toDp() }),
                            state = listState
                        ) {
                            items(40, key = { it }) { index ->
                                CustomCard(index)
                            }
                        }
                    }

                    composable(Nav.SETTINGS) {
                        val blurSettings = stringResource(R.string.blur_settings)
                        val gradientBlurT = stringResource(R.string.gradient_blur)
                        val topBar = stringResource(R.string.top_bar)
                        val appSettings = stringResource(R.string.app_settings)
                        val liquidglass = stringResource(R.string.liquid_glass_settings)

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            ProvidePreferenceLocals {
                                LazyColumn(
                                    modifier = Modifier
                                        .hazeSource(state = hazeState)
                                        .layerBackdrop(backdrop),
                                    contentPadding = PaddingValues(
                                        top = with(density) { (topBarHeight + 15 * 2).toDp() },
                                        bottom = with(density) { (bottomBarHeight + 30).toDp() }),
                                ) {
                                    categoryPreference("bs", blurSettings) {
                                        SwitchPreference(
                                            value = blurEnable,
                                            title = { Text(text = stringResource(R.string.blur_enable)) },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text(stringResource(R.string.enable_blur_with_this_app)) },
                                            onValueChange = { newValue ->
                                                blurEnable = newValue

                                                scope.launch {
                                                    dataStoreManager.saveBlurEnable(newValue)
                                                }
                                            }
                                        )
                                        Divider()
                                        SliderPreference(
                                            key = "slider_preference2",
                                            enabled = { blurEnable },
                                            defaultValue = 20f,
                                            title = { Text(text = stringResource(R.string.blur_radius)) },
                                            valueRange = 1f..150f,
                                            //valueSteps = 9,
                                            summary = { Text(text = stringResource(R.string.change_blur_radius)) },
                                            valueText = {
                                                Text(text = ((it / 0.5f).roundToInt() * 0.5f).toString())
                                            },
                                            onValueChange = {
                                                blurRadius2 = (it)

                                                scope.launch {
                                                    dataStoreManager.saveBlurRadius(it)
                                                }
                                            }
                                        )
                                        Divider()
                                        ListPreference(
                                            enabled = { blurEnable },
                                            key = "aa",
                                            onValueChange = {
                                                blurType = it

                                                scope.launch {
                                                    dataStoreManager.saveBlurType(it)
                                                }
                                            },
                                            defaultValue = blurType,
                                            values = listOf(
                                                "Ultra Thin",
                                                "Thin",
                                                "Regular",
                                                "Thick",
                                                "Ultra Thick"
                                            ),
                                            title = stringResource(R.string.blurtype),
                                            summary = { Text(text = it) },
                                            backdrop = backdrop
                                        )
                                    }

                                    categoryPreference("gb", gradientBlurT) {
                                        SwitchPreference(
                                            value = gradientBlur and !liquidTopBar,
                                            title = { Text(text = stringResource(R.string.gradient_blur)) },
                                            summary = { Text(stringResource(R.string.enable_gradient_blur_on_top_bar)) },
                                            onValueChange = { newValue ->
                                                gradientBlur = newValue

                                                scope.launch {
                                                    dataStoreManager.saveGradientBlur(newValue)
                                                }
                                            },
                                            enabled = blurEnable and !liquidTopBar
                                        )
                                        Divider()
                                        SliderPreference(
                                            key = "slider_preference",
                                            enabled = { (blurEnable and gradientBlur) and !liquidTopBar },
                                            defaultValue = 0.5f,
                                            title = { Text(text = stringResource(R.string.blur_progress)) },
                                            valueRange = 0.01f..1f,
                                            summary = { Text(text = stringResource(R.string.change_blur_progress_on_topbar_only_for_gradient)) },
                                            valueText = { Text(text = ((progress / 0.05f).roundToInt() * 0.05f).toString()) },
                                            onValueChange = {
                                                progress = (it)

                                                scope.launch {
                                                    dataStoreManager.saveProgress(it)
                                                }
                                            }
                                        )
                                    }
                                    categoryPreference("tb", topBar) {
                                        SwitchPreference(
                                            value = isLarge,
                                            title = { Text(text = stringResource(R.string.enable_large_top_bar)) },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text(stringResource(R.string.enable_large_top_bar_in_this_app)) },
                                            onValueChange = {
                                                isLarge = it

                                                scope.launch {
                                                    dataStoreManager.saveIsLarge(it)
                                                }
                                            },
                                            enabled = !liquidTopBar
                                        )
                                    }

                                    categoryPreference("app", appSettings) {
                                        ListPreference(
                                            enabled = { true },
                                            key = "1212",
                                            onValueChange = {
                                                onThemeChange(it)
                                                themeType = it

                                                scope.launch {
                                                    dataStoreManager.saveThemeType(it)
                                                }
                                            },
                                            defaultValue = themeType,
                                            values = listOf(
                                                "default",
                                                "dynamic dark",
                                                "dynamic light",
                                                "dark",
                                                "light",
                                                "red",
                                                "yellow"
                                            ),
                                            title = stringResource(R.string.theme_color),
                                            summary = { Text(text = it) },
                                            backdrop = backdrop
                                        )
                                        Divider()
                                        SwitchPreference(
                                            value = navShow,
                                            title = { Text(text = "Show NavBar") },
                                            summary = { Text("Show navigation bar line") },
                                            onValueChange = {
                                                navShow = it
                                                navShow(it)

                                                //need restart
                                                scope.launch {
                                                    val result = snackBarState
                                                        .showSnackbar(
                                                            message = "Restart app to apply changes",
                                                            actionLabel = "restart",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    if (result == SnackbarResult.ActionPerformed) {
                                                        restartApp(context)
                                                    }

                                                    dataStoreManager.saveNavShow(it)
                                                }
                                            }
                                        )
                                    }

                                    categoryPreference("liquidglass", liquidglass) {
                                        SwitchPreference(
                                            value = liquidglassbar,
                                            title = { Text(text = stringResource(R.string.liquid_bottom_bar)) },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text(stringResource(R.string.enable_liquid_navigation_bottom_bar_ios_like)) },
                                            onValueChange = {
                                                liquidglassbar = it

                                                scope.launch {
                                                    dataStoreManager.saveLiquidBar(it)
                                                }
                                            }
                                        )
                                        Divider()
                                        SwitchPreference(
                                            value = liquidTopBar,
                                            title = { Text(text = stringResource(R.string.liquid_top_bar)) },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text(stringResource(R.string.enable_liquid_top_bar_ios_like)) },
                                            onValueChange = {
                                                liquidTopBar = it

                                                scope.launch {
                                                    dataStoreManager.saveLiquidTopBar(it)
                                                }
                                            }
                                        )
                                        Divider()
                                        SwitchPreference(
                                            value = liquidSwitch,
                                            title = { Text(text = stringResource(R.string.liquid_switch)) },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text(stringResource(R.string.enable_liquid_switch_ios_like)) },
                                            onValueChange = {
                                                liquidSwitch = it

                                                scope.launch {
                                                    dataStoreManager.saveLiquidSwitch(it)
                                                }
                                            }
                                        )
                                        Divider()
                                        SwitchPreference(
                                            value = liquidSlider,
                                            title = { Text(text = "Liquid Slider") },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text("Enable liquid slider (IOS like)") },
                                            onValueChange = {
                                                liquidSlider = it

                                                scope.launch {
                                                    dataStoreManager.saveLiquidSlider(it)
                                                }
                                            }
                                        )
                                        Divider()
                                        SwitchPreference(
                                            value = liquidDialog,
                                            title = { Text(text = "Liquid Dialog") },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text("Enable liquid Dialog (IOS like)") },
                                            onValueChange = {
                                                liquidDialog = it

                                                scope.launch {
                                                    dataStoreManager.saveLiquidDialog(it)
                                                }
                                            }
                                        )
                                        Divider()
                                        SwitchPreference(
                                            value = iosSettings,
                                            title = { Text(text = "IOS Settings Style") },
                                            //icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
                                            summary = { Text("Enable IOS settings style") },
                                            onValueChange = {
                                                iosSettings = it

                                                scope.launch {
                                                    dataStoreManager.saveIosSettings(it)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )

            TopBar(
                blurEnable,
                gradientBlur,
                isLarge,
                hazeState,
                blurStyle,
                progress,
                blurRadius2,
                backdrop,
                liquidTopBar
            ) {
                topBarHeight = it
            }

            BottomNavBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController,
                blurEnable,
                backdrop,
                liquidglassbar,
                hazeState,
                blurStyle,
                blurRadius2
            ) {
                bottomBarHeight = it
            }
        }
    }
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    Runtime.getRuntime().exit(0)
}


@Preview(showSystemUi = true, showBackground = true, locale = "ru")
@Composable
fun Preview3() {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    JetpackTheme(colorTheme = "dark ", darkTheme = true) {
        NewScreen(
            { "dynamic" },
            { true },
            scope,
            snackBarHostState,
            DataStoreManager(LocalContext.current)
        )
    }
}