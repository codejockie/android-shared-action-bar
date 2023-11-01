package com.codejockie.sharedactionbar

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ActionMenuItem {
    val title: String
    val onClick: () -> Unit

    sealed interface IconMenuItem : ActionMenuItem {
        val icon: ImageVector
        val contentDescription: String?

        data class AlwaysShown(
            override val icon: ImageVector,
            override val contentDescription: String?,
            override val title: String,
            override val onClick: () -> Unit,
        ) : IconMenuItem

        data class ShownIfRoom(
            override val icon: ImageVector,
            override val contentDescription: String?,
            override val title: String,
            override val onClick: () -> Unit,
        ) : IconMenuItem
    }

    data class NeverShown(
        override val title: String,
        override val onClick: () -> Unit,
    ) : ActionMenuItem
}