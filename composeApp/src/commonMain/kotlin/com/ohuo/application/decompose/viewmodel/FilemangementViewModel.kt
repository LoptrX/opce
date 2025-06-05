package com.ohuo.application.decompose.viewmodel

import com.ohuo.application.services.dto.file.FileSearchInput
import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.services.dto.OPResult
import com.ohuo.application.services.dto.file.FileItem
import com.ohuo.application.services.fileSearch
import com.ohuo.application.services.ok
import com.ohuo.application.util.scope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class FileManagementScreenState(
    val isLoading: Boolean = false,
    val isInit:Boolean = false,
    val query: FileSearchInput = FileSearchInput(),
    val file: FileItem = FileItem()
)

class FileManagementViewModel(
    context: ComponentContext,
    initialState: FileManagementScreenState = FileManagementScreenState(isLoading = true)
) : DecomposeViewModel<FileManagementScreenState>(
    context = context,
    initialState = initialState
) {
    fun search(isFresh: Boolean = false, path: String?) {
        val (isInit, isLoading) = stateFlow.value
        if (isInit && isLoading) return
        updateState(stateFlow.value.copy(isLoading = true))
        if (isFresh) {
            updateState(stateFlow.value.copy(query = stateFlow.value.query.copy(page = 0)))
        }
        if (path != null) {
            updateState(stateFlow.value.copy(query = stateFlow.value.query.copy(path = path)))
        }
        lifecycle.scope.launch {
            ok<OPResult.FileSearchRes, FileItem> {
                fileSearch(stateFlow.value.query)
            }.let {
                updateState(
                    stateFlow.value.copy(
                        isLoading = false,
                        file = it ?: stateFlow.value.file,
                    )
                )
            }
        }
    }
}