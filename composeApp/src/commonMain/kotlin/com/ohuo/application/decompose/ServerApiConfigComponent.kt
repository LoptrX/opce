package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.ohuo.application.decompose.viewmodel.ServerConfigViewModel

class ServerApiConfigComponent(
    context: ComponentContext,
    val id: Long?,
    private val onNavigateUp: () -> Boolean
) : ComponentContext by context {
    
    val viewModel = ServerConfigViewModel(context)
    
    init {
        viewModel.loadConfig(id)
    }
    
    fun navigateUp(): Boolean {
        return onNavigateUp()
    }
}