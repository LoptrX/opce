package com.ohuo.application.decompose

import com.ohuo.application.services.dto.file.FileSearchInput
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ohuo.application.decompose.viewmodel.FileManagementScreenState
import com.ohuo.application.decompose.viewmodel.FileManagementViewModel

class FileManageComponent(
    context: ComponentContext,
    basePath: String = "",
    private val onNavigateUp: () -> Unit,
) : ComponentContext by context {
    val viewModel = if (basePath.isNotBlank()) {
        FileManagementViewModel(
            context,
            FileManagementScreenState(isLoading = true, query = FileSearchInput(path = basePath))
        )
    } else {
        FileManagementViewModel(context)
    }

    init {
        lifecycle.doOnCreate {
            search(true, basePath)
            viewModel.updateState(viewModel.stateFlow.value.copy(isInit = true))
        }
    }

    fun search(isFresh: Boolean, path: String = ROOT_PATH) {
        viewModel.search(isFresh, path)
    }

    fun navigateUp() = onNavigateUp()

    companion object {
        const val ROOT_PATH = "/"
    }
}