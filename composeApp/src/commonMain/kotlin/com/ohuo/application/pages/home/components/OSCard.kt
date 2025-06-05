package com.ohuo.application.pages.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohuo.application.components.OCard
import com.ohuo.application.services.dto.SystemInfo

@Composable
fun OSCard(os: SystemInfo) {
    OCard {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "系统信息",
                style = MaterialTheme.typography.titleSmall // 标题样式
            )
            Spacer(modifier = Modifier.height(8.dp)) // 添加间距
            val textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 28.sp)
            Column(  verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("操作系统", style = textStyle)
                    Text(text = os.os, style = textStyle)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("发行版本", style = textStyle)
                    Text(text = os.platform ?: "", style = textStyle)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("内核版本", style = textStyle)
                    Text(text = os.kernelVersion ?: "", style = textStyle)
                }
            }
        }
    }
}