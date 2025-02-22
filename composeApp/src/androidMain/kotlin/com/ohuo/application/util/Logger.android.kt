package com.ohuo.application.util

import android.util.Log

actual fun debug(tag: String, message: String) {
    Log.d(tag, message)
}

actual fun info(tag: String, message: String) {
    Log.i(tag, message)
}

actual fun warn(tag: String, message: String) {
    Log.w(tag, message)
}

actual fun error(tag: String, message: String) {
    Log.e(tag, message)
}