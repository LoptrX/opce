package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.database.repository.findById
import com.ohuo.application.database.repository.save
import com.ohuo.application.util.scope
import com.ohuo.application.util.isHttpsDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ServerApiConfig::class)
data class ServerConfigState(
    val config: ServerApiConfig = ServerApiConfig(
        -1L,
        "",
        "",
        120,
        "",
        1
    ),
    val errors: Map<String, String> = emptyMap()
)

class ServerConfigViewModel(
    context: ComponentContext,
    initialState: ServerConfigState = ServerConfigState()
) : DecomposeViewModel<ServerConfigState>(
    context = context,
    initialState = initialState
) {

    fun loadConfig(id: Long?) {
        context.lifecycle.scope.launch {
            id?.let { findById(it) }?.let { config ->
                val currentState = stateFlow.value
                updateState(currentState.copy(config = config))
            }
        }
    }

    fun update(data: ServerApiConfig) {
        val currentState = stateFlow.value
        updateState(currentState.copy(config = data))
    }

    fun saveConfig(data: ServerApiConfig, onComplete: (ServerApiConfig) -> Unit) {
        context.lifecycle.scope.launch(Dispatchers.IO) {
            save(data)
            withContext(Dispatchers.Main) {
                onComplete(data)
            }
        }
    }

    fun validateInput(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val currentState = stateFlow.value
        val config = currentState.config

        if (config.serverUrl.isBlank()) {
            errors["serverUrl"] = "服务器地址不能为空"
        } else if (!isHttpsDomain(config.serverUrl)) {
            errors["serverUrl"] = "服务器地址必须以https://开头"
        }

        if (config.apiKey.isBlank()) {
            errors["apiKey"] = "密钥不能为空"
        }

        if (config.alias.isBlank()) {
            errors["alias"] = "别名不能为空"
        }

        if (config.time in 0..120) {
            errors["time"] = "验证时间必须在0-120分钟内，为0时不校验"
        }
        updateState(currentState.copy(errors = errors))
        return errors
    }
}