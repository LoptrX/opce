package com.ohuo.application

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform