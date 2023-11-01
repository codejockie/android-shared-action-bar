package com.codejockie.sharedactionbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codejockie.sharedactionbar.ui.theme.SharedActionBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedActionBarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundApp() {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                modifier = Modifier.fillMaxWidth()
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "App content"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "My App"
            )
        },
        actions = {
            ActionsMenu(
                // 3
                items = listOf(
                    ActionMenuItem.IconMenuItem.AlwaysShown(
                        title = "Search",
                        contentDescription = "Search",
                        onClick = {},
                        icon = Icons.Outlined.Search,
                    ),
                    ActionMenuItem.IconMenuItem.AlwaysShown(
                        title = "Favourite",
                        contentDescription = "Favourite",
                        onClick = {},
                        icon = Icons.Outlined.FavoriteBorder,
                    ),
                    ActionMenuItem.IconMenuItem.ShownIfRoom(
                        title = "Refresh",
                        contentDescription = "Refresh",
                        onClick = {},
                        icon = Icons.Outlined.Refresh
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
                // 4
                isOpen = menuExpanded,
                // 5
                onToggleOverflow = { menuExpanded = !menuExpanded },
                maxVisibleItems = 3,
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HomeTopAppBarPreview() {
    SharedActionBarTheme {
        HomeTopAppBar()
    }
}

@Preview(showBackground = true)
@Composable
fun PlaygroundAppPreview() {
    SharedActionBarTheme {
        PlaygroundApp()
    }
}