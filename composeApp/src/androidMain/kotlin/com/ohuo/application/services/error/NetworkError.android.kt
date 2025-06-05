package com.ohuo.application.services.error

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

actual fun convertException(e: Throwable): NetworkError {
    return when(e) {
        // 域名解析失败
        is UnknownHostException -> NetworkError.UnknownHost

        // 连接超时（建立连接阶段）
        is ConnectTimeoutException -> NetworkError.Timeout

        // 读取超时（数据接收阶段）
        is SocketTimeoutException -> NetworkError.Timeout

        // SSL证书错误
        is SSLHandshakeException -> NetworkError.SSLHandshakeError(e.message)

        // Ktor自动抛出的HTTP错误（当expectSuccess=true时）
        is io.ktor.client.plugins.ResponseException -> {
            NetworkError.HttpError(e.response.status.value)
        }

        // 其他未知错误
        else -> NetworkError.Other(e.message ?: "Unknown error")
    }
}