package com.codejockie.sharedactionbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun ManyOptionsScreen(
    appBarState: AppBarState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val screen = appBarState.currentScreen as? Screen.ManyOptions
    var favouritesEnabled by remember { mutableStateOf(false) }

    fun showSnackbar(text: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message = text)
        }
    }

    LaunchedEffect(key1 = screen) {
        screen?.buttons
            ?.onEach { button ->
                when (button) {
                    Screen.ManyOptions.AppBarIcons.NavigationIcon -> onBackClick()
                    Screen.ManyOptions.AppBarIcons.Search,
                    Screen.ManyOptions.AppBarIcons.Star,
                    Screen.ManyOptions.AppBarIcons.Refresh,
                    Screen.ManyOptions.AppBarIcons.Settings,
                    Screen.ManyOptions.AppBarIcons.About -> showSnackbar(
                        "Clicked on ${button.name}"
                    )

                    Screen.ManyOptions.AppBarIcons.Favourite -> {
                        favouritesEnabled = favouritesEnabled.not()
                        screen.setFavouriteIcon(
                            if (favouritesEnabled) Icons.Outlined.Favorite
                            else Icons.Outlined.FavoriteBorder
                        )
                    }
                }
            }
            ?.launchIn(this)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Many options content")
    }
}