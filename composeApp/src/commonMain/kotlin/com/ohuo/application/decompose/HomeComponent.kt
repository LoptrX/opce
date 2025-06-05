package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStop
import com.ohuo.application.decompose.viewmodel.HomeViewModel
import com.ohuo.application.util.info

class HomeComponent(
    context: ComponentContext,
    private val onNavigateToSwitchServer: () -> Unit,
) : ComponentContext by context {
    val viewModel = HomeViewModel(context)
    
    fun navigateToSwitchServer() {
        onNavigateToSwitchServer()
    }

}