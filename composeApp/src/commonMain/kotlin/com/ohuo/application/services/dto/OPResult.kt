package com.ohuo.application.services.dto

import com.ohuo.application.services.dto.app.AppCheckUpdate
import com.ohuo.application.services.dto.app.AppDetail
import com.ohuo.application.services.dto.app.AppInstalledSearch
import com.ohuo.application.services.dto.file.FileItem
import com.ohuo.application.services.dto.website.SiteResult
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable
sealed class OPResult<T> {
    abstract val code: Int
    abstract val message: String
    abstract val data: T

    fun isSuccess() = code in HttpStatusCode.OK.value..HttpStatusCode.MultiStatus.value

    @Serializable
    data class SettingsSearchRes(
        override val code: Int,
        override val message: String,
        override val data: SettingsSearch?
    ) : OPResult<SettingsSearch?>()

    @Serializable
    data class SettingsUpgradeRes(
        override val code: Int,
        override val message: String,
        override val data: SettingsUpgrade?
    ) : OPResult<SettingsUpgrade?>()

    @Serializable
    data class AppCheckUpdateRes(
        override val code: Int,
        override val message: String,
        override val data: AppCheckUpdate?
    ) : OPResult<AppCheckUpdate?>()

    @Serializable
    data class AppInstalledSearchRes(
        override val code: Int,
        override val message: String,
        override val data: AppInstalledSearch?
    ) : OPResult<AppInstalledSearch?>()

    @Serializable
    data class AppDetailRes(
        override val code: Int,
        override val message: String,
        override val data: AppDetail?
    ) : OPResult<AppDetail?>()

    @Serializable
    data class AppOpRes(
        override val code: Int,
        override val message: String,
        override val data: Map<String, String?>?
    ) : OPResult<Map<String, String?>?>()

    @Serializable
    data class OSRes(
        override val code: Int,
        override val message: String,
        override val data: OS?
    ) : OPResult<OS?>()

    @Serializable
    data class SystemInfoRes(
        override val code: Int,
        override val message: String,
        override val data: SystemInfo?
    ) : OPResult<SystemInfo?>()

    @Serializable
    data class DashboardCurrentRes(
        override val code: Int,
        override val message: String,
        override val data: DashboardCurrent?
    ): OPResult<DashboardCurrent?>()

    @Serializable
    data class HostsMonitorRes(
        override val code: Int,
        override val message: String,
        override val data: List<String>?
    ): OPResult<List<String>?>()

    @Serializable
    data class FileSearchRes(
        override val code: Int,
        override val message: String,
        override val data: FileItem = FileItem()
    ): OPResult<FileItem>()

    @Serializable
    data class WebSiteSearchRes(
        override val code: Int,
        override val message: String,
        override val data: SiteResult = SiteResult()
    ): OPResult<SiteResult>()
}
