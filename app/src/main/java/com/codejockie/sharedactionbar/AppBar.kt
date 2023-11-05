package com.codejockie.sharedactionbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
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