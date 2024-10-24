package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel.HomeViewModel

@Composable
fun UpdateNameDialog(
    id: Int,
    oldText: String,
    setShowDialog: (Boolean) -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val spacerHeight = 10.dp

    var txtField by remember { mutableStateOf(oldText) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = "Update file name")
                Spacer(modifier = Modifier.height(spacerHeight))
                TextField(
                    shape = RectangleShape,
                    value = txtField,
                    onValueChange = { txtField = it },
                )
                Spacer(modifier = Modifier.height(spacerHeight))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { setShowDialog(false) }
                    ) {
                        Text(text = "CANCEL")
                    }
                    TextButton(
                        onClick = {
                            if (txtField.isNotEmpty() && txtField != oldText) {
                                homeViewModel.updateName(txtField, id)
                            }
                            setShowDialog(false)
                        }
                    ) {
                        Text(text = "SAVE")
                    }
                }
            }
        }
    }
}