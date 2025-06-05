package com.ohuo.application.decompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.datetime.Clock
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ohuo.application.AppBar
import com.ohuo.application.pages.config.ServerApiConfigScreen
import com.ohuo.application.pages.home.HomeScreen
import com.ohuo.application.pages.manager.AppDetailScreen
import com.ohuo.application.pages.manager.AppListScreen
import com.ohuo.application.pages.manager.FileManageScreen
import com.ohuo.application.pages.manager.ManagementsScreen
import com.ohuo.application.pages.manager.SwitchServerScreen
import com.ohuo.application.pages.me.MeScreen
import com.ohuo.application.route.NavigationItem
import com.ohuo.application.route.navItems

/**
 * Main composable that renders the UI based on the current state of the RootComponent
 */
@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(component: RootComponent) {
    val snackBarHostState = remember { SnackbarHostState() }
    val childStack by component.stack.subscribeAsState()
    val activeChild = childStack.active.instance

    val canNavigateBack = childStack.backStack.isNotEmpty()

    Scaffold(
        topBar = {
            val isTab = component.shouldShowBottomBar(childStack.active.configuration)
            AppBar(
                currentScreen = activeChild,
                canNavigateBack = canNavigateBack && !isTab,
                navigateUp = { component.onNavigateUp() }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (component.shouldShowBottomBar(childStack.active.configuration)) {
                BottomNavigationBar(
                    items = navItems,
                    currentRoute = activeChild.route,
                    onNavigate = { route ->
                        val matchedRoute = RootComponent.Child.match(route)
                        if (matchedRoute != null) {
                            component.navigateToTab(matchedRoute)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Children(
                stack = component.stack,
                animation = predictiveBackAnimation(
                    backHandler = component.backHandler,
                    fallbackAnimation = stackAnimation(fade() + scale()),
                    selector = { backEvent, _, _ -> androidPredictiveBackAnimatable(backEvent) },
                    onBack = component::onNavigateUp
                ),
            ) { child ->
                    when (val instance = child.instance) {
                        is RootComponent.Child.HomeChild -> {
                            HomeScreen(
                                component = instance.component,
                                snackBarHostState = snackBarHostState
                            )
                        }

                        is RootComponent.Child.ServerApiConfigChild -> {
                            ServerApiConfigScreen(
                                component = instance.component,
                                snackBarHostState = snackBarHostState
                            )
                        }

                        is RootComponent.Child.ManagementsChild -> {
                            ManagementsScreen(
                                component = instance.component,
                                snackBarHostState = snackBarHostState
                            )
                        }

                        is RootComponent.Child.MeChild -> {
                            MeScreen(
                                component = instance.component,
                                snackBarHostState = snackBarHostState
                            )
                        }

                        is RootComponent.Child.SwitchServerChild -> {
                            SwitchServerScreen(
                                component = instance.component,
                                snackBarHostState = snackBarHostState
                            )
                        }

                        is RootComponent.Child.AppListChild -> {
                            AppListScreen(instance.component)
                        }

                        is RootComponent.Child.AppDetailChild -> {
                            AppDetailScreen(instance.component)
                        }

                        is RootComponent.Child.FileManagementChild -> {
                            FileManageScreen(instance.component)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(
        items: List<NavigationItem>,
        currentRoute: String,
        onNavigate: (String) -> Unit
    ) {
        // 添加防抖处理，记录上次点击时间
        val lastClickTimeMap = remember { mutableStateMapOf<String, Long>() }
        val debounceTime = 300L // 防抖时间间隔，单位毫秒
        
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEachIndexed { index, item ->
                val selected = currentRoute == item.route
                val icon = if (selected) item.selectedIcon else item.unSelectedIcon
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = selected,
                    onClick = {
                        if (selected) return@NavigationBarItem
                        
                        // 获取当前时间
                        val currentTime = Clock.System.now().toEpochMilliseconds()
                        // 获取上次点击时间，如果没有则为0
                        val lastClickTime = lastClickTimeMap[item.route] ?: 0L
                        
                        // 如果时间间隔大于防抖时间，则执行导航
                        if (currentTime - lastClickTime > debounceTime) {
                            // 更新点击时间
                            lastClickTimeMap[item.route] = currentTime
                            // 执行导航
                            onNavigate(item.route)
                        }
                    }
                )
            }
        }
    }