package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun SettingsItem(
    settingName: String,
    fontSize: TextUnit,
    curValue: Float,
    updatePreference: (Float) -> Unit,
    step: Float = 1f,
    precision: Int = 0,
    units: String? = null,
) {
    var isError by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("%.${precision}f".format(curValue)) }
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(curValue) {
        textFieldValue = "%.${precision}f".format(curValue)
    }
    LaunchedEffect(isKeyboardVisible) {
        textFieldValue = "%.${precision}f".format(curValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .let {
                    if (!isKeyboardVisible) {
                        it.clickable(onClick = { updatePreference(curValue - step) })
                    } else {
                        it
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 30.sp
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .zIndex(100f)
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
                    focusManager.clearFocus()
                } catch (e: NumberFormatException) {
                    isError = true
                }
            })
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .let {
                    if (!isKeyboardVisible) {
                        it.clickable(onClick = { updatePreference(curValue + step) })
                    } else {
                        it
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 30.sp
            )
        }
    }
}