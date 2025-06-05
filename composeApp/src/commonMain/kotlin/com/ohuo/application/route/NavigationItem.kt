package com.ohuo.application.route

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NavigationItem(
    @Contextual
    val unSelectedIcon: ImageVector,
    @Contextual
    val selectedIcon: ImageVector,
    val title: String,
    val route : String
)