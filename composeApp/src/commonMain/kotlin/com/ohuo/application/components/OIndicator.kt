package com.ohuo.application.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohuo.application.util.formatDecimal

@Composable
fun OIndicator(
    process: Float,
    modifier: Modifier = Modifier.size(70.dp),
    description: String,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
        .copy(textAlign = TextAlign.Center, fontSize = 10.sp, lineHeight = 20.sp),
) {
    val percent = animateFloatAsState(process)
    Box(
        contentAlignment = Alignment.Center, // 内容居中
    ) {
        // 圆形进度条
        CircularProgressIndicator(
            progress = { percent.value },
            gapSize = 0.dp,
            strokeWidth = 6.dp,
            modifier = modifier
        )
        // 居中显示文本
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val present = formatDecimal((process * 100).toDouble(), 2)
            Text(text = "$present%", style = textStyle)
            Text(text = description, style = textStyle)
        }
    }
}