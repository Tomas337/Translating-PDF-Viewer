package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSheet() {
    val fontSize = 20.sp

    Column(
        modifier = Modifier.fillMaxHeight(0.89f),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SettingsRow(
            settingName = "font size scale",
            fontSize = fontSize,
        )
        SettingsRow(
            settingName = "page padding",
            fontSize = fontSize,
            units = "dp"
        )
    }
}