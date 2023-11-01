package com.codejockie.sharedactionbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

const val HomeRoute = "home"

sealed interface Screen {
    val route: String
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
    val navigationIconContentDescription: String?
    val onNavigationIconClick: (() -> Unit)?
    val title: String
    val actions: List<ActionMenuItem>

    object Home : Screen {
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
                    // TODO
                },
                icon = Icons.Outlined.Settings,
                contentDescription = null,
            )
        )
    }
}