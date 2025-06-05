package com.ohuo.application.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import com.ohuo.application.decompose.RootComponent

val navItems = listOf(
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "首页",
        route = RootComponent.Child.HomeChild.ROUTE,
    ),
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Apps,
        selectedIcon = Icons.Filled.Apps,
        title = "管理",
        route = RootComponent.Child.ManagementsChild.ROUTE,
    ),
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person,
        title = "我的",
        route = RootComponent.Child.MeChild.ROUTE,
    ),
)