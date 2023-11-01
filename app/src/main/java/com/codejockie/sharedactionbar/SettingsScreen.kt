package com.codejockie.sharedactionbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(key1 = Unit) {
        Screen.Settings
            .buttons
            .onEach { button ->
                when (button) {
                    Screen.Settings.AppBarIcons.NavigationIcon -> onBackClick()
                }
            }
            .launchIn(this)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Settings content")
    }
}