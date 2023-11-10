package com.codejockie.sharedactionbar

import androidx.annotation.StringRes
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    val context = LocalContext.current
    val screen = appBarState.currentScreen as? Screen.ManyOptions
    var favouritesEnabled by rememberSaveable { mutableStateOf(false) }

    fun showSnackbar(@StringRes id: Int, name: String) {
        val message = context.getString(id, name)
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
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
                        R.string.clicked_on, button.name
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
        Text(text = stringResource(R.string.many_options_content))
    }
}