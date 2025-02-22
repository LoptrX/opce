package com.ohuo.application.pages.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ohuo.application.GlobalState
import com.ohuo.application.constant.CURRENT_SERVER
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.database.repository.getPriority
import com.ohuo.application.pages.home.dto.OS
import com.ohuo.application.route.Screen
import com.ohuo.application.services.dashboardOS
import com.ohuo.application.services.settingsSearch
import com.ohuo.application.updateServerApiConfig
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreen(navigator: NavHostController, snackBarHostState: SnackbarHostState) {
    var settings by remember { mutableStateOf(HashMap<String, Any?>()) }
    var os by remember { mutableStateOf(OS()) }
    var server = GlobalState[CURRENT_SERVER] as ServerApiConfig?
    val scope = rememberCoroutineScope()

    scope.launch(Dispatchers.IO) {
        val serverApiConfig = getPriority()
        if (serverApiConfig == null) {
            withContext(Dispatchers.Main) {
              navigator.navigate(Screen.ServerApiConfig(0))
            }
        } else {
            updateServerApiConfig(serverApiConfig)
            val res = settingsSearch()
            if (!res.status.isSuccess()) {
//            snackbarHostState.showSnackbar(res.bodyAsBytes())
            }

            val osRes = dashboardOS()
            if (!osRes.status.isSuccess()) {
//         snackBarHostState.showSnackbar(res.bodyAsText())
            } else {
                os = Json.decodeFromString(res.bodyAsText())
            }
        }

    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(modifier = Modifier.height(80.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = server?.alias ?: "",
                        style = MaterialTheme.typography.titleLarge // 标题样式
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // 添加间距
                    Text(
                        text = server?.serverUrl ?: "",
                        style = MaterialTheme.typography.titleSmall // 内容样式
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    IconButton(onClick = {
                        val config = GlobalState[CURRENT_SERVER] as ServerApiConfig
                        navigator.navigate(Screen.ServerApiConfig(config.id))
                    }) {
                        Icon(
                            Icons.Default.EditNote,
                            "编辑",
                        )
                    }
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "title",
                style = MaterialTheme.typography.titleLarge // 标题样式
            )
        }
    }
}