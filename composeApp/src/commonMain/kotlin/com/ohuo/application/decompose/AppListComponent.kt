package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.ohuo.application.decompose.viewmodel.AppListViewModel
import com.ohuo.application.services.dto.app.AppItem
import com.ohuo.application.util.scope
import kotlinx.coroutines.launch

class AppListComponent(
    context: ComponentContext,
    private val onNavigateUp: () -> Unit,
    private val onNavigateToAppDetail: (AppItem, () -> Unit) -> Unit,
) : ComponentContext by context {
    val viewModel = AppListViewModel(context)

    init {
        lifecycle.doOnCreate {
            lifecycle.scope.launch {
                viewModel.search()
            }
        }
        lifecycle.doOnResume {
            lifecycle.scope.launch {
                viewModel.search()
            }
        }
    }

    fun navigateUp() = onNavigateUp()

    fun navigateToAppDetail(app: AppItem) = onNavigateToAppDetail(app, this.viewModel::search)
}