package com.ohuo.application.pages.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ohuo.application.components.OCard
import com.ohuo.application.decompose.HomeComponent
import com.ohuo.application.pages.home.components.OSCard
import com.ohuo.application.pages.home.components.PerformanceIndexCard
import com.ohuo.application.pages.home.components.SystemInfoCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreen(
    component: HomeComponent,
    snackBarHostState: SnackbarHostState
) {
    val uiState by component.viewModel.stateFlow.collectAsState()
    DisposableEffect(Unit) {
        component.viewModel.load()
        onDispose {
            component.viewModel.timerManager.stopPeriodicRefresh()
        }
    }
    LazyColumn(Modifier.fillMaxSize().padding(bottom = 16.dp)) {
        item {
            SystemInfoCard(uiState.systemInfo)
        }
        item {
            PerformanceIndexCard(
                Modifier.padding(10.dp),
                header = "状态",
                data = uiState.dashboardCurrent
            )
        }
        item {
            OCard(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth().padding(10.dp)
            ) {
                Row(
                    Modifier.height(40.dp).fillMaxWidth().padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.settingsUpgrade?.isUpdateAvailable(uiState.settings?.systemVersion) == true) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    modifier = Modifier.offset(x = 15.dp),
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(text = "new")
                                }
                            }
                        ) {
                            Text(uiState.settings?.systemVersion ?: "UNKNOWN")
                        }
                    } else {
                        Text(uiState.settings?.systemVersion ?: "UNKNOWN")
                    }
                }
            }
        }
        item {
            OSCard(uiState.systemInfo)
        }
    }
}



