package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ohuo.application.decompose.viewmodel.ManagementsViewModel

class ManagementsComponent(
    context: ComponentContext,
    private val onNavigateApps: () -> Unit
) : ComponentContext by context {
    val viewModel = ManagementsViewModel(context)

    init {
        lifecycle.doOnCreate {
            viewModel.loadServers()
        }
    }

    fun navigateApps() {
        onNavigateApps()
    }
}