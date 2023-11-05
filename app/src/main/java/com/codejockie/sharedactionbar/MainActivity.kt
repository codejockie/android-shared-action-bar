package com.codejockie.sharedactionbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codejockie.sharedactionbar.ui.theme.SharedActionBarTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var storage: OnboardingStorage

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
                            AppBar(
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
                        ) { backStackEntry ->
                            HomeRoute(
                                appBarState = appBarState,
                                onboardingStorage = storage,
                                savedStateHandle = backStackEntry.savedStateHandle,
                                onSettingsClick = { navController.navigate(SettingsRoute) },
                                toNoAppBarScreen = { navController.navigate(NoAppBarRoute) },
                                toManyOptionsScreen = { navController.navigate(ManyOptionsRoute) },
                                toOnboarding = {
                                    navController.navigate(OnboardingRoute)
                                },
                                onOnboardingCancelled = {
                                    this@MainActivity.finish()
                                },
                                modifier = Modifier
                                    .fillMaxSize()
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
                        composable(OnboardingRoute) {
                            LaunchedEffect(key1 = Unit) {
                                // Set default onboarding result to cancelled should the user
                                // decide to leave the onboarding screen
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    OnboardingKey,
                                    OnboardingResult.Cancelled
                                )
                            }
                            OnboardingRoute(
                                onboardingStorage = storage,
                                popBackStack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeRoute(
    appBarState: AppBarState,
    onSettingsClick: () -> Unit,
    toNoAppBarScreen: () -> Unit,
    toManyOptionsScreen: () -> Unit,
    savedStateHandle: SavedStateHandle,
    onboardingStorage: OnboardingStorage,
    toOnboarding: () -> Unit,
    onOnboardingCancelled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onboardingState by onboardingStorage.onboardingState.collectAsState()
    val onboardingResult by savedStateHandle.getLiveData<OnboardingResult>(OnboardingKey)
        .observeAsState()

    when (onboardingState) {
        OnboardingState.NotOnboarded -> {
            when (onboardingResult) {
                null -> LaunchedEffect(key1 = Unit) {
                    toOnboarding()
                }
                OnboardingResult.Completed -> HomeScreen(
                    appBarState = appBarState,
                    onSettingsClick = onSettingsClick,
                    toNoAppBarScreen = toNoAppBarScreen,
                    toManyOptionsScreen = toManyOptionsScreen,
                    modifier = modifier
                )
                OnboardingResult.Cancelled -> LaunchedEffect(key1 = Unit) {
                    onOnboardingCancelled()
                }
            }
        }

        OnboardingState.Onboarded -> HomeScreen(
            appBarState = appBarState,
            onSettingsClick = onSettingsClick,
            toNoAppBarScreen = toNoAppBarScreen,
            toManyOptionsScreen = toManyOptionsScreen,
            modifier = modifier
        )
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
                text = "App content"
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
                        title = "Search",
                        contentDescription = "Search",
                        onClick = {},
                        icon = Icons.Outlined.Search,
                    ),
                    ActionMenuItem.IconMenuItem.AlwaysShown(
                        title = "Favourite",
                        contentDescription = "Favourite",
                        onClick = {},
                        icon = Icons.Outlined.FavoriteBorder,
                    ),
                    ActionMenuItem.IconMenuItem.ShownIfRoom(
                        title = "Refresh",
                        contentDescription = "Refresh",
                        onClick = {},
                        icon = Icons.Outlined.Refresh
                    ),
                    ActionMenuItem.NeverShown(
                        title = "Settings",
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