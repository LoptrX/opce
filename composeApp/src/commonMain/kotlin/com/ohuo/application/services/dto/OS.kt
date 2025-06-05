package com.ohuo.application.services.dto

import kotlinx.serialization.Serializable

@Serializable
data class OS(
    val diskSize: Long = 0,
    val kernelArch: String = "",
    val kernelVersion: String = "",
    val os: String = "",
    val platform: String = "",
    val platformFamily: String = ""
)
