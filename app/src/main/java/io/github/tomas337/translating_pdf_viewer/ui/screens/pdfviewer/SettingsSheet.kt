package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsSheet(
    initialHeight: Float,
    maxHeight: Int,
    setSettingsSheetVisibility: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel = viewModel(factory = PreferencesViewModel.Factory)
) {
    val fontSize = 16.sp
    val dragBarHeight = 28.dp

    val fontSizeScale by preferencesViewModel.fontSizeScale.collectAsState()
    val lineSpacing by preferencesViewModel.lineSpacing.collectAsState()
    val pagePadding by preferencesViewModel.pagePadding.collectAsState()
    val pageSpacing by preferencesViewModel.pageSpacing.collectAsState()
    val paragraphSpacing by preferencesViewModel.paragraphSpacing.collectAsState()

    val isInitialized = (fontSizeScale != -1f &&
            lineSpacing != -1 &&
            pagePadding.value != -1f &&
            pageSpacing.value != -1f &&
            paragraphSpacing.value != -1f)

    val minSheetHeight = with (LocalDensity.current) { dragBarHeight.toPx() / maxHeight }
    var sheetHeight by remember { mutableFloatStateOf(initialHeight) }

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val focusManager = LocalFocusManager.current

    if (isInitialized) {
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
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Handle bar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dragBarHeight)
                    .background(MaterialTheme.colorScheme.secondary)
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
                    tint = MaterialTheme.colorScheme.onSecondary
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
                    updatePreference = { preferencesViewModel.updateFontSizeScale(it) },
                    precision = 1,
                    step = 0.1f
                )
                SettingsItem(
                    settingName = "line spacing",
                    fontSize = fontSize,
                    curValue = lineSpacing.toFloat(),
                    updatePreference = { preferencesViewModel.updateLineSpacing(it.toInt()) },
                    units = "sp"
                )
                SettingsItem(
                    settingName = "page padding",
                    fontSize = fontSize,
                    curValue = pagePadding.value,
                    updatePreference = { preferencesViewModel.updatePagePadding(it.toInt()) },
                    units = "dp"
                )
                SettingsItem(
                    settingName = "page spacing",
                    fontSize = fontSize,
                    curValue = pageSpacing.value,
                    updatePreference = { preferencesViewModel.updatePageSpacing(it.toInt()) },
                    units = "dp"
                )
                SettingsItem(
                    settingName = "paragraph spacing",
                    fontSize = fontSize,
                    curValue = paragraphSpacing.value,
                    updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                    units = "dp"
                )
            }
        }
    }
}