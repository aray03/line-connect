package com.example.androiddemo.flow.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.androiddemo.flow.data.ProgressStore
import com.example.androiddemo.flow.generator.LevelGenerator
import com.example.androiddemo.flow.levels.LEVEL_PACKS
import com.example.androiddemo.flow.model.BoardSize
import com.example.androiddemo.flow.model.Level
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun FlowApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val progress = remember { ProgressStore(context) }
    var completed by remember { mutableStateOf(BoardSize.entries.associateWith(progress::completedLevels)) }
    var randomSolved by remember { mutableStateOf(BoardSize.entries.associateWith(progress::randomSolved)) }
    var size by rememberSaveable { mutableStateOf(BoardSize.FIVE) }
    var levelIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var randomMode by rememberSaveable { mutableStateOf(false) }
    // Rows of the current generated board joined by '\n', or null while one is being generated.
    var randomRows by rememberSaveable { mutableStateOf<String?>(null) }

    Box(modifier.fillMaxSize().background(Color.Black)) {
        val index = levelIndex
        val pack = LEVEL_PACKS.getValue(size)
        when {
            randomMode -> {
                BackHandler { randomMode = false }
                val rows = randomRows
                if (rows == null) {
                    LaunchedEffect(Unit) {
                        randomRows = withContext(Dispatchers.Default) {
                            LevelGenerator.generate(size).level.rows.joinToString("\n")
                        }
                    }
                    Text(
                        "Generating ${size.label} board…",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    val level = remember(rows) { Level(0, rows.split("\n")) }
                    GameScreen(
                        level = level,
                        title = "Random ${size.label}",
                        solvedMessage = "Solved!",
                        nextLabel = "New board",
                        nextAlwaysVisible = true,
                        onSolved = {
                            val count = randomSolved.getValue(size) + 1
                            randomSolved = randomSolved + (size to count)
                            progress.setRandomSolved(size, count)
                        },
                        onNext = { randomRows = null },
                        onBack = { randomMode = false },
                    )
                }
            }
            index == null -> LevelSelectScreen(
                size = size,
                levels = pack,
                completed = completed.getValue(size),
                randomSolved = randomSolved.getValue(size),
                onSizeChange = { size = it },
                onSelect = { levelIndex = it },
                onRandom = {
                    randomRows = null
                    randomMode = true
                },
            )
            else -> {
                BackHandler { levelIndex = null }
                val level = pack[index]
                val hasNext = index < pack.lastIndex
                GameScreen(
                    level = level,
                    title = "${size.label} · Level ${level.number}",
                    solvedMessage = if (hasNext) "Level complete!" else "Pack complete!",
                    nextLabel = if (hasNext) "Next" else null,
                    nextAlwaysVisible = false,
                    onSolved = {
                        val done = completed.getValue(size)
                        if (level.number !in done) {
                            completed = completed + (size to done + level.number)
                            progress.setCompletedLevels(size, done + level.number)
                        }
                    },
                    onNext = { levelIndex = index + 1 },
                    onBack = { levelIndex = null },
                )
            }
        }
    }
}
