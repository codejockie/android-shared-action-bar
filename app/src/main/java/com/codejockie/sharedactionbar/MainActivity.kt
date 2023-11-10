package com.codejockie.sharedactionbar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codejockie.sharedactionbar.ui.theme.SharedActionBarTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SharedActionBarTheme {
                val navController = rememberNavController()
                val appBarState = rememberAppBarState(navController)
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (appBarState.isVisible) {
                            PlaygroundTopAppBar(
                                appBarState = appBarState,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeRoute,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(
                            route = HomeRoute,
                        ) {
                            HomeScreen(
                                appBarState = appBarState,
                                onSettingsClick = { navController.navigate(SettingsRoute) },
                                toNoAppBarScreen = { navController.navigate(NoAppBarRoute) },
                                toManyOptionsScreen = { navController.navigate(ManyOptionsRoute) },
                                modifier = Modifier
                                    .fillMaxSize(),
                                onLangChange = {
                                    AppCompatDelegate.setApplicationLocales(
                                        LocaleListCompat.forLanguageTags(it)
                                    )
                                }
                            )
                        }
                        composable(
                            route = ManyOptionsRoute,
                        ) {
                            ManyOptionsScreen(
                                appBarState = appBarState,
                                snackbarHostState = snackbarHostState,
                                onBackClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        composable(
                            route = SettingsRoute,
                        ) {
                            SettingsScreen(
                                appBarState = appBarState,
                                onBackClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        composable(
                            route = NoAppBarRoute,
                        ) {
                            NoAppBarScreen(
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundTopAppBar(
    appBarState: AppBarState,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var addDropMenu by remember { mutableStateOf(false) }
    var listDropMenu by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        navigationIcon = {
            val icon = appBarState.navigationIcon
            val callback = appBarState.onNavigationIconClick

            icon?.let {
                IconButton(onClick = { callback?.invoke() }) {
                    Icon(icon, contentDescription = appBarState.navigationIconContentDescription)
                }
            }
        },
        title = {
            val title = appBarState.title
            if (title.isNotEmpty()) {
                Text(text = title)
            }
        },
        actions = {
            val items = appBarState.actions
            if (items.isNotEmpty()) {
                ActionsMenu(
                    items = items,
                    isOpen = menuExpanded,
                    onToggleOverflow = { menuExpanded = menuExpanded.not() },
                    maxVisibleItems = 3,
                    dropMenuExpandedStates = mapOf(
                        "add" to addDropMenu,
                        "list" to listDropMenu
                    ),
                    dropMenuItemOnClicks = mapOf(
                        "add" to { addDropMenu = addDropMenu.not() },
                        "list" to { listDropMenu = listDropMenu.not() }
                    ),
                    dropMenuItems = appBarState.dropMenuItems
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    appBarState: AppBarState,
    onSettingsClick: () -> Unit,
    toNoAppBarScreen: () -> Unit,
    toManyOptionsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    onLangChange: (String) -> Unit,
) {
    val screen = appBarState.currentScreen as? Screen.Home
    val fontSize = rememberSaveable { mutableStateOf(value = 14.sp) }

    LaunchedEffect(key1 = screen) {
        screen?.buttons
            ?.onEach { button ->
                when (button) {
                    Screen.Home.AppBarIcons.Settings -> onSettingsClick()
                    Screen.Home.AppBarIcons.English -> onLangChange("en")
                    Screen.Home.AppBarIcons.Spanish -> onLangChange("es")
                }
            }
            ?.launchIn(this)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = toManyOptionsScreen
            ) {
                Text(
                    fontSize = fontSize.value,
                    text = stringResource(R.string.many_action_bar_items_screen),
                    onTextLayout = {
                        if (it.didOverflowWidth) {
                            fontSize.value *= 0.9f
                        }
                    }
                )
            }
            Button(
                onClick = toNoAppBarScreen
            ) {
                Text(
                    text = stringResource(R.string.no_app_bar_screen),
                    fontSize = fontSize.value,
                    onTextLayout = {
                        if (it.didOverflowWidth) {
                            fontSize.value *= 0.9f
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundApp() {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                modifier = Modifier.fillMaxWidth()
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.app_content)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "My App"
            )
        },
        actions = {
            ActionsMenu(
                items = listOf(
                    ActionMenuItem.IconMenuItem.AlwaysShown(
                        title = stringResource(R.string.search),
                        contentDescription = stringResource(R.string.search),
                        onClick = {},
                        icon = Icons.Outlined.Search,
                    ),
                    ActionMenuItem.IconMenuItem.AlwaysShown(
                        title = stringResource(R.string.favourite),
                        contentDescription = stringResource(R.string.favourite),
                        onClick = {},
                        icon = Icons.Outlined.FavoriteBorder,
                    ),
                    ActionMenuItem.IconMenuItem.ShownIfRoom(
                        title = stringResource(R.string.refresh),
                        contentDescription = stringResource(R.string.refresh),
                        onClick = {},
                        icon = Icons.Outlined.Refresh
                    ),
                    ActionMenuItem.NeverShown(
                        title = stringResource(R.string.settings),
                        onClick = {},
                    ),
                    ActionMenuItem.NeverShown(
                        title = "About",
                        onClick = {},
                    ),
                ),
                isOpen = menuExpanded,
                onToggleOverflow = { menuExpanded = !menuExpanded },
                maxVisibleItems = 3,
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HomeTopAppBarPreview() {
    SharedActionBarTheme {
        HomeTopAppBar()
    }
}

@Preview(showBackground = true)
@Composable
fun PlaygroundAppPreview() {
    SharedActionBarTheme {
        PlaygroundApp()
    }
}