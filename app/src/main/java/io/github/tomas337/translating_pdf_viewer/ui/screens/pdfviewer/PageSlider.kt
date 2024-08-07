package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PageSlider(
    maxPage: Int,
    curPage: Int,
    setPage: suspend (Int) -> Unit,
    maxWidth: Int,
    maxHeight: Int,
    isVisible: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val size = 40.dp
    var stepSize by remember { mutableIntStateOf(0) }
    var y by remember { mutableIntStateOf(0) }

    LaunchedEffect(maxPage, maxHeight) {
        with (density) {
            stepSize = if (maxPage > 1) (maxHeight - size.roundToPx()) / (maxPage - 1) else 0
        }
    }
    LaunchedEffect(curPage, stepSize) {
        y = curPage * stepSize
    }

    if (isVisible) {
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        val x = maxWidth - placeable.width
                        placeable.place(x, y, zIndex = 1f)
                    }
                }
                .pointerInput(Unit) {
                    var newY = y
                    detectVerticalDragGestures(
                        onDragStart = { newY = y },
                        onDragEnd = { newY = y },
                        onDragCancel = { newY = y },
                        onVerticalDrag = { _, dragAmount ->
                            newY += dragAmount.roundToInt()
                            val isAtTop = (y == 0)
                            val isAtBottom = (y == (maxHeight - size.roundToPx()))

                            if (newY > (y + stepSize / 2) && !isAtBottom) {
                                y += stepSize
                                coroutineScope.launch { setPage(y / stepSize) }
                            } else if (newY < (y - stepSize / 2) && !isAtTop) {
                                y -= stepSize
                                coroutineScope.launch { setPage(y / stepSize) }
                            }
                        }
                    )
                }
                .size(size)
                .background(color = Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("$curPage", color = Color.White)
        }
    }
}