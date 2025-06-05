package com.ohuo.application.decompose.viewmodel

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

/**
 * Data class representing the state of the Me screen.
 */
@Serializable
data class MeScreenState(
    val userName: String = "",
    val userEmail: String = "",
    val isLoggedIn: Boolean = false,
    val avatarUrl: String = ""
)

/**
 * ViewModel for the Me screen using Decompose architecture
 */
class MeViewModel(
    context: ComponentContext,
    initialState: MeScreenState = MeScreenState()
) : DecomposeViewModel<MeScreenState>(
    context = context,
    initialState = initialState
) {

    
    /**
     * Updates user profile information
     */
    fun updateProfile(userName: String, userEmail: String, avatarUrl: String) {
        val currentState = stateFlow.value
        val newState = currentState.copy(
            userName = userName,
            userEmail = userEmail,
            avatarUrl = avatarUrl
        )
        updateState(newState)
    }
    
    /**
     * Updates login status
     */
    fun updateLoginStatus(isLoggedIn: Boolean) {
        val currentState = stateFlow.value
        val newState = currentState.copy(isLoggedIn = isLoggedIn)
        updateState(newState)
    }
    
    /**
     * Loads user profile data
     */
    fun loadProfile() {
        // Implementation would load user data from a repository
        // For now, this is a placeholder
    }
}