package com.ohuo.application.services

import com.ohuo.application.constant.SERVER_URL
import com.ohuo.application.globalStateComponent
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun licensesStatus(): HttpResponse {
    return withContext(Dispatchers.IO) {
        val urlString = globalStateComponent.state.value.serverUrl + "/api/v1/licenses/get/status"
        panelClient.get(urlString)
    }
}