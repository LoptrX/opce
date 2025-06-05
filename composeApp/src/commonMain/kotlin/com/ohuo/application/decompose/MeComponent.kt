package com.ohuo.application.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ohuo.application.decompose.viewmodel.MeViewModel

class MeComponent(
    context: ComponentContext
) : ComponentContext by context {
    val viewModel = MeViewModel(context)
    
    init {
        lifecycle.doOnCreate {
            viewModel.loadProfile()
        }
    }
}