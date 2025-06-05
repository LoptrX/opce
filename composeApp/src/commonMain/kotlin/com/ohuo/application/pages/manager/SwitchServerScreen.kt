package com.ohuo.application.pages.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.ohuo.application.decompose.SwitchServerComponent
import com.ohuo.application.decompose.viewmodel.ServerStateInfo
import com.ohuo.application.pages.home.components.PerformanceIndexCard

@Composable
@OptIn(ExperimentalDecomposeApi::class)
fun SwitchServerScreen(component: SwitchServerComponent,
                       snackBarHostState: SnackbarHostState) {
    val viewModel = component.viewModel
    val state by viewModel.stateFlow.collectAsState()
    Box(Modifier.fillMaxSize()) {
        when (state.isLoading) {
            true -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            false -> Content(state.serverList, component)
        }
    }
}

@Composable
fun Content(
    serverList: List<ServerStateInfo>,
    component: SwitchServerComponent
) {
    LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
        serverList.forEachIndexed {i, it->
            item {
                Box {
                    PerformanceIndexCard(Modifier.clickable {
                        component.onServerSelected(it.server)
                        component.navigateUp()
                    },it.server.alias, it.dashboard)
                }
            }
        }
    }
}