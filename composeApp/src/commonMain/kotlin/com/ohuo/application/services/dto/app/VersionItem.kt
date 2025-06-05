package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class VersionItem(
    val additionalProperties: Map<String, JsonElement>, // 根据实际内容类型调整
    val downloadCallBackUrl: String,
    val downloadUrl: String,
    val lastModified: Long,
    val name: String
)