package com.example.androiddemo.flow.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddemo.flow.model.FlowGame
import com.example.androiddemo.flow.model.Level

@Composable
internal fun GameScreen(
    level: Level,
    title: String,
    solvedMessage: String,
    nextLabel: String?,
    nextAlwaysVisible: Boolean,
    onSolved: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val game = remember(level) { FlowGame(level) }
    val solved = game.isSolved
    LaunchedEffect(solved) { if (solved) onSolved() }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
        )
        val total = level.size * level.size
        Row(
            Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Stat("flows", "${game.connectedCount}/${level.endpoints.size}")
            Stat("pipe", "${game.filledCount * 100 / total}%")
        }

        FlowBoard(game, Modifier.fillMaxWidth().aspectRatio(1f))

        Spacer(Modifier.height(24.dp))
        if (solved) {
            Text(
                solvedMessage,
                color = DOT_COLORS.getValue('G'),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(12.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack) { Text("Levels") }
            OutlinedButton(onClick = game::reset) { Text("Reset") }
            if (nextLabel != null && (solved || nextAlwaysVisible)) {
                Button(onClick = onNext) { Text(nextLabel) }
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
    }
}
