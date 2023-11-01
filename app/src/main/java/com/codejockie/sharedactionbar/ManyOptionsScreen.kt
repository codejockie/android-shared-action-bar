package com.codejockie.sharedactionbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun ManyOptionsScreen(
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    fun showSnackbar(text: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message = text)
        }
    }

    LaunchedEffect(key1 = Unit) {
        Screen.ManyOptionsScreen
            .buttons
            .onEach { button ->
                when (button) {
                    Screen.ManyOptionsScreen.AppBarIcons.NavigationIcon -> onBackClick()
                    Screen.ManyOptionsScreen.AppBarIcons.Search,
                    Screen.ManyOptionsScreen.AppBarIcons.Favorite,
                    Screen.ManyOptionsScreen.AppBarIcons.Star,
                    Screen.ManyOptionsScreen.AppBarIcons.Refresh,
                    Screen.ManyOptionsScreen.AppBarIcons.Settings,
                    Screen.ManyOptionsScreen.AppBarIcons.About -> showSnackbar(
                        "Clicked on ${button.name}"
                    )
                }
            }
            .launchIn(this)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Many options content")
    }
}