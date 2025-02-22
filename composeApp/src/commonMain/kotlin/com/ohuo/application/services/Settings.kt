package com.ohuo.application.services

import com.ohuo.application.GlobalState
import com.ohuo.application.constant.SERVER_URL
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse

/**
 * 检查更新
 */
suspend fun settingsUpgrade(): HttpResponse {
    val urlString = GlobalState[SERVER_URL] as String + "/api/v1/settings/upgrade"
    return panelClient.get(urlString)
}

/**
 * 面板配置
 */
suspend fun settingsSearch(): HttpResponse {
    val urlString = GlobalState[SERVER_URL] as String + "/api/v1/settings/search"
    return panelClient.post(urlString)
}