package com.ohuo.application.util

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform