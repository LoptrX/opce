package com.ohuo.application.services.dto.file

import kotlinx.serialization.Serializable

@Serializable
data class FileItem(
    val path: String = "/",
    val name: String = "/",
    val user: String = "",
    val group: String = "",
    val uid: String = "0",
    val gid: String = "0",
    val extension: String = "",
    val content: String = "",
    val size: Long = 0,
    val isDir: Boolean = false,
    val isSymlink: Boolean = false,
    val isHidden: Boolean = false,
    val linkPath: String = "",
    val type: String = "",
    val mode: String = "",
    val mimeType: String = "",
    val updateTime: String = "",
    val modTime: String = "",
    val items: List<FileItem>? = null,
    val itemTotal: Int = 0,
    val favoriteID: Int = 0,
    val isDetail: Boolean = false
) {
    // 可选：添加便捷访问方法
    val isEmptyDirectory: Boolean
        get() = isDir && (items.isNullOrEmpty())

    val effectiveItems: List<FileItem>
        get() = items ?: emptyList()
}
