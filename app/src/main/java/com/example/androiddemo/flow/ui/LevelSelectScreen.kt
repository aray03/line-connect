package com.example.androiddemo.flow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddemo.flow.model.BoardSize
import com.example.androiddemo.flow.model.Level

@Composable
internal fun LevelSelectScreen(
    size: BoardSize,
    levels: List<Level>,
    completed: Set<Int>,
    randomSolved: Int,
    onSizeChange: (BoardSize) -> Unit,
    onSelect: (Int) -> Unit,
    onRandom: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        val title = "flow"
        val titleColors = listOf('R', 'G', 'B', 'Y')
        Text(
            buildAnnotatedString {
                title.forEachIndexed { i, ch ->
                    withStyle(SpanStyle(color = DOT_COLORS.getValue(titleColors[i]))) { append(ch) }
                }
            },
            fontSize = 64.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(Modifier.height(24.dp))
        SizeTabs(selected = size, onSelect = onSizeChange)
        Spacer(Modifier.height(24.dp))
        levels.chunked(5).forEachIndexed { rowIndex, rowLevels ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowLevels.forEachIndexed { i, level ->
                    val done = level.number in completed
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (done) Color(0xFF1B3A1F) else Color(0xFF1C1C1E))
                            .clickable { onSelect(rowIndex * 5 + i) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "${level.number}",
                            color = if (done) DOT_COLORS.getValue('G') else Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("${completed.size}/${levels.size} solved", color = Color.Gray, fontSize = 16.sp)
        if (!size.requireFill) {
            Text("Connect every pair to win", color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = onRandom, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Random ${size.label} board", fontSize = 18.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text("$randomSolved random ${size.label} boards solved", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun SizeTabs(selected: BoardSize, onSelect: (BoardSize) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1C1C1E))
            .padding(4.dp),
    ) {
        BoardSize.entries.forEach { size ->
            val isSelected = size == selected
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Color(0xFF3A3A3C) else Color.Transparent)
                    .clickable { onSelect(size) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    size.label,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}
