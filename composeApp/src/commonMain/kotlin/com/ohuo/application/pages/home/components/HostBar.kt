package com.ohuo.application.pages.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ohuo.application.database.model.ServerApiConfig
import com.ohuo.application.util.desensitizeDomain


@Composable
fun HostBar(
    server: ServerApiConfig?,
    onNavigateToSwitchServer: () -> Unit
) {
    Row(modifier = Modifier.height(120.dp).background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.Bottom) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = server?.alias ?: "",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = server?.serverUrl?.let { desensitizeDomain(it) } ?: "",
                style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row {
                IconButton(onClick = { onNavigateToSwitchServer() }) {
                    Icon(
                        Icons.Default.SwapHoriz,
                        "切换服务器",
                        tint = Color.White
                    )
                }
            }
        }
    }
}