package com.ohuo.application.pages.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ohuo.application.GlobalState
import com.ohuo.application.constant.PANEL_TIMESTAMP
import com.ohuo.application.constant.PANEL_TOKEN
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.database.repository.findById
import com.ohuo.application.database.repository.save
import com.ohuo.application.route.Screen
import com.ohuo.application.services.settingsSearch
import com.ohuo.application.updateServerApiConfig
import com.ohuo.application.util.info
import com.ohuo.application.util.isHttpsDomain
import com.ohuo.application.util.md5
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.collections.set

@Composable
fun ServerApiConfigScreen(
    id: Long?,
    navigator: NavHostController,
    tipsState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val data = id?.let { findById(id) }
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var serverUrl by remember { mutableStateOf(data?.serverUrl ?: "") }
        var serverUrlErr: String? by remember { mutableStateOf(null) }
        var apiKey by remember { mutableStateOf(data?.apiKey ?: "") }
        var apiKeyErr: String? by remember { mutableStateOf(null) }
        var alias by remember { mutableStateOf(data?.alias ?: "") }
        var aliasErr: String? by remember { mutableStateOf(null) }
        val time by remember { mutableStateOf(Clock.System.now().epochSeconds) }

        TextField(
            value = serverUrl,
            singleLine = true,
            isError = serverUrlErr != null,
            onValueChange = {
                serverUrl = it
                serverUrlErr = null
            },
            label = { Text("服务器 URL") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )
        if (serverUrlErr != null) {
            Text(
                text = serverUrlErr!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = apiKey,
            singleLine = true,
            isError = apiKeyErr != null,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { apiKey = it },
            label = { Text("API Key") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )
        if (apiKeyErr != null) {
            Text(
                text = apiKeyErr!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = alias,
            singleLine = true,
            isError = aliasErr != null,
            onValueChange = {
                alias = it
                aliasErr = null
            },
            label = { Text("别名") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )
        if (aliasErr != null) {
            Text(
                text = aliasErr!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!isHttpsDomain(serverUrl)) {
                    serverUrlErr = "服务器地址错误"
                    return@Button
                }
                if (apiKey.isBlank()) {
                    apiKeyErr = "请填写正确的ApiKey"
                    return@Button
                }
                if (alias.isBlank()) {
                    aliasErr = "请填写别名"
                    return@Button
                }
                val token = md5("1panel$apiKey$time")
                info(message = "Sign 1panel api key sign, apiKey: $apiKey, time: $time, token: $token")
                GlobalState[PANEL_TOKEN] = token
                GlobalState[PANEL_TIMESTAMP] = time
                scope.launch(Dispatchers.IO) {
                    val res = settingsSearch()
                    if (res.status.isSuccess()) {
                        val config = ServerApiConfig(-1, serverUrl, apiKey, time, alias, 1)
                        updateServerApiConfig(save(config))
                        GlobalState[PANEL_TOKEN] = null
                        GlobalState[PANEL_TIMESTAMP] = null
                    }
                    withContext(Dispatchers.Main) {
                        if (res.status.isSuccess()) {
                            if (!navigator.navigateUp()) {
                                navigator.navigate(Screen.Home)
                            }
                            tipsState.showSnackbar("验证成功", duration = SnackbarDuration.Short)
                        } else {
                            tipsState.showSnackbar("验证失败", duration = SnackbarDuration.Long)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("添加服务器")
        }

    }

}