package com.ohuo.application.services.error

// commonMain/kotlin
sealed class NetworkError {
    data object UnknownHost : NetworkError()      // 域名解析失败
    data object Timeout : NetworkError()         // 连接或读取超时
    data class HttpError(val statusCode: Int) : NetworkError()  // HTTP错误状态码
    data class SSLHandshakeError(val message: String?) : NetworkError()  // SSL错误
    data class Other(val message: String?) : NetworkError()     // 其他未知错误
}

// commonMain/kotlin
expect fun convertException(e: Throwable): NetworkError