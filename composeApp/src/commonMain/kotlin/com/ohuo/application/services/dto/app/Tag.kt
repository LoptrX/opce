package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val key: String,
    val locales: Locales,
    val name: String,
    val sort: Int
)