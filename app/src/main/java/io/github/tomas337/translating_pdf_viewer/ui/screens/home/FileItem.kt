package io.github.tomas337.translating_pdf_viewer.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.domain.model.FileInfoModel
import io.github.tomas337.translating_pdf_viewer.ui.main.navigation.NavRoute

@Composable
fun FileItem(
    fileInfo: FileInfoModel,
    navController: NavController
) {
    val fileItemHeight = 70.dp
    val textSize = 20.sp
    val padding = 10.dp

    Box(
        modifier = Modifier
            .clickable {
                navController.navigate(NavRoute.PdfViewer.createRoute(fileInfo.id))
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Image(
                fileInfo.thumbnail.asImageBitmap(),
                "Thumbnail"
            )
            Text(
                text = fileInfo.name,
                fontSize = textSize,
                textAlign = TextAlign.Center,
                )
            Text(
                text = fileInfo.language,
                fontSize = textSize,
                textAlign = TextAlign.Right,
            )
            IconButton(
                onClick = {
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Edit file info"
                )
            }
        }
    }
}