package com.ohuo.application.services

import com.ohuo.application.GlobalState
import com.ohuo.application.constant.SERVER_URL
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

suspend fun licensesStatus(): HttpResponse {
    val urlString = GlobalState[SERVER_URL] as String + "/api/v1/licenses/get/status"
    return panelClient.get(urlString)
}