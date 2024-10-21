package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ResetButton(
    resetToDefaults: () -> Unit
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        Dialog(onDismissRequest = { showConfirmationDialog = false }) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(text = "Reset to default settings?")
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showConfirmationDialog = false }
                        ) {
                            Text(text = "CANCEL")
                        }
                        TextButton(
                            onClick = {
                                resetToDefaults()
                                showConfirmationDialog = false
                            }
                        ) {
                            Text(text = "RESET")
                        }
                    }
                }
            }
        }
    }
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
                showConfirmationDialog = true
            }
    ) {
        Text(
            text = "Reset to default settings",
            color = Color.White
        )
    }
}