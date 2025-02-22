package com.ohuo.application.util

import java.security.MessageDigest
import kotlin.experimental.and

@OptIn(ExperimentalStdlibApi::class)
actual fun md5(text: String): String {
    val instance = MessageDigest.getInstance("MD5")
    val bytes = instance.digest(text.toByteArray())
    return bytes.map {
        val b = it.and(0xff.toByte()).toHexString()
        if (b.length == 1) "0$b" else b
    }.reduce(String::plus)
}