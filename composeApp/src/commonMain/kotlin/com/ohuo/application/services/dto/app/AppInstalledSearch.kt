package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable


@Serializable
data class AppInstalledSearch(
    val items: List<AppItem> = listOf(),
    val total:Int = 0,
)

@Serializable
data class Query(
    val name: String = "",
    val sync: Boolean = false,
    val update: Boolean = false,
    val tags: List<String> = listOf(),
    val page: Int = 1,
    val pageSize: Int = 20,
)
