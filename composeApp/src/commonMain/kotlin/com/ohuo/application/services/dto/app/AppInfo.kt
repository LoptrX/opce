package com.ohuo.application.services.dto.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val website: String = "",
    val document: String = "",
    val github: String = "",
    @SerialName("gpuSupport") val isGpuSupported: Boolean = false
)