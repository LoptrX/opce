package com.ohuo.application.util

import java.math.BigDecimal
import java.math.RoundingMode

actual fun formatDecimal(number: Double, decimalPlaces: Int): String {
    return BigDecimal(number).setScale(decimalPlaces, RoundingMode.HALF_UP).toString()
}