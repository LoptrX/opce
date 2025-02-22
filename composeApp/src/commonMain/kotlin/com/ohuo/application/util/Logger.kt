package com.ohuo.application.util

expect fun debug(tag: String = "", message: String)
expect fun info(tag: String = "", message: String)
expect fun warn(tag: String = "", message: String)
expect fun error(tag: String = "", message: String)