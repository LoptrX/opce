package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

/**
 * Data class representing the state of the Managements screen.
 */
@Serializable
data class ManagementsScreenState(
    val isLoading: Boolean = false,
    val serverList: List<String> = emptyList(),
    val selectedServer: String = ""
)

/**
 * ViewModel for the Managements screen using Decompose architecture
 */
class ManagementsViewModel(
    context: ComponentContext,
    initialState: ManagementsScreenState = ManagementsScreenState()
) : DecomposeViewModel<ManagementsScreenState>(
    context = context,
    initialState = initialState
) {

    /**
     * Loads the list of available servers
     */
    fun loadServers() {
        updateState(stateFlow.value.copy(isLoading = false))
    }
    
    /**
     * Updates the selected server
     */
    fun selectServer(serverName: String) {
        updateState(stateFlow.value.copy(selectedServer = serverName))
    }
}