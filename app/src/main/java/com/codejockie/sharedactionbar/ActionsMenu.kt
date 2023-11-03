package com.codejockie.sharedactionbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.codejockie.sharedactionbar.ui.theme.SharedActionBarTheme

private data class MenuItems(
    val visibleItems: List<ActionMenuItem.IconMenuItem>,
    val overflowItems: List<ActionMenuItem>
)

private fun splitMenuItems(
    items: List<ActionMenuItem>,
    maxVisibleItems: Int,
    dropMenuItems: List<ActionMenuItem.IconMenuItem.DropMenu> = emptyList(),
): MenuItems {
    // Combine dropMenuItems and alwaysShownItems
    val alwaysShownItems: MutableList<ActionMenuItem.IconMenuItem> =
        (dropMenuItems + items.filterIsInstance<ActionMenuItem.IconMenuItem.AlwaysShown>()).toMutableList()
    val ifRoomItems: MutableList<ActionMenuItem.IconMenuItem> =
        items.filterIsInstance<ActionMenuItem.IconMenuItem.ShownIfRoom>().toMutableList()
    val overflowItems = items.filterIsInstance<ActionMenuItem.NeverShown>()

//    Determine if there are overflow items — we do if we have items of type NeverShown
//    (as these always go to the drop down), or if the number of AlwaysShown items plus the number
//    of ShownIfRoom items exceeds the maximum number of visible items, minus 1, to account for the
//    overflow action item.
    val hasOverflow = overflowItems.isNotEmpty() ||
            (alwaysShownItems.size + ifRoomItems.size - 1) > maxVisibleItems
    val usedSlots = alwaysShownItems.size + (if (hasOverflow) 1 else 0)
    val availableSlots = maxVisibleItems - usedSlots

    if (availableSlots > 0 && ifRoomItems.isNotEmpty()) {
        val visible = ifRoomItems.subList(0, availableSlots.coerceAtMost(ifRoomItems.size))
        alwaysShownItems.addAll(visible)
        ifRoomItems.removeAll(visible)
    }

    return MenuItems(
        visibleItems = alwaysShownItems,
        overflowItems = ifRoomItems + overflowItems
    )
}

@Composable
fun ActionsMenu(
    items: List<ActionMenuItem>,
    isOpen: Boolean,
    onToggleOverflow: () -> Unit,
    maxVisibleItems: Int,
    expandDropMenus: Map<String, Boolean> = emptyMap(),
    onDropMenuItemClicks: Map<String, () -> Unit> = emptyMap(),
    dropMenuItems: List<ActionMenuItem.IconMenuItem.DropMenu> = emptyList(),
) {
    val menuItems = remember(key1 = items, key2 = maxVisibleItems) {
        splitMenuItems(items, maxVisibleItems, dropMenuItems)
    }

    menuItems.visibleItems.forEach { item ->
        val isDropMenu = item is ActionMenuItem.IconMenuItem.DropMenu
        if (!isDropMenu) {
            IconButton(onClick = item.onClick) {
                Icon(item.icon, contentDescription = item.contentDescription)
            }
        } else {
            val onClick = onDropMenuItemClicks[item.title] ?: {}
            Box {
                IconButton(onClick = onClick) {
                    Icon(item.icon, contentDescription = item.contentDescription)
                }
                DropdownMenu(
                    expanded = expandDropMenus[item.title] ?: false,
                    onDismissRequest = onClick
                ) {
                    item.items?.forEach {
                        DropdownMenuItem(text = { Text(it.title) }, onClick = it.onClick)
                    }
                }
            }
        }
    }

    if (menuItems.overflowItems.isNotEmpty()) {
        Box {
            IconButton(onClick = onToggleOverflow) {
                Icon(
                    Icons.Outlined.MoreVert,
                    contentDescription = stringResource(id = R.string.menu)
                )
            }
            DropdownMenu(expanded = isOpen, onDismissRequest = onToggleOverflow) {
                menuItems.overflowItems.forEach { item ->
                    DropdownMenuItem(text = { Text(item.title) }, onClick = item.onClick)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 440, showBackground = true)
@Preview(widthDp = 440, uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ActionMenuPreview(
    @PreviewParameter(ActionMenuParameterProvider::class) items: List<ActionMenuItem>
) {
    SharedActionBarTheme {
        var menuOpen by remember { mutableStateOf(false) }
        val numAlwaysShown = items.count { item -> item is ActionMenuItem.IconMenuItem.AlwaysShown }
        val numIfRoom = items.count { item -> item is ActionMenuItem.IconMenuItem.ShownIfRoom }
        val numOverflow = items.count { item -> item is ActionMenuItem.NeverShown }
        val label = "Always: $numAlwaysShown Room: $numIfRoom Over: $numOverflow"
        TopAppBar(
            title = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                ActionsMenu(
                    items = items,
                    isOpen = menuOpen,
                    onToggleOverflow = { menuOpen = !menuOpen },
                    maxVisibleItems = 3
                )
            }
        )
    }
}

private class ActionMenuParameterProvider : PreviewParameterProvider<List<ActionMenuItem>> {
    override val values: Sequence<List<ActionMenuItem>>
        get() = sequenceOf(
            listOf(
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Search",
                    onClick = {},
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Favorite",
                    onClick = {},
                    icon = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Star",
                    onClick = {},
                    icon = Icons.Outlined.Star,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Refresh",
                    onClick = {},
                    icon = Icons.Outlined.Refresh,
                    contentDescription = null,
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
            listOf(
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Search",
                    onClick = {},
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Favorite",
                    onClick = {},
                    icon = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Star",
                    onClick = {},
                    icon = Icons.Outlined.Star,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Refresh",
                    onClick = {},
                    icon = Icons.Outlined.Refresh,
                    contentDescription = null,
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
            listOf(
                ActionMenuItem.IconMenuItem.AlwaysShown(
                    title = "Search",
                    onClick = {},
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Favorite",
                    onClick = {},
                    icon = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Star",
                    onClick = {},
                    icon = Icons.Outlined.Star,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Refresh",
                    onClick = {},
                    icon = Icons.Outlined.Refresh,
                    contentDescription = null,
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
            listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Search",
                    onClick = {},
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Favorite",
                    onClick = {},
                    icon = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Star",
                    onClick = {},
                    icon = Icons.Outlined.Star,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Refresh",
                    onClick = {},
                    icon = Icons.Outlined.Refresh,
                    contentDescription = null,
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
            listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Search",
                    onClick = {},
                    icon = Icons.Outlined.Search,
                    contentDescription = null,
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Favorite",
                    onClick = {},
                    icon = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                ),
            ),
            listOf(
                ActionMenuItem.NeverShown(
                    title = "Search",
                    onClick = {},
                ),
                ActionMenuItem.NeverShown(
                    title = "Favorite",
                    onClick = {},
                ),
            ),
        )
}