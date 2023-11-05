package com.codejockie.sharedactionbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeScreen(
    appBarState: AppBarState,
    onSettingsClick: () -> Unit,
    toNoAppBarScreen: () -> Unit,
    toManyOptionsScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screen = appBarState.currentScreen as? Screen.Home

    LaunchedEffect(key1 = screen) {
        screen?.buttons
            ?.onEach { button ->
                when (button) {
                    Screen.Home.AppBarIcons.Settings -> onSettingsClick()
                }
            }
            ?.launchIn(this)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = toManyOptionsScreen
            ) {
                Text(
                    text = "Many action bar items screen"
                )
            }
            Button(
                onClick = toNoAppBarScreen
            ) {
                Text(
                    text = "No app bar screen"
                )
            }
        }
    }
}