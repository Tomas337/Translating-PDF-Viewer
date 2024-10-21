package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResetButton(
    resetToDefaults: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 30.dp)
            .background(
                // TODO set color to MaterialTheme.colorScheme after creating better color theme
                color = Color(0xFFD10000),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                resetToDefaults()
            }
    ) {
        Text(
            text = "Reset to default settings",
            color = Color.White
        )
    }
}