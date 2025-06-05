package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable

@Serializable
data class AppList(
    val additionalProperties: AppListAdditionalProperties = AppListAdditionalProperties(),
    val apps: List<App> = listOf(),
    val lastModified: Long = -1,
    val valid: Boolean = false,
    val violations: List<String> = listOf()
)