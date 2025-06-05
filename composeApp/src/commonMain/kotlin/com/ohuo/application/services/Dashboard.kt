package com.ohuo.application.services

import com.ohuo.application.constant.SERVER_URL
import com.ohuo.application.globalStateComponent
import com.ohuo.application.pages.config.dto.TempAuthDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun dashboardOS(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/dashboard/base/os"
        panelClient.get(urlString)
    }
}

suspend fun systemInfo(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString =
            globalStateComponent.state.value.serverUrl + "/api/v1/dashboard/base/all/all"
        panelClient.get(urlString)
    }
}

suspend fun dashboardCurrent(scope: String): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/dashboard/current"
        panelClient.post(urlString) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "ioOption" to "all",
                    "netOption" to "all",
                    "scope" to scope,
                )
            )
        }
    }
}

suspend fun dashboardCurrent(client: HttpClient, tempAuth:TempAuthDTO, scope: String): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = tempAuth.serverUrl + "/api/v1/dashboard/current"
        client.post(urlString) {
            contentType(ContentType.Application.Json)
            headers.append("1Panel-Token", tempAuth.token)
            headers.append("1Panel-Timestamp", tempAuth.timestamp)
            setBody(
                mapOf(
                    "ioOption" to "all",
                    "netOption" to "all",
                    "scope" to scope,
                )
            )
        }
    }
}