package com.ohuo.application.services.dto

import kotlinx.serialization.Serializable

@Serializable
data class SettingsUpgrade(
    val testVersion: String = "",
    val newVersion: String = "",
    val latestVersion: String = "",
    val releaseNote: String = ""
) {
    fun isUpdateAvailable(current:String?): Boolean {
        return latestVersion.isNotEmpty() && current != latestVersion
    }
}
