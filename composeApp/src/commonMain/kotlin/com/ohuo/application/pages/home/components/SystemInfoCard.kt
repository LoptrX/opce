package com.ohuo.application.pages.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohuo.application.components.OCard
import com.ohuo.application.services.dto.SystemInfo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SystemInfoCard(systemInfo: SystemInfo) {
    val textStyle =
        MaterialTheme.typography.bodyLarge
            .copy(fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 40.sp)
    FlowRow(
        maxItemsInEachRow = 2
    ) {
        CardItem("网站", systemInfo.websiteNumber, textStyle)
        CardItem("应用", systemInfo.appInstalledNumber, textStyle)
        CardItem("数据库", systemInfo.databaseNumber, textStyle)
        CardItem("容器", 0, textStyle)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowScope.CardItem(
    title: String,
    numeric: Int,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
        .weight(1f)
        .height(120.dp)
        .padding(8.dp)
) {
    Box(
        modifier = modifier,
    ) {
        OCard(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Surface(Modifier.fillMaxHeight()
                .fillMaxWidth().clickable {}, shape = RoundedCornerShape(8.dp)) {
                Column(Modifier.padding(8.dp)) {
                    Row(Modifier.fillMaxHeight(.4f)) { Text(text = title) }
                    Row(Modifier.fillMaxHeight(.6f)) {
                        Text(text = "$numeric", style = textStyle)
                    }
                }
            }
        }
    }
}