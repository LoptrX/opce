package com.ohuo.application.route

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val title: String) {
    @Serializable
    data object Home : Screen("首页")

    @Serializable
    data class ServerApiConfig(val id: Long? = null) : Screen("配置页")
}