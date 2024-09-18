package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun SettingsRow(
    settingName: String,
    fontSize: TextUnit,
    units: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = settingName,
            modifier = Modifier.weight(3f),
            fontSize = fontSize
        )
        Row(
            modifier = Modifier.weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val values = remember { (1..99).map { it.toString() } }
            val valuesPickerState = rememberPickerState()
            Picker(
                modifier = Modifier.width(80.dp),
                textStyle = TextStyle.Default.copy(
                    fontSize = fontSize
                ),
                state = valuesPickerState,
                items = values,
                visibleItemsCount = 3
            )
            if (units != null) {
                Text(
                    text = units,
                    fontSize = fontSize
                )
            }
        }
    }
}