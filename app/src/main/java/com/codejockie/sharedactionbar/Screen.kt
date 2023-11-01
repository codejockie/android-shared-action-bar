package com.codejockie.sharedactionbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

const val HomeRoute = "home"
const val SettingsRoute = "settings"

sealed interface Screen {
    val route: String
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
    val navigationIconContentDescription: String?
    val onNavigationIconClick: (() -> Unit)?
    val title: String
    val actions: List<ActionMenuItem>

    object Home : Screen {
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
    }

    object Settings : Screen {
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
}

fun getScreen(route: String?): Screen? = Screen::class.nestedClasses.map { kClass ->
    kClass.objectInstance as Screen
}.firstOrNull { screen -> screen.route == route }