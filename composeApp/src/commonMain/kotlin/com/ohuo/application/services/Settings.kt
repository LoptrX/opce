package com.ohuo.application.services

import com.ohuo.application.constant.SERVER_URL
import com.ohuo.application.globalStateComponent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * 检查更新
 */
suspend fun settingsUpgrade(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/settings/upgrade"
        panelClient.get(urlString)
    }
}

/**
 * 面板配置
 */
suspend fun settingsSearch(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/settings/search"
         panelClient.post(urlString)
    }
}

/**
 * 应用备份
 */
suspend fun backup(params: Map<String,Any?>): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/settings/backup/backup"
        panelClient.post(urlString) {
            contentType(Application.Json)
            setBody(params)
        }
    }
}