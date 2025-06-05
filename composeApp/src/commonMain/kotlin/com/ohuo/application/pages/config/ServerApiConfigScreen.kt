package com.ohuo.application.pages.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ohuo.application.components.OButton
import com.ohuo.application.components.show
import com.ohuo.application.decompose.ServerApiConfigComponent
import com.ohuo.application.pages.config.dto.TempAuthDTO
import com.ohuo.application.services.Result
import com.ohuo.application.services.dto.OPResult
import com.ohuo.application.services.initial
import com.ohuo.application.services.safeRequest
import com.ohuo.application.services.settingsSearch
import com.ohuo.application.updateServerApiConfig
import com.ohuo.application.util.info
import com.ohuo.application.util.md5
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun ServerApiConfigScreen(
    component: ServerApiConfigComponent,
    snackBarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val viewModel = component.viewModel
    val uiState = viewModel.stateFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var loading by remember { mutableStateOf(false) }
        var serverUrlErr: String? by remember { mutableStateOf(null) }
        var apiKeyErr: String? by remember { mutableStateOf(null) }
        var aliasErr: String? by remember { mutableStateOf(null) }
        var timeErr: String? by remember { mutableStateOf(null) }
        TextField(
            value = uiState.value.config.serverUrl,
            singleLine = true,
            isError = serverUrlErr != null,
            supportingText = {
                Row {
                    Text(serverUrlErr ?: "",
                        Modifier.clearAndSetSemantics {})
                }
            },
            onValueChange = {
                viewModel.update(uiState.value.config.copy(serverUrl = it))
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
            value = uiState.value.config.apiKey,
            singleLine = true,
            isError = apiKeyErr != null,
            supportingText = {
                Row {
                    Text(apiKeyErr ?: "",
                        Modifier.clearAndSetSemantics {})
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                viewModel.update(uiState.value.config.copy(apiKey = it))
                apiKeyErr = null
            },
            label = { Text("API Key") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = uiState.value.config.time.toString(),
            singleLine = true,
            isError = timeErr != null,
            supportingText = {
                Row {
                    Text(timeErr ?: "",
                        Modifier.clearAndSetSemantics {})
                }
            },
            onValueChange = {
                viewModel.update(uiState.value.config.copy(time = it.toLongOrNull() ?: 0))
                timeErr = null
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = { Text("时间戳") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = uiState.value.config.alias,
            singleLine = true,
            isError = aliasErr != null,
            supportingText = {
                Row {
                    Text(aliasErr ?: "",
                        Modifier.clearAndSetSemantics {})
                }
            },
            onValueChange = {
                viewModel.update(uiState.value.config.copy(alias = it))
                aliasErr = null
            },
            label = { Text("别名") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        OButton(
            loading = loading,
            loadingText = "验证中",
            onClick = {
                if (loading) {
                    return@OButton
                }
                loading = true
                // 在 onClick 中调用
                val errors = viewModel.validateInput()
                if (errors.isNotEmpty()) {
                    serverUrlErr = errors["serverUrl"]
                    apiKeyErr = errors["apiKey"]
                    aliasErr = errors["alias"]
                    timeErr = errors["time"]
                    loading = false
                    return@OButton
                }
                scope.launch(Dispatchers.IO) {
                    val seconds = Clock.System.now().epochSeconds.toString()
                    val serverUrl = uiState.value.config.serverUrl
                    val token = md5("1panel${uiState.value.config.apiKey}${seconds}")
                    info(message = "Sign 1panel api key sign, data: $uiState, token: $token")

                    val data = TempAuthDTO(serverUrl = serverUrl, token, seconds)

                    val res: Result<HttpResponse> = safeRequest {
                        initial(data)
                    }
                    if (res.isFailure) {
                        snackBarHostState.show("验证失败", duration = SnackbarDuration.Short)
                        return@launch
                    }
                    if (res.getOrNull()?.status?.isSuccess() == false) {
                        snackBarHostState.show("验证失败", duration = SnackbarDuration.Short)
                        return@launch
                    }
                    val isInit: Boolean? = res.getOrNull()?.body()
                    if (isInit == true) {
                        snackBarHostState.show("验证成功", duration = SnackbarDuration.Short)
                        viewModel.saveConfig(uiState.value.config) {
                            updateServerApiConfig(uiState.value.config)
                            snackBarHostState.currentSnackbarData?.dismiss()
                            component.navigateUp()
                        }
                    } else {
                        snackBarHostState.show("验证失败", duration = SnackbarDuration.Short)
                    }
                }.invokeOnCompletion {
                    loading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("添加服务器")
        }

    }

}
