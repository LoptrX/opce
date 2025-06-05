package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.decompose.viewmodel.SwitchServerViewModel

class SwitchServerComponent(
    context: ComponentContext,
    private val onNavigateUp: () -> Boolean
) : ComponentContext by context {
    val viewModel = SwitchServerViewModel(context)

    init {
        lifecycle.doOnCreate {
            viewModel.loadServers()
        }
    }

    fun onServerSelected(server: ServerApiConfig) {
        viewModel.updateCurrentServer(server)
    }

    fun navigateUp(): Boolean {
        return onNavigateUp()
    }
}