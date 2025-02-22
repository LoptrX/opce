package com.ohuo.application.services

import com.ohuo.application.GlobalState
import com.ohuo.application.constant.CURRENT_SERVER
import com.ohuo.application.constant.SERVER_URL
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse

suspend fun checkSystemInit(): HttpResponse {
    val urlString = GlobalState[SERVER_URL] as String + "/api/v1/auth/intl"
    return panelClient.get(urlString)
}