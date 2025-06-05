package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable

@Serializable
data class AppDetail(
    val id: Int = -1,
    val status: String = "",
    val dockerCompose:String = "",
    val downloadCallBackUrl:String = "",
    val downloadUrl:String = "",
    val enable: Boolean = false,
    val gpuSupport: Boolean = false,
    val hostMode: Boolean = false,
    val ignoreUpgrade: Boolean = false,
    val update: Boolean = false,
    val image: String = "",
    val params: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val lastModified: Long = 0,
    val lastVersion: String = "",
    val version: String = "",
)
