package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BookmarksBottomBar(
    selected: SnapshotStateList<Int>,
    height: Dp,
    removeBookmark: (Int) -> Unit,
    showDialog: () -> Unit,
    setSelectionMode: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable {
                    for (pageIndex in selected) {
                        removeBookmark(pageIndex)
                    }
                    setSelectionMode(false)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete bookmark",
                tint = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = "Delete",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        if (selected.size == 1) {
            VerticalDivider()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        showDialog()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Rename bookmark",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = "Rename",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}