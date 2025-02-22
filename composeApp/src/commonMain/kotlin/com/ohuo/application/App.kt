package com.ohuo.application

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ohuo.application.constant.CURRENT_SERVER
import com.ohuo.application.constant.PANEL_TIMESTAMP
import com.ohuo.application.constant.PANEL_TOKEN
import com.ohuo.application.constant.SERVER_URL
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.pages.config.ServerApiConfigScreen
import com.ohuo.application.pages.home.HomeScreen
import com.ohuo.application.route.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

val GlobalState = hashMapOf<String, Any?>()

fun updateServerApiConfig(config: ServerApiConfig) {
    GlobalState[CURRENT_SERVER] = config
    GlobalState[SERVER_URL] = config.serverUrl
    GlobalState[PANEL_TIMESTAMP] = config.timestamp
    GlobalState[PANEL_TOKEN] = null
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = currentScreen.title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        }
    )
}


@Composable
@Preview
fun App(navigator: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry = navigator.currentBackStackEntryAsState()
    // Get the name of the current screen
    val snackBarHostState = remember { SnackbarHostState() }
    val currentScreen = backStackEntry.value?.toRoute<Screen>() ?: Screen.Home

    MaterialTheme {
        var visibility by remember { mutableStateOf(true) }
        Scaffold(
            topBar = {
                if (visibility) {
                    AppBar(
                        currentScreen,
                        canNavigateBack = navigator.previousBackStackEntry != null,
                        navigateUp = { navigator.navigateUp() },
                    )
                }
            },
            snackbarHost = {
                Box(Modifier.fillMaxSize()) {
                    SnackbarHost(
                        modifier = Modifier.align(Alignment.TopCenter),
                        hostState = snackBarHostState
                    )
                }
            }
        ) {
            NavHost(navController = navigator, startDestination = currentScreen) {
                composable<Screen.Home> {
                    visibility = false
                    HomeScreen(navigator, snackBarHostState)
                }
                composable<Screen.ServerApiConfig>{ backStackEntry ->
                    visibility = false
                    val screen = backStackEntry.toRoute<Screen.ServerApiConfig?>()
                    ServerApiConfigScreen(screen?.id, navigator, snackBarHostState)
                }
            }
        }
    }
}
