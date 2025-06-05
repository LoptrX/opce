package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.database.repository.getPriority
import com.ohuo.application.services.dashboardCurrent
import com.ohuo.application.services.dto.DashboardCurrent
import com.ohuo.application.services.dto.OPResult
import com.ohuo.application.services.dto.SettingsSearch
import com.ohuo.application.services.dto.SettingsUpgrade
import com.ohuo.application.services.dto.SystemInfo
import com.ohuo.application.services.hostsMonitorIOOpts
import com.ohuo.application.services.hostsMonitorNetOpts
import com.ohuo.application.services.ok
import com.ohuo.application.services.settingsSearch
import com.ohuo.application.services.settingsUpgrade
import com.ohuo.application.services.systemInfo
import com.ohuo.application.updateServerApiConfig
import com.ohuo.application.util.TimerManager
import com.ohuo.application.util.scope
import com.ohuo.application.util.info
import kotlinx.coroutines.launch
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Serializer(forClass = ServerApiConfig::class)
data class HomeScreenState(
    val systemInfo: SystemInfo = SystemInfo(),
    @Contextual
    val server: ServerApiConfig? = null,
    val netOpts: List<String> = arrayListOf(),
    val netOpt: String = "all",
    val ioOpts: List<String> = arrayListOf(),
    val ioOpt: String = "all",
    val settings: SettingsSearch? = null,
    @Contextual
    val dashboard: Any? = null,
    val dashboardCurrent: DashboardCurrent = DashboardCurrent(),
    val settingsUpgrade: SettingsUpgrade? = null,
)

class HomeViewModel(
    context: ComponentContext,
    initialState: HomeScreenState = HomeScreenState()
) : DecomposeViewModel<HomeScreenState>(
    context = context,
    initialState = initialState
) {
    val timerManager = TimerManager(lifecycle.scope, 10)

    fun load() {
        lifecycle.scope.launch {
            val serverApiConfig = getPriority() ?: return@launch
            updateServerApiConfig(serverApiConfig)
            updateState(
                state.copy(
                    server = serverApiConfig
                )
            )
        }.invokeOnCompletion {
            lifecycle.scope.launch {
                ok<OPResult.SettingsSearchRes, SettingsSearch?> { settingsSearch() }?.let {
                    updateState(state.copy(settings = it))
                }
            }
            lifecycle.scope.launch {
                ok<OPResult.SystemInfoRes, SystemInfo?> { systemInfo() }?.let {
                    updateState(state.copy(systemInfo = it))
                }
            }
            lifecycle.scope.launch {
                ok<OPResult.HostsMonitorRes, List<String>?> { hostsMonitorNetOpts() }?.let {
                    updateState(state.copy(netOpts = it))
                }
            }
            lifecycle.scope.launch {
                ok<OPResult.HostsMonitorRes, List<String>?> { hostsMonitorIOOpts() }?.let {
                    updateState(state.copy(ioOpts = it))
                }
            }
            lifecycle.scope.launch {
                ok<OPResult.SettingsUpgradeRes, SettingsUpgrade?> {
                    settingsUpgrade()
                }?.let {
                    info("1PanelUpdateChecker", it.toString())
                    updateState(state.copy(settingsUpgrade = it))
                }
            }
            timerManager.startPeriodicRefresh(true) {
                freshIndex()
            }
        }
    }


    suspend fun freshIndex() {
        var current = state.dashboardCurrent
        ok<OPResult.DashboardCurrentRes, DashboardCurrent?> { dashboardCurrent("basic") }?.let {
            current = it
        }
        ok<OPResult.DashboardCurrentRes, DashboardCurrent?> { dashboardCurrent("gpu") }?.let {
            current = current.copy(gpuData = it.gpuData)
        }
        ok<OPResult.DashboardCurrentRes, DashboardCurrent?> { dashboardCurrent("ioNet") }?.let {
            current = current.copy(
                ioCount = it.ioCount,
                ioReadBytes = it.ioReadBytes,
                ioReadTime = it.ioReadTime,
                ioWriteBytes = it.ioWriteBytes,
                ioWriteTime = it.ioWriteTime,
                netBytesRecv = it.netBytesRecv,
                netBytesSent = it.netBytesSent
            )
        }
        updateState(state.copy(dashboardCurrent = current))
    }
}