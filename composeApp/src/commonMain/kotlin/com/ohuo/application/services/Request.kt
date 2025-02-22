package com.ohuo.application.services

import com.ohuo.application.GlobalState
import com.ohuo.application.constant.CURRENT_SERVER
import com.ohuo.application.constant.PANEL_TIMESTAMP
import com.ohuo.application.constant.PANEL_TOKEN
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.util.info
import com.ohuo.application.util.md5
import io.ktor.client.HttpClient
import io.ktor.http.headers

val panelClient = HttpClient {
    headers {
        authParams()
    }
}

fun authParams(): Map<String, Any> {
    if (GlobalState["token"] != null && GlobalState["timestamp"] != null) {
        return mapOf(
            "1Panel-Token" to GlobalState[PANEL_TOKEN] as String,
            "1Panel-Timestamp" to GlobalState[PANEL_TIMESTAMP] as Long
        )
    }
    val config = GlobalState[CURRENT_SERVER] as ServerApiConfig
    val token = md5("1panel${config.apiKey}${config.timestamp}")
    info(message = "Sign 1panel api key sign, config: $config token: $token")
    GlobalState[PANEL_TOKEN] = token
    GlobalState[PANEL_TIMESTAMP] = config.timestamp ?: 0L
    return mapOf(
        "1Panel-Token" to token,
        "1Panel-Timestamp" to (config.timestamp ?: 0L)
    )
}
