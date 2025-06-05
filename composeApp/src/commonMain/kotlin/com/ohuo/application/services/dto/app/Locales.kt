package com.ohuo.application.services.dto.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Locales(
    val en: String = "",
    val ja: String = "",
    val ko: String = "",
    val ms: String = "",
    @SerialName("pt-br") val ptBr: String = "",
    val ru: String = "",
    val zh: String = "",
    @SerialName("zh-hant") val zhHant: String = ""
)