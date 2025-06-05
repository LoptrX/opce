package com.ohuo.application.services

import com.ohuo.application.services.dto.file.FileSearchInput
import com.ohuo.application.globalStateComponent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun fileSearch(params: FileSearchInput): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/files/search"
        panelClient.post(urlString) {
            contentType(Application.Json)
            setBody(params)
        }
    }
}