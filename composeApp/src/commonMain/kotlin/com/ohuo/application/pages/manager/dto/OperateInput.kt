package com.ohuo.application.pages.manager.dto

import kotlinx.serialization.Serializable

@Serializable
data class OperateInput(
    val installId: Int,
    val operate: String,
    val detailId: Int = 0,
    val deleteBackup: Boolean? = null,
    val forceDelete: Boolean? = null,
    val deleteDB: Boolean? = null,
)