package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.detectTap

@Composable
fun SettingsSheet(
    initialHeight: Float,
    maxHeight: Int,
    setSettingsSheetVisibility: (Boolean) -> Unit,
    fontSizeScale: Float,
    lineSpacing: Int,
    pagePadding: Dp,
    pageSpacing: Dp,
    paragraphSpacing: Dp,
    updateFontSizeScale: (Float) -> Unit,
    updateLineSpacing: (Float) -> Unit,
    updatePagePadding: (Float) -> Unit,
    updatePageSpacing: (Float) -> Unit,
    updateParagraphSpacing: (Float) -> Unit,
    resetToDefaults: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fontSize = 16.sp
    val dragBarHeight = 28.dp

    val minSheetHeight = with (LocalDensity.current) { dragBarHeight.toPx() / maxHeight }
    var sheetHeight by remember { mutableFloatStateOf(initialHeight) }

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxHeight(sheetHeight)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    val y = maxHeight - placeable.height
                    placeable.place(0, y, zIndex = 2f)
                }
            }
            .pointerInput(isKeyboardVisible) {
                if (isKeyboardVisible) {
                    detectTap { focusManager.clearFocus() }
                }
            }
            .background(MaterialTheme.colorScheme.surface)
    ) {

        // Handle bar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(dragBarHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .pointerInput(Unit) {
                    var curY = sheetHeight * maxHeight

                    detectVerticalDragGestures(
                        onVerticalDrag = { _, dragAmount ->
                            curY -= dragAmount
                            val newSheetHeight = curY / maxHeight

                            if (newSheetHeight < minSheetHeight) {
                                curY = minSheetHeight * maxHeight
                                sheetHeight = minSheetHeight
                            } else {
                                sheetHeight = newSheetHeight
                            }
                        },
                        onDragEnd = {
                            val tolerance = 0.05
                            if (sheetHeight < minSheetHeight + tolerance) {
                                setSettingsSheetVisibility(false)
                            }
                        }
                    )
                }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_drag_handle_48),
                contentDescription = "Drag handle",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            SettingsItem(
                settingName = "font size scale",
                fontSize = fontSize,
                curValue = fontSizeScale,
                updatePreference = updateFontSizeScale,
                precision = 1,
                step = 0.1f
            )
            SettingsItem(
                settingName = "line spacing",
                fontSize = fontSize,
                curValue = lineSpacing.toFloat(),
                updatePreference = updateLineSpacing,
                units = "sp"
            )
            SettingsItem(
                settingName = "page padding",
                fontSize = fontSize,
                curValue = pagePadding.value,
                updatePreference = updatePagePadding,
                units = "dp"
            )
            SettingsItem(
                settingName = "page spacing",
                fontSize = fontSize,
                curValue = pageSpacing.value,
                updatePreference = updatePageSpacing,
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = updateParagraphSpacing,
                units = "dp"
            )
            ResetButton(
                resetToDefaults = resetToDefaults
            )
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}