package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.main.navigation.NavRoute
import io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel.HomeViewModel

@Composable
fun FileItem(
    fileInfo: FileModel,
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val textSize = 20.sp
    val padding = 10.dp

    val thumbnailPath: String? by homeViewModel.getThumbnailPath(fileInfo.id).observeAsState()

    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }
    val pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        UpdateNameDialog(
            id = fileInfo.id,
            oldText = fileInfo.name,
            setShowDialog = { showDialog = it }
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .clickable {
                navController.navigate(NavRoute.PdfViewer.createRoute(fileInfo.id))
            }
    ) {
        val boxWithConstraintsScope = this
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            AsyncImage(
                model = thumbnailPath,
                contentDescription = "Thumbnail",
                modifier = Modifier.size(boxWithConstraintsScope.maxWidth * 0.2f)
            )
            Text(
                text = fileInfo.name,
                modifier = Modifier.width(boxWithConstraintsScope.maxWidth * 0.5f),
                fontSize = textSize,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            IconButton(
                onClick = {
                    isContextMenuVisible = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Edit file info"
                )
                DropdownMenu(
                    expanded = isContextMenuVisible,
                    onDismissRequest = {
                        isContextMenuVisible = false
                    },
                    offset = pressOffset.copy(
                        y = pressOffset.y - boxWithConstraintsScope.maxWidth * 0.1f
                    )
                ) {
                    TextButton(
                        onClick = {
                            showDialog = true
                            isContextMenuVisible = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RectangleShape
                    ) {
                        Text(text = "Edit name")
                    }
                    TextButton(
                        onClick = {
                            homeViewModel.deleteFile(context, fileInfo.id)
                            isContextMenuVisible = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RectangleShape
                    ) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}