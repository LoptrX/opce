package com.ohuo.application.services

import com.ohuo.application.globalStateComponent
import com.ohuo.application.services.dto.website.WebsiteSearch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * 网站列表
 */
suspend fun websites(params: WebsiteSearch): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/websites/search"
        panelClient.post(urlString) {
            contentType(Application.Json)
            setBody(params)
        }
    }
}