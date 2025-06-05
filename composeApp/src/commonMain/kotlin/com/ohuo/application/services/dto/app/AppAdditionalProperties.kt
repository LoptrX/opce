package com.ohuo.application.services.dto.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppAdditionalProperties(
    @SerialName("Required") val required: List<String>,
    val crossVersionUpdate: Boolean,
    val description: Description,
    val document: String,
    val github: String,
    val gpuSupport: Boolean,
    val key: String,
    val limit: Int,
    val name: String,
    val recommend: Int,
    val shortDescEn: String,
    val shortDescZh: String,
    val tags: List<String>,
    val type: String,
    val version: Int,
    val website: String
)