package com.ohuo.application.services.dto.file

import kotlinx.serialization.Serializable

@Serializable
data class FileSearchInput(
    val path: String = "/",
    val expand: Boolean = true,
    val showHidden: Boolean = true,
    val page: Int = 1,
    val pageSize: Int = 100,
    val search: String = "",
    val containSub: Boolean = false,
    val sortBy: String = "name",
    val sortOrder: String = "ascending"
)