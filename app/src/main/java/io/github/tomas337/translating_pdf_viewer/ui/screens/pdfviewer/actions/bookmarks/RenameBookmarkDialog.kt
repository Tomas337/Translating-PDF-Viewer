package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun RenameBookmarkDialog(
    initialText: String,
    pageIndex: Int,
    setShowDialog: (Boolean) -> Unit,
    renameBookmark: (Int, String) -> Unit
) {
    val spacerHeight = 10.dp

    var txtField by remember { mutableStateOf(initialText) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .semantics { contentDescription = "Rename bookmark dialog" }
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = "Rename bookmark")
                Spacer(modifier = Modifier.height(spacerHeight))
                TextField(
                    shape = RectangleShape,
                    value = txtField,
                    onValueChange = { txtField = it },
                    modifier = Modifier.semantics { contentDescription = "Item name text field" }
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
                            if (txtField.isNotEmpty() && txtField != initialText) {
                                renameBookmark(pageIndex, txtField)
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