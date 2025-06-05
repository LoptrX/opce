package com.ohuo.application.pages.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ohuo.application.components.LoadingPage
import com.ohuo.application.decompose.FileManageComponent
import com.ohuo.application.decompose.FileManageComponent.Companion.ROOT_PATH
import com.ohuo.application.services.dto.file.FileItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManageScreen(component: FileManageComponent) {
    // 状态管理
    val state = component.viewModel.stateFlow.collectAsState()
    val history = remember { mutableStateListOf<String>() }

    // 下拉刷新状态
    val pullRefreshState = rememberPullToRefreshState()
    var refreshing by remember { mutableStateOf(false) }
    when (state.value.isLoading) {
        true -> LoadingPage()
        false -> FileListPage(state.value.file, component::search)
    }
}


@Composable
fun FileListPage(file: FileItem, searchFn: (Boolean, String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            val lastPath = if (file.path == ROOT_PATH) {
                ""
            } else {
                val path = file.path.substringBeforeLast("/")
                if (path == "") {
                    "/"
                } else {
                    path
                }
            }
            FileItemCard(FileItem().copy(name = "..", path = lastPath), searchFn)
        }
        file.effectiveItems.forEach {
            item {
                FileItemCard(it, searchFn)
            }
        }
    }
}

@Composable
fun FileItemCard(file: FileItem, onClick: (Boolean, String) -> Unit) {
    var modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
    if (file.name == ".." || (file.path.isNotBlank() && file.isDir)) {
        modifier = modifier.clickable {
            onClick(true, file.path)
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp)) {
            Icon(
                if (file.isDir || file.name == "..") Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                contentDescription = if (file.isDir) "文件夹" else "文件",
                tint = if (file.isDir) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (file.path.isNotBlank() && !file.isDir) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatFileSize(file.size),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Column {
                if (file.isDir) {
                    Row {
                        Icon(Icons.AutoMirrored.Filled.ArrowRight, "Go next path")
                    }
                }
            }
        }
    }
}

fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024} KB"
        size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
        else -> "${size / (1024 * 1024 * 1024)} GB"
    }
}