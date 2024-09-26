package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PreferencesViewModel

@Composable
fun SettingsSheet(
    preferencesViewModel: PreferencesViewModel = viewModel(factory = PreferencesViewModel.Factory)
) {
    val fontSize = 20.sp

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

    if (isInitialized) {
        Column(
            modifier = Modifier.fillMaxHeight(0.89f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingsRow(
                settingName = "font size scale",
                fontSize = fontSize,
                values = (1..100).map { "%.1f".format(it / 10f) },
                startIndex = (fontSizeScale * 10).toInt() - 1,
                updatePreference = { preferencesViewModel.updateFontSizeScale(it.toFloat()) },
            )
            SettingsRow(
                settingName = "line spacing",
                fontSize = fontSize,
                values = (1..99).map { it.toString() },
                startIndex = lineSpacing - 1,
                updatePreference = { preferencesViewModel.updateLineSpacing(it.toInt()) },
            )
            SettingsRow(
                settingName = "page padding",
                fontSize = fontSize,
                values = (1..99).map { it.toString() },
                startIndex = pagePadding.value.toInt() - 1,
                updatePreference = { preferencesViewModel.updatePagePadding(it.toInt()) },
                units = "dp"
            )
            SettingsRow(
                settingName = "page spacing",
                fontSize = fontSize,
                values = (1..99).map { it.toString() },
                startIndex = pageSpacing.value.toInt() - 1,
                updatePreference = { preferencesViewModel.updatePageSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsRow(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                values = (1..99).map { it.toString() },
                startIndex = paragraphSpacing.value.toInt() - 1,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
        }
    }
}