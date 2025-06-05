package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ohuo.application.decompose.viewmodel.AppDetailScreenState
import com.ohuo.application.decompose.viewmodel.AppDetailViewModel
import com.ohuo.application.services.dto.app.AppItem

class AppDetailComponent(
    context: ComponentContext,
    val params: AppItem?,
    private val onNavigateUp: () -> Unit,
    private val onNavigateToFM: (String) -> Unit,
) : ComponentContext by context {
    val viewModel = if (params != null) {
        AppDetailViewModel(context, AppDetailScreenState(isLoading = false, params))
    } else {
        AppDetailViewModel(context)
    }

    init {
        lifecycle.doOnCreate {
            if (params == null) {
                onNavigateUp()
            }
        }
    }

    fun navigateUp() = onNavigateUp()

    fun navigateToFM(basePath:String) = onNavigateToFM(basePath)

}