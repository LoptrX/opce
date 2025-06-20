package com.ohuo.application.util

fun isHttpsDomain(text: String): Boolean {
    return Regex("^https://[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9](?:\\.[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9])*\\.[a-zA-Z]{2,}$")
        .matches(text)
}