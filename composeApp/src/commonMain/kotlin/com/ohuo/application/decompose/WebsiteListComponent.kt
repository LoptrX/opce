package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.ohuo.application.decompose.viewmodel.WebsiteListScreenState
import com.ohuo.application.decompose.viewmodel.WebsiteListViewModel
import com.ohuo.application.services.dto.app.AppItem
import com.ohuo.application.services.dto.website.SiteItem
import com.ohuo.application.util.scope
import kotlinx.coroutines.launch

class WebsiteListComponent(
    context: ComponentContext,
    private val onNavigateUp: () -> Unit,
    private val onNavigateToWebsiteDetail: (SiteItem, () -> Unit) -> Unit,
) : ComponentContext by context {
     val viewModel = WebsiteListViewModel(context, WebsiteListScreenState(isLoading = true))
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

    fun navigateToAppDetail(item: SiteItem) = onNavigateToWebsiteDetail(item, this.viewModel::search)
}