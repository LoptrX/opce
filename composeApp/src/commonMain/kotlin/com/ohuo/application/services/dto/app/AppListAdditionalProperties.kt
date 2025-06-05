package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable

@Serializable
data class AppListAdditionalProperties(
    val tags: List<Tag> = listOf(),
    val version: String = ""
)