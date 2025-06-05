package com.ohuo.application.pages.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ohuo.application.components.LoadingPage
import com.ohuo.application.decompose.AppListComponent
import com.ohuo.application.services.dto.app.AppInstalledSearch
import com.ohuo.application.services.dto.app.AppItem

@Composable
fun AppListScreen(context: AppListComponent) {
    val state = context.viewModel.stateFlow.collectAsState()
    when (state.value.isLoading) {
        true -> LoadingPage()
        false -> AppList(state.value.apps, context)
    }
}

@Composable
fun AppList(apps: AppInstalledSearch, context: AppListComponent) {
    LazyColumn(Modifier.fillMaxSize()) {
        apps.items.forEach {
            item {
                AppItem(context, it)
            }
        }
    }
}

@Composable
private fun AppItem(
    context: AppListComponent,
    item: AppItem
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { context.navigateToAppDetail(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = "data:image/png;base64,${item.icon}",
                contentDescription = item.name
            )
        }
        Column(
            Modifier.padding(start = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(0.7f)) {
                    Text(item.name, style = MaterialTheme.typography.titleLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.version, style = MaterialTheme.typography.bodySmall)
                        if (item.canUpdate) {
                            Surface(
                                color = MaterialTheme.colorScheme.error,              // 背景颜色
                                shape = MaterialTheme.shapes.small, // 圆角
                                modifier = Modifier.padding(8.dp).width(45.dp),
                            ) {
                                Text(
                                    "可更新",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onError,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }
                }
                Column(
                    Modifier.weight(0.3f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(item.status, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}