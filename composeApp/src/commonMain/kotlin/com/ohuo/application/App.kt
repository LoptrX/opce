package com.ohuo.application

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.decompose.GlobalStateComponent
import com.ohuo.application.decompose.RootComponent
import com.ohuo.application.decompose.RootContent
import org.jetbrains.compose.ui.tooling.preview.Preview

lateinit var globalStateComponent: GlobalStateComponent

// 添加全局根组件变量，用于在MainActivity中访问
lateinit var rootComponent: RootComponent

fun updateServerApiConfig(config: ServerApiConfig) {
    globalStateComponent.updateServerConfig(config)
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: RootComponent.Child,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 首先检查页面自身是否要显示TopBar
    if (currentScreen.showTopBar) {
        // 检查是否有自定义的TopBar内容
        val customTopBarContent = currentScreen.customTopBarContent
        if (customTopBarContent != null) {
            // 使用自定义的TopBar内容
            customTopBarContent(canNavigateBack, navigateUp)
        } else {
            // 使用默认的TopAppBar
            TopAppBar(
                // 使用页面自定义标题，如果没有则使用默认标题
                title = {
                    val title = currentScreen.customTopBarTitle ?: currentScreen.title
                    Text(text = title, color = MaterialTheme.colorScheme.onPrimary)
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun App() {
    val lifecycle = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        lifecycle.onCreate()
        onDispose { lifecycle.onDestroy() }
    }

    val defaultComponentContext = remember { DefaultComponentContext(lifecycle) }

    globalStateComponent = remember(defaultComponentContext) {
        GlobalStateComponent(defaultComponentContext)
    }

    // 初始化全局根组件
    rootComponent = remember(defaultComponentContext) {
        RootComponent(
            context = defaultComponentContext
        )
    }

    MaterialTheme {
        RootContent(rootComponent)
    }
}

