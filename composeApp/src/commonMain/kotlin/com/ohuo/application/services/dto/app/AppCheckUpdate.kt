package com.ohuo.application.services.dto.app

import kotlinx.serialization.Serializable

@Serializable
data class AppCheckUpdate(
    val appList: AppList,
    val appStoreLastModified: Long = -1,
    val canUpdate: Boolean = false,
    val isSyncing: Boolean = false
)

@Serializable
data class App(
    val additionalProperties: AppAdditionalProperties,
    val icon: String,
    val lastModified: Long,
    val name: String,
    val readMe: String,
    val versions: List<VersionItem>
)





