package com.ohuo.application.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

val mutex = Mutex()
suspend fun SnackbarHostState.show(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration =
        if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
): SnackbarResult {
    return mutex.withLock {
        withContext(Dispatchers.Main) {
            this@show.currentSnackbarData?.dismiss()
            this@show.showSnackbar(message, actionLabel, withDismissAction, duration)
        }
    }

}