package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.ohuo.application.database.model.ServerApiConfig
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Serializer(forClass = ServerApiConfig::class)
data class GlobalState(
    @Contextual
    val currentServer: ServerApiConfig? = null,
    val serverUrl: String? = null,
    val panelToken: String? = null,
    val panelTimestamp: Long? = null
)

class GlobalStateComponent(
    context: ComponentContext,
    initialState: GlobalState = GlobalState()
) : ComponentContext by context {

    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    val serializer = initialState::class.serializer() as KSerializer<GlobalState>

    private val _state = MutableValue(
        stateKeeper.consume(KEY_STATE,serializer) ?: initialState
    )
    
    val state: Value<GlobalState> = _state
    
    init {
        stateKeeper.register(KEY_STATE,serializer) { _state.value }
    }
    
    fun updateServerConfig(config: ServerApiConfig) {
        _state.update { currentState ->
            currentState.copy(
                currentServer = config,
                serverUrl = config.serverUrl,
                panelToken = null,
                panelTimestamp = null
            )
        }
    }
    
    fun updatePanelAuth(token: String, timestamp: Long) {
        _state.update { currentState ->
            currentState.copy(
                panelToken = token,
                panelTimestamp = timestamp
            )
        }
    }
    
    companion object {
        private const val KEY_STATE = "global_state"
    }
}