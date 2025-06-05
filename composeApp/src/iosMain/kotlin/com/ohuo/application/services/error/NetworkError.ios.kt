package com.ohuo.application.services.error

import platform.Foundation.NSURLErrorCannotConnectToHost
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorSecureConnectionFailed
import platform.Foundation.NSURLErrorTimedOut

actual fun convertException(e: Throwable): NetworkError {
    // 通过NSError属性判断错误类型
    val nsError = e.cause?.cause as platform.Foundation.NSError?
    return when {
        // 域名解析失败（iOS可能包含多种错误码）
        e.isUnknownHostError() -> NetworkError.UnknownHost

        // 连接超时
        nsError?.domain == NSURLErrorDomain && nsError?.code == NSURLErrorTimedOut -> NetworkError.Timeout

        // SSL错误
        nsError?.domain == NSURLErrorDomain && nsError?.code == NSURLErrorSecureConnectionFailed ->
            NetworkError.SSLHandshakeError(nsError.localizedDescription)

        // HTTP错误（需要手动处理，iOS不自动抛出）
        e is io.ktor.client.plugins.ResponseException ->
            NetworkError.HttpError(e.response.status.value)

        // 其他情况
        else -> NetworkError.Other(e.message ?: "Unknown error")
    }
}

// 扩展函数判断具体错误

// 扩展函数判断域名解析失败（iOS需要组合判断）
private fun Throwable.isUnknownHostError(): Boolean {
    val nsError = this.cause?.cause as platform.Foundation.NSError?
    return when {
        // 直接域名解析失败
        this.message?.contains("hostname lookup failed") == true -> true

        // 间接错误码判断
        nsError?.domain == NSURLErrorDomain && nsError?.code == NSURLErrorCannotConnectToHost -> true

        else -> false
    }
}
private fun Throwable.isHostUnreachable(): Boolean {
    return message?.contains("host is unreachable") == true
}

private fun Throwable.isTimeOut(): Boolean {
    return message?.contains("host is unreachable") == true
}