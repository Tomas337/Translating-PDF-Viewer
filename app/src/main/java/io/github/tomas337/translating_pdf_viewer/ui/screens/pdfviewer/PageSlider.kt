package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun PageSlider(
    maxPage: Int,
    curPage: Int,
    setPage: suspend (Int) -> Unit,
    maxWidth: Dp,
) {
    val coroutineScope = rememberCoroutineScope()

    Slider(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = 90f
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxWidth,
                    )
                )
                // x & y and width & height are interchanged due to rotation
                layout(placeable.height, placeable.width) {
                    placeable.place(0, -maxWidth.roundToPx(), zIndex = 1f)
                }
            }
            .fillMaxWidth()
            .height(20.dp)
        ,
        valueRange = 0f..(maxPage - 1).toFloat(),
        value = curPage.toFloat(),
        onValueChange = {
            coroutineScope.launch {
                setPage(it.toInt())
            }
        },
        steps = max(maxPage - 2, 0),
    )
}