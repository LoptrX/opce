package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.services.appInstalledSearch
import com.ohuo.application.services.dto.OPResult.AppInstalledSearchRes
import com.ohuo.application.services.dto.app.AppInstalledSearch
import com.ohuo.application.services.dto.app.Query
import com.ohuo.application.services.ok
import com.ohuo.application.util.scope
import kotlinx.coroutines.launch

data class AppListScreenState(
    val isLoading: Boolean = false,
    val query: Query = Query(),
    val apps: AppInstalledSearch = AppInstalledSearch()
)

class AppListViewModel(
    context: ComponentContext,
    initialState: AppListScreenState = AppListScreenState(isLoading = true)
) : DecomposeViewModel<AppListScreenState>(
    context = context,
    initialState = initialState
) {

    fun search() {
        lifecycle.scope.launch {
            ok<AppInstalledSearchRes, AppInstalledSearch?> { appInstalledSearch(state.query) }?.let {
                updateState(state.copy(apps = it, isLoading = false))
            } ?: run {
                updateState(state.copy(isLoading = false))
            }
        }
    }
}