package com.codejockie.sharedactionbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SettingsScreen(
    appBarState: AppBarState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screen = appBarState.currentScreen as? Screen.Settings

    LaunchedEffect(key1 = screen) {
        screen?.buttons
            ?.onEach { button ->
                when (button) {
                    Screen.Settings.AppBarIcons.NavigationIcon -> onBackClick()
                }
            }
            ?.launchIn(this)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(R.string.settings_content))
    }
}