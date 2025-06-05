package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.services.appInstalledSearch
import com.ohuo.application.services.dto.OPResult.AppInstalledSearchRes
import com.ohuo.application.services.dto.app.AppDetail
import com.ohuo.application.services.dto.app.AppInstalledSearch
import com.ohuo.application.services.dto.app.AppItem
import com.ohuo.application.services.dto.app.Query
import com.ohuo.application.services.ok
import kotlinx.serialization.Serializable

@Serializable
data class AppDetailScreenState(
    val isLoading: Boolean,
    val app: AppItem = AppItem(),
    val appDetail: AppDetail = AppDetail()
)

class AppDetailViewModel(
    context: ComponentContext,
    initialState: AppDetailScreenState = AppDetailScreenState(isLoading = true)
) : DecomposeViewModel<AppDetailScreenState>(
    context = context,
    initialState = initialState
) {

    suspend fun refresh() {
        ok<AppInstalledSearchRes, AppInstalledSearch?> {
            appInstalledSearch(
                Query().copy(
                    name = state.app.name
                )
            )
        }?.let {
            updateState(state.copy(app = it.items.first(), isLoading = false))
        } ?: run {
            updateState(state.copy(isLoading = false))
        }
    }
}