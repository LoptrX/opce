package com.ohuo.application.services.dto

import kotlinx.serialization.Serializable

@Serializable
data class SystemInfo(
    // 新增字段
    val websiteNumber: Int = 0,
    val databaseNumber: Int = 0,
    val cronjobNumber: Int = 0,
    val appInstalledNumber: Int = 0,  // 注意字段名拼写与 JSON 一致
    val hostname: String = "",
    val os: String = "",
    val platform: String = "",
    val platformFamily: String = "",
    val platformVersion: String = "",
    val kernelArch: String = "",
    val kernelVersion: String = "",
    val virtualizationSystem: String = "",  // JSON 字符串，可进一步解析为对象
    val ipv4Addr: String = "",
    val SystemProxy: String = "",           // 保持原始大写字段名
    val cpuCores: Int = 0,
    val cpuLogicalCores: Int = 0,
    val cpuModelName: String = "",
)