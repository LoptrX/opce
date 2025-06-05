package com.ohuo.application.services.dto.website

import kotlinx.serialization.Serializable

@Serializable
data class SiteResult(
    val total: Int = -1,
    val items: MutableList<SiteItem> = mutableListOf()
)

@Serializable
data class SiteItem(
    val id: Int,
    val createdAt: String,
    val protocol: String,
    val primaryDomain: String,
    val type: String,
    val alias: String,
    val remark: String? = null,
    val status: String,
    val expireDate: String,
    val sitePath: String,
    val appName: String,
    val runtimeName: String,
    val sslExpireDate: String,
    val sslStatus: String,
    val appInstallId: Int,
    val runtimeType: String
)
