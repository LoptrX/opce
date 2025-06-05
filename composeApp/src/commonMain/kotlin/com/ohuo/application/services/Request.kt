package com.ohuo.application.services

import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.decompose.GlobalState
import com.ohuo.application.globalStateComponent
import com.ohuo.application.services.dto.OPResult
import com.ohuo.application.services.error.NetworkError
import com.ohuo.application.services.error.convertException
import com.ohuo.application.util.error
import com.ohuo.application.util.info
import com.ohuo.application.util.md5
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmField
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName

val client = HttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

val panelClient = HttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}.apply {
    plugin(HttpSend).intercept { request ->
        val state = globalStateComponent.state.value
        var token = state.panelToken
        var timestamp = state.panelTimestamp
        val now = Clock.System.now().epochSeconds
        if (token == null || timestamp == null || (now - timestamp > 120 * 60 * 1000)) {
            val pair = generateSign(state, token, timestamp)
            timestamp = pair.first
            token = pair.second
        }

        request.headers {
            append("1Panel-Token", token!!)
            append("1Panel-Timestamp", timestamp.toString())
        }
        execute(request)
    }
}

private fun generateSign(
    state: GlobalState,
    token: String?,
    timestamp: Long?
): Pair<Long?, String?> {
    var token1 = token
    var timestamp1 = timestamp
    val config = state.currentServer
    if (config != null) {
        val seconds = Clock.System.now().epochSeconds
        token1 = md5("1panel${config.apiKey}$seconds")
        info(message = "Sign 1panel api key sign, config: $config token: $token1")
        globalStateComponent.updatePanelAuth(token1, seconds)
        timestamp1 = seconds
    }
    return Pair(timestamp1, token1)
}

@JvmInline
value class Result<out T> @PublishedApi internal constructor(
    @PublishedApi internal val value: Any?
) {
    val isSuccess get() = value !is Failure
    val isFailure get() = value is Failure

    public inline fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value as T
        }

    public fun errorOrNull(): NetworkError? =
        when (value) {
            is Failure -> value.error
            else -> null
        }

    public companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        @JvmName("success")
        public inline fun <T> success(value: T): Result<T> =
            Result(value)

        @JvmName("failure")
        public inline fun <T> failure(error: NetworkError): Result<T> =
            Result(createFailure(error))
    }

    internal class Failure(
        @JvmField
        val error: NetworkError
    ) {
        override fun equals(other: Any?): Boolean = other is Failure && error == other.error
        override fun hashCode(): Int = error.hashCode()
        override fun toString(): String = "Failure($error)"
    }
}

fun createFailure(error: NetworkError): Any =
    Result.Failure(error)

suspend fun safeRequest(block: suspend () -> HttpResponse): Result<HttpResponse> {
    return try {
        val response = block()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(convertException(e))
    }
}

suspend inline fun <reified T : OPResult<E>, E> ok(crossinline block: suspend () -> HttpResponse): E? {
    val result = safeRequest {
        val response = block()
        val text = response.bodyAsText()
        info("Http Response", ": $text")
        response
    }
    if (result.isSuccess && result.getOrNull()?.status?.isSuccess() == true) {
        return (result.getOrNull()?.body() as T).data
    } else {
        error("NET_ERR", result.errorOrNull()?.toString() ?: "")
    }
    return null
}

fun ServerApiConfig.computeToken(): Pair<String, Long> {
    val seconds = Clock.System.now().epochSeconds
    val token = md5("1panel${this.apiKey}$seconds")
    info(message = "Sign 1panel api key sign, config: $this token: $token")
    return token to seconds
}
