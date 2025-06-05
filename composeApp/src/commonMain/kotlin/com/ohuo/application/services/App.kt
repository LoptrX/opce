package com.ohuo.application.services

import com.ohuo.application.globalStateComponent
import com.ohuo.application.pages.config.dto.TempAuthDTO
import com.ohuo.application.pages.manager.dto.OperateInput
import com.ohuo.application.services.dto.app.Query
import com.ohuo.application.util.info
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun initial(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/auth/intl"
        panelClient.get(urlString)
    }
}

suspend fun initial(auth: TempAuthDTO): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = auth.serverUrl + "/api/v1/auth/intl"
        client.get(urlString) {
            headers.append("1Panel-Token", auth.token)
            headers.append("1Panel-Timestamp", auth.timestamp)
        }
    }
}

suspend fun checkUpdate(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/apps/checkupdate"
        panelClient.get(urlString)
    }
}

suspend fun appDetail(id:Int): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/apps/detail/$id"
        panelClient.get(urlString)
    }
}

suspend fun appInstalledOp(params: OperateInput): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/apps/installed/op"
        panelClient.post(urlString) {
            contentType(Application.Json)
            setBody(params)
        }
    }
}

@OptIn(InternalAPI::class)
suspend fun appInstalledSearch(params: Query): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/apps/installed/search"
        panelClient.post(urlString) {
            contentType(Application.Json)
            setBody(params)
        }
    }
}