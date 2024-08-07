package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PageSlider(
    pageCount: Int,
    curPage: Int,
    setPage: suspend (Int) -> Unit,
    maxWidth: Int,
    maxHeight: Int,
    isVisible: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val width = 40.dp
    val height = 40.dp
    var stepSize by remember { mutableIntStateOf(0) }
    var y by remember { mutableIntStateOf(0) }

    LaunchedEffect(pageCount, maxHeight) {
        with (density) {
            stepSize = if (pageCount > 1) (maxHeight - height.roundToPx()) / (pageCount - 1) else 0
        }
    }
    LaunchedEffect(curPage, stepSize) {
        y = curPage * stepSize
    }

    var isActive by remember { mutableStateOf(false) }

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
                        onDragEnd = {
                            newY = y
                            isActive = false
                        },
                        onDragCancel = {
                            newY = y
                            isActive = false
                        },
                        onVerticalDrag = { _, dragAmount ->
                            newY += dragAmount.roundToInt()
                            val isAtTop = (y == 0)
                            val isAtBottom = (y == (maxHeight - height.roundToPx()))

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
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isActive = true
                            if (tryAwaitRelease()) {
                                isActive = false
                            }
                        }
                    )
                }
                .height(height)
                .clip(
                    RoundedCornerShape(
                        topStart = (height.value / 2).dp,
                        bottomStart = (height.value / 2).dp
                    )
                )
                .background(color = Color.Black),
        contentAlignment = Alignment.Center,
        ) {
            if (isActive) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val paddedCurPage = (curPage + 1)
                        .toString()
                        .padStart(pageCount.toString().length, ' ')

                    Text(
                        text ="$paddedCurPage / $pageCount",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.width(width))
                }
            } else {
                Text(
                    text = "${curPage + 1}",
                    modifier = Modifier.width(width),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}