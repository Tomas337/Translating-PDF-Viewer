package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberPickerState(initialValue: String = "") = remember { PickerState(initialValue) }

class PickerState(initialValue: String) {
    var selectedItem by mutableStateOf(initialValue)
}