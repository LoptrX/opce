package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.services.dto.website.SiteResult
import com.ohuo.application.services.dto.website.WebsiteSearch
import com.ohuo.application.services.ok
import com.ohuo.application.services.websites
import com.ohuo.application.util.scope
import kotlinx.coroutines.launch

data class WebsiteListScreenState(
    val isLoading:Boolean = false,
    val query: WebsiteSearch = WebsiteSearch(),
    val result: SiteResult = SiteResult()
)

class WebsiteListViewModel(
    context: ComponentContext,
    initialState: WebsiteListScreenState = WebsiteListScreenState()
) : DecomposeViewModel<WebsiteListScreenState>(
    context = context,
    initialState = initialState
) {

    fun search() {
        lifecycle.scope.launch {
            ok<AppInstalledSearchRes, AppInstalledSearch?> { websites(state.query) }?.let {
                updateState(state.copy(apps = it, isLoading = false))
            } ?: run {
                updateState(state.copy(isLoading = false))
            }
        }
    }
}