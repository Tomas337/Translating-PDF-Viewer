package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItem(
    settingName: String,
    fontSize: TextUnit,
    curValue: Float,
    updatePreference: (Float) -> Unit,
    step: Float = 1f,
    units: String? = null,
) {
    var isError by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(curValue.toString()) }

    LaunchedEffect(curValue) {
        textFieldValue = curValue.toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { updatePreference(curValue - step) }
        ) {
            Text("-")
        }
        OutlinedTextField(
            modifier = Modifier
                .width(200.dp),
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = fontSize
            ),
            singleLine = true,
            label = ({
                Text(
                    text = settingName,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold
                )
            }),
            suffix = ({
                if (units != null) {
                    Text(
                        text = units,
                        fontSize = fontSize
                    )
                }
            }),
            isError = isError,
            supportingText = ({
                if (isError) {
                    Text("Invalid numeric format")
                }
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                try {
                    updatePreference(textFieldValue.toFloat())
                    isError = false
                } catch (e: NumberFormatException) {
                    isError = true
                }
            })
        )
        Button(
            onClick = { updatePreference(curValue + step) }
        ) {
            Text("+")
        }
    }
}