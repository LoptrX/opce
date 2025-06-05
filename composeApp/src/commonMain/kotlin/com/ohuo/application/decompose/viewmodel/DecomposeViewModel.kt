package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.ohuo.application.util.scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * Base ViewModel class for Decompose architecture
 * 
 * @param context The Decompose ComponentContext
 * @param initialState The initial state of the ViewModel
 * @param stateKey The key used to save and restore state (defaults to class name)
 */
@OptIn(InternalSerializationApi::class)
abstract class DecomposeViewModel<State : Any>(    
    protected val context: ComponentContext,
    initialState: State
) : ComponentContext by context {
    private val stateKey by lazy { 
        DecomposeViewModel::class.simpleName + "_" + this::class.simpleName
    }
    
    protected val coroutineScope = context.lifecycle.scope
    
    override val stateKeeper: StateKeeper = context.stateKeeper

    protected val _stateFlow = MutableStateFlow<State>(initialState)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()
    
    open var state: State = if (initialState is Serializable) {
        @Suppress("UNCHECKED_CAST")
        stateKeeper.consume(stateKey, initialState::class.serializer() as KSerializer<State>) ?: initialState
    } else {
        initialState
    }

    init {
        if (state is Serializable) {
            @Suppress("UNCHECKED_CAST")
            val serializer = state::class.serializer() as KSerializer<State>
            stateKeeper.register(stateKey, serializer) { state }
            
            lifecycle.doOnDestroy {
                stateKeeper.unregister(stateKey)
            }
        }
    }
    
    @OptIn(InternalSerializationApi::class)
    open fun updateState(newState: State) {
        state = newState
        _stateFlow.value = newState
    }
}