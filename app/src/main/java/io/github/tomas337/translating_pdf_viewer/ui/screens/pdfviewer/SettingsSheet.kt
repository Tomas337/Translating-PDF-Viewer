package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PreferencesViewModel

@Composable
fun SettingsSheet(
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel = viewModel(factory = PreferencesViewModel.Factory)
) {
    val fontSize = 16.sp

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
            modifier = modifier
                .fillMaxHeight(0.3f)
                .background(MaterialTheme.colorScheme.background)
                .zIndex(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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

            // Test starts here
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                units = "dp"
            )
            SettingsItem(
                settingName = "paragraph spacing",
                fontSize = fontSize,
                curValue = paragraphSpacing.value,
                updatePreference = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
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