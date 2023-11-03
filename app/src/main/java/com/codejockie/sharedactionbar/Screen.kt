package com.codejockie.sharedactionbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

const val HomeRoute = "home"
const val ManyOptionsRoute = "manyOptions"
const val NoAppBarRoute = "noAppBar"
const val SettingsRoute = "settings"

sealed interface Screen {
    val route: String
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
    val navigationIconContentDescription: String?
    val onNavigationIconClick: (() -> Unit)?
    val title: String
    val actions: List<ActionMenuItem>
    val dropMenuItems: List<ActionMenuItem.IconMenuItem.DropMenu>
        get() = emptyList()

    class Home : Screen {
        enum class AppBarIcons {
            Settings
        }

        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()

        override val route: String = HomeRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector? = null
        override val onNavigationIconClick: (() -> Unit)? = null
        override val navigationIconContentDescription: String? = null
        override val title: String = "Home"
        override val actions: List<ActionMenuItem> = listOf(
            ActionMenuItem.IconMenuItem.AlwaysShown(
                title = "Settings",
                onClick = {
                    _buttons.tryEmit(AppBarIcons.Settings)
                },
                icon = Icons.Outlined.Settings,
                contentDescription = null,
            )
        )
        override val dropMenuItems
            get() = listOf(
                ActionMenuItem.IconMenuItem.DropMenu(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    onClick = {},
                    title = "add",
                    items = listOf(
                        ActionMenuItem.NeverShown(
                            title = "Settings",
                            onClick = {},
                        ),
                        ActionMenuItem.NeverShown(
                            title = "About",
                            onClick = {},
                        ),
                    )
                ),
                ActionMenuItem.IconMenuItem.DropMenu(
                    Icons.Outlined.List,
                    contentDescription = null,
                    onClick = {},
                    title = "list",
                    items = listOf(
                        ActionMenuItem.NeverShown(
                            title = "Info",
                            onClick = {},
                        ),
                        ActionMenuItem.NeverShown(
                            title = "Contact Us",
                            onClick = {},
                        ),
                    )
                )
            )
    }

    class Settings : Screen {
        override val route: String = SettingsRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Outlined.ArrowBack
        override val onNavigationIconClick: () -> Unit = {
            _buttons.tryEmit(AppBarIcons.NavigationIcon)
        }
        override val navigationIconContentDescription: String? = null
        override val title: String = "Settings"
        override val actions: List<ActionMenuItem> = emptyList()

        enum class AppBarIcons {
            NavigationIcon
        }

        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()
    }

    class NoAppBar : Screen {
        override val route: String = NoAppBarRoute
        override val isAppBarVisible: Boolean = false
        override val navigationIcon: ImageVector? = null
        override val onNavigationIconClick: (() -> Unit)? = null
        override val navigationIconContentDescription: String? = null
        override val title: String = ""
        override val actions: List<ActionMenuItem> = emptyList()
    }

    class ManyOptions : Screen {
        override val route: String = ManyOptionsRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val onNavigationIconClick: () -> Unit = {
            _buttons.tryEmit(AppBarIcons.NavigationIcon)
        }
        override val navigationIconContentDescription: String? = null
        override val title: String = "Many Options"

        private var _favouriteIcon by mutableStateOf(Icons.Default.FavoriteBorder)

        override val actions: List<ActionMenuItem> by derivedStateOf {
            listOf(
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Search",
                    onClick = {
                        _buttons.tryEmit(AppBarIcons.Search)
                    },
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Favourite",
                    onClick = {
                        _buttons.tryEmit(AppBarIcons.Favourite)
                    },
                    icon = _favouriteIcon,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Star",
                    onClick = {
                        _buttons.tryEmit(AppBarIcons.Star)
                    },
                    icon = Icons.Outlined.Star,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Refresh",
                    onClick = {
                        _buttons.tryEmit(AppBarIcons.Refresh)
                    },
                    icon = Icons.Outlined.Refresh,
                    contentDescription = null,
                ),
                ActionMenuItem.NeverShown(
                    title = "Settings",
                    onClick = {
                        _buttons.tryEmit(AppBarIcons.Settings)
                    },
                ),
                ActionMenuItem.NeverShown(
                    title = "About",
                    onClick = {
                        _buttons.tryEmit(AppBarIcons.About)
                    },
                ),
            )
        }

        fun setFavouriteIcon(icon: ImageVector) {
            _favouriteIcon = icon
        }

        enum class AppBarIcons {
            NavigationIcon,
            Search,
            Favourite,
            Star,
            Refresh,
            Settings,
            About,
        }

        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()
    }
}

fun getScreen(route: String?): Screen? = when (route) {
    HomeRoute -> Screen.Home()
    SettingsRoute -> Screen.Settings()
    ManyOptionsRoute -> Screen.ManyOptions()
    NoAppBarRoute -> Screen.NoAppBar()
    else -> null
}