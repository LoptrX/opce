package com.ohuo.application.pages.me

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ohuo.application.decompose.MeComponent

@Composable
fun MeScreen(component: MeComponent, snackBarHostState: SnackbarHostState) {
    val viewModel = component.viewModel
    val state by viewModel.stateFlow.collectAsState()
    
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = statusBarPadding.calculateTopPadding() + 16.dp,
                    bottom = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display user profile information
            if (state.isLoggedIn) {
                // User is logged in, show profile
                Text("用户名: ${state.userName}")
                Text("邮箱: ${state.userEmail}")
                // Add more UI components as needed
            } else {
                // User is not logged in, show login prompt
                Text("请登录查看个人信息")
                // Add login button or other UI components
            }
        }
    }
}