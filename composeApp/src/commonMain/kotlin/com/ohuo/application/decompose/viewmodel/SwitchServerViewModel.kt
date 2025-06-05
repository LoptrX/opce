package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.ohuo.application.database.Db
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.database.repository.getPriority
import com.ohuo.application.pages.config.dto.TempAuthDTO
import com.ohuo.application.services.client
import com.ohuo.application.services.computeToken
import com.ohuo.application.services.dashboardCurrent
import com.ohuo.application.services.dto.DashboardCurrent
import com.ohuo.application.services.dto.OPResult
import com.ohuo.application.services.ok
import com.ohuo.application.updateServerApiConfig
import com.ohuo.application.util.TimerManager
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Data class representing the state of the Switch Server screen.
 */
data class ServerStateInfo(
    val server: ServerApiConfig,
    val dashboard: DashboardCurrent = DashboardCurrent()
)

data class SwitchServerScreenState(
    val isLoading: Boolean = false,
    val serverList: List<ServerStateInfo> = listOf(),
    val selected: Long? = null
)

/**
 * ViewModel for the Switch Server screen using Decompose architecture
 */
class SwitchServerViewModel(
    context: ComponentContext,
    initialState: SwitchServerScreenState = SwitchServerScreenState()
) : DecomposeViewModel<SwitchServerScreenState>(
    context = context,
    initialState = initialState
) {
    private val auths: MutableMap<String, TempAuthDTO> = mutableMapOf()
    private val mutex: Mutex = Mutex()
    private val tm = TimerManager(coroutineScope)

    init {
        lifecycle.doOnDestroy {
            tm.stopPeriodicRefresh()
        }
    }

    /**
     * Loads the list of available servers
     */
    fun loadServers() {
        val currentState = stateFlow.value
        updateState(currentState.copy(isLoading = true))

        coroutineScope.launch {
            val servers = Db.instance.serverApiConfigQueries.selectAll().executeAsList()
            val currentServer = getPriority()

            val currentState = stateFlow.value
            val newState = currentState.copy(
                isLoading = false,
                serverList = servers.map { ServerStateInfo(it) },
                selected = currentServer?.id
            )
            updateState(newState)
        }.invokeOnCompletion {
            tm.startPeriodicRefresh(true) {
                freshIndex()
            }
        }
    }

    fun selectServer(server: ServerApiConfig) {
        coroutineScope.launch {
            val updatedServer = server.copy(priority = 1)
            updateServerApiConfig(updatedServer)
            val currentState = stateFlow.value
            updateState(currentState.copy(selected = updatedServer.id))
            loadServers()
        }
    }

    fun updateCurrentServer(server: ServerApiConfig) {
        coroutineScope.launch {
            val updatedServer = server.copy(priority = 1)
            updateServerApiConfig(updatedServer)
            val currentState = stateFlow.value
            updateState(currentState.copy(selected = updatedServer.id))
            loadServers()
        }
    }

    suspend fun getAuth(server: ServerApiConfig): TempAuthDTO {
        mutex.withLock {
            return auths.getOrPut(server.id.toString()) {
                val pair = server.computeToken()
                TempAuthDTO(server.serverUrl, pair.first, pair.second.toString())
            }
        }
    }

    suspend fun freshIndex() {
        val list = state.serverList.map { item ->
            val server = item.server
            var auth = getAuth(server)
            var current = item.dashboard
            ok<OPResult.DashboardCurrentRes, DashboardCurrent?> {
                dashboardCurrent(client, auth, "basic")
            }?.let {
                current = it
            }
            ok<OPResult.DashboardCurrentRes, DashboardCurrent?> {
                dashboardCurrent(
                    client,
                    auth,
                    "gpu"
                )
            }?.let {
                current = current.copy(gpuData = it.gpuData)
            }
            ok<OPResult.DashboardCurrentRes, DashboardCurrent?> {
                dashboardCurrent(
                    client,
                    auth,
                    "ioNet"
                )
            }?.let {
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
            item.copy(dashboard = current)
        }
        updateState(state.copy(serverList = list))
    }
}