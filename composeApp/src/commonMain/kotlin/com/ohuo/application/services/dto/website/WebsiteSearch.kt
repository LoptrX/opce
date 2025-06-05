package com.ohuo.application.services.dto.website

import kotlinx.serialization.Serializable

@Serializable
data class WebsiteSearch(
    val name: String = "",
    val page: Int = 1,
    val pageSize: Int = 10,
    val orderBy: String = "created_at",
    val order: String = "null",
    val websiteGroupId: Int = 0
)
