package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    val isProcessed by remember(fileInfo.intToTextStyleMap) {
        derivedStateOf { fileInfo.intToTextStyleMap.isNotEmpty() }
    }

    val textSize = 20.sp
    val padding = 10.dp

    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }
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
            .clickable(enabled = isProcessed) {
                navController.navigate(NavRoute.PdfViewer.createRoute(fileInfo.id))
            }
            .semantics { contentDescription = "File: ${fileInfo.name}" }
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
                model = fileInfo.thumbnailPath,
                contentDescription = "Thumbnail",
                modifier = Modifier.size(boxWithConstraintsScope.maxWidth * 0.2f),
                colorFilter = if (!isProcessed) {
                    ColorFilter.tint(MaterialTheme.colorScheme.surfaceVariant, BlendMode.Saturation)
                } else null
            )
            Text(
                text = fileInfo.name,
                modifier = Modifier.width(boxWithConstraintsScope.maxWidth * 0.5f),
                fontSize = textSize,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                color = if (isProcessed) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                maxLines = 2
            )

            val widthModifier = Modifier.width(48.dp)
            if (isProcessed) {
                IconButton(
                    onClick = {
                        isContextMenuVisible = true
                    },
                    modifier = widthModifier
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Edit file info"
                    )
                    ContextMenu(
                        fileInfo.id,
                        padding,
                        isContextMenuVisible,
                        { isContextMenuVisible = it },
                        { showDialog = it },
                        boxWithConstraintsScope
                    )
                }
            } else {
                val progress by homeViewModel.addFileProgress.collectAsState()
                CircularProgressIndicator(
                    progress = { progress },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = widthModifier
                        .semantics {
                            contentDescription = "File extraction progress bar"
                        }
                        .progressSemantics(progress)
                )
            }
        }
    }
}