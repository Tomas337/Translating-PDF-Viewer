package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel.HomeViewModel

@Composable
fun ContextMenu(
    id: Int,
    padding: Dp,
    isContextMenuVisible: Boolean,
    setContextMenuVisibility: (Boolean) -> Unit,
    setShowDialog: (Boolean) -> Unit,
    boxWithConstraintsScope: BoxWithConstraintsScope,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val context = LocalContext.current

    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = {
            setContextMenuVisibility(false)
        },
        offset = pressOffset.copy(
            y = pressOffset.y - boxWithConstraintsScope.maxWidth * 0.1f
        )
    ) {
        TextButton(
            onClick = {
                setShowDialog(true)
                setContextMenuVisibility(false)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit icon",
                )
                Spacer(modifier = Modifier.width(padding))
                Text(text = "Edit name")
            }
        }
        TextButton(
            onClick = {
                homeViewModel.deleteFile(context, id)
                setContextMenuVisibility(false)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Thrash icon",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(padding))
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}