package com.ohuo.application.util


fun Long.toGb():Double = this.div(1024.0 * 1024.0 * 1024.0)

expect fun  formatDecimal(number: Double, decimalPlaces: Int): String

fun formatSize(bytes: Long): String {
    require(bytes >= 0) { "Bytes cannot be negative." }

    if (bytes == 0L) return "0 B"

    val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    var size = bytes.toDouble()
    var unitIndex = 0

    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }

    val formattedSize = formatDecimal(size,0)

    return "$formattedSize ${units[unitIndex]}"
}

fun desensitizeDomain(domain: String): String {
    // 优化正则：精确匹配主域名和多级 TLD
    val regex = Regex("""^(?:(.*?)\.)?([a-zA-Z0-9-]+)((?:\.(?:[a-zA-Z0-9-]{2,})+))$""", RegexOption.IGNORE_CASE)
    return regex.replace(domain) { matchResult ->
        val (sub, main, tld) = matchResult.destructured
        val adjustedSub = sub?.takeIf { it.isNotEmpty() }?.let { "$it." } ?: ""
        "$adjustedSub***$tld"
    }
}