package com.ohuo.application.pages.config.dto

import com.ohuo.application.util.info
import com.ohuo.application.util.md5
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class TempAuthDTO(
    val serverUrl: String,
    val token: String,
    val timestamp: String,
)
