package com.ohuo.application.pages.home.dto

import kotlinx.serialization.Serializable

@Serializable
data class OS(
    val diskSize: Int = 0,
    val kernelArch: String = "",
    val kernelVersion: String = "",
    val os: String = "",
    val platform: String = "",
    val platformFamily: String = ""
)
