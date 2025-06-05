package com.ohuo.application.pages.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ohuo.application.components.LoadingPage
import com.ohuo.application.decompose.AppDetailComponent
import com.ohuo.application.decompose.viewmodel.AppDetailScreenState
import com.ohuo.application.pages.manager.dto.OperateInput
import com.ohuo.application.services.appInstalledOp
import com.ohuo.application.services.dto.OPResult
import com.ohuo.application.services.dto.app.AppItem
import com.ohuo.application.services.ok
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(component: AppDetailComponent) {
    val viewModel = component.viewModel
    val state = viewModel.stateFlow.collectAsState()

    when (state.value.isLoading) {
        true -> LoadingPage()
        false -> {
            AppDetailPage(state.value, component::navigateToFM, viewModel::refresh)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDetailPage(
    state: AppDetailScreenState,
    onNavigateToFM: (String) -> Unit, onRefresh: suspend () -> Unit
) {
    // 使用 ViewModel 的加载状态作为刷新状态
    var refreshing = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = refreshing.value,
        state = pullRefreshState,
        onRefresh = {
            scope.launch {
                // 调用刷新方法
                onRefresh()
                pullRefreshState.animateToHidden()
            }
        }) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Item("Status", state.app.status)
            }
            item {
                Item("Https Port", state.app.httpsPort.toString())
            }
            item {
                Item("Http Port", state.app.httpPort.toString())
            }
            item {
                Item("Container", state.app.name)
            }
            item {
                Item("Created At", state.app.createTime)
            }
            item {
                Item("Path", state.app.path) {
                    onNavigateToFM(state.app.path)
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.Item(title: String, content: String, onClick: (() -> Unit)? = null) {
    val modifier = onClick?.let { Modifier.clickable { it.invoke() } } ?: Modifier
    Row(
        modifier.fillMaxWidth().height(80.dp).padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row { Text(title, style = MaterialTheme.typography.titleMedium) }
            Row {
                Text(
                    content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column {
            onClick?.let {
                Row {
                    Icon(Icons.AutoMirrored.Filled.ArrowRight, "Go")
                }
            }
        }
    }
}


@Composable
fun AppDetailMenus(scope: CoroutineScope, app: AppItem, onRefresh: suspend () -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    // 触发菜单的按钮
    val colors = IconButtonDefaults.filledIconButtonColors()
    FilledIconButton(
        colors = colors.copy(containerColor = MaterialTheme.colorScheme.primary),
        onClick = { expanded.value = !expanded.value }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Open menu")
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            text = { Text("开机") },
            enabled = app.status != "Running",
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "start",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("关机") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "stop",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("重启") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "restart",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("同步") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "sync",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("重建") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "rebuild",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("卸载") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "uninstall",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("编辑") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "edit",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )
        DropdownMenuItem(
            text = { Text("备份") },
            onClick = {
                scope.launch {
                    ok<OPResult.AppOpRes, Map<String, String?>?> {
                        appInstalledOp(
                            OperateInput(
                                app.id,
                                "backup",
                            )
                        )
                    }?.run {
                        expanded.value = false
                        onRefresh()
                    }
                }
            }
        )

    }
}