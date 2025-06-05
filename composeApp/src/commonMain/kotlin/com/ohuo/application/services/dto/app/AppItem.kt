package com.ohuo.application.services.dto.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppItem(
    val id: Int = 0,
    val name: String = "",
    @SerialName("appID") val appId: Int = 0,
    @SerialName("appDetailID") val appDetailId: Int = 0,
    val version: String = "",
    val status: String = "",
    val message: String = "",
    val httpPort: Int = 0,
    val httpsPort: Int = 0,
    val path: String = "",
    val canUpdate: Boolean = false,
    val icon: String = "",
    @SerialName("appName") val applicationName: String = "",
    val ready: Int = 0,
    val total: Int = 0,
    @SerialName("appKey") val applicationKey: String = "",
    @SerialName("appType") val applicationType: String = "",
    @SerialName("appStatus") val applicationStatus: String = "",
    val dockerCompose: String = "",
    @SerialName("createdAt") val createTime: String = "",
    val app: AppInfo = AppInfo(),
    val versions: List<String> = emptyList()
)