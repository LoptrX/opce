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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohuo.application.components.OCard
import com.ohuo.application.components.OIndicator
import com.ohuo.application.services.dto.DashboardCurrent
import com.ohuo.application.util.formatDecimal
import com.ohuo.application.util.formatSize


@Composable
fun PerformanceIndexCard(
    modifier: Modifier = Modifier, header: String, data: DashboardCurrent) {
    val textStyle = MaterialTheme.typography.bodySmall.copy(lineHeight = 28.sp)
    OCard(modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = header,
                style = MaterialTheme.typography.titleSmall // 标题样式
            )
            Spacer(modifier = Modifier.height(8.dp)) // 添加间距
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier,
                ) {
                    // 圆形进度条
                    val percent = formatDecimal(data.cpuUsedPercent / 100, 2).toFloat()
                    OIndicator(
                        process = percent,
                        description = "CPU",
                    )
                    val cpuUsed = formatDecimal(data.cpuUsed, 2)
                    Text(text = "${cpuUsed}/${data.cpuTotal} Core", style = textStyle)
                }
                Column(
                    modifier = Modifier,
                ) {
                    // 圆形进度条
                    val percent = formatDecimal(data.memoryUsedPercent / 100, 2).toFloat()
                    OIndicator(
                        process = percent,
                        description = "Mem",
                    )
                    Text(
                        text = "${formatSize(data.memoryUsed)}/${formatSize(data.memoryTotal)}",
                        style = textStyle
                    )
                }
                Column(
                   horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 圆形进度条
                    val percent = formatDecimal(data.loadUsagePercent / 100, 2).toFloat()
                    OIndicator(
                        process = percent,
                        description = "Load",
                    )
                    Text(text = "/")
                }
                Column(
                    modifier = Modifier,
                ) {
                    val diskData = data.diskData
                    // 圆形进度条
                    val usedPercent = diskData?.first()?.usedPercent ?: 0.0
                    val percent = formatDecimal(usedPercent / 100, 2).toFloat()
                    OIndicator(
                        process = percent,
                        description = "Disk",
                    )
                    val used = diskData?.map { it.used }?.reduce(Long::plus) ?: 0
                    val total = diskData?.map { it.total }?.reduce(Long::plus) ?: 0
                    Text(text = "${formatSize(used)}/${formatSize(total)}", style = textStyle)
                }
            }
        }
    }
}