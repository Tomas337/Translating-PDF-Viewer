package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkItem(
    index: Int,
    bookmark: BookmarkModel,
    checked: Boolean,
    inSelectionMode: Boolean,
    selectBookmark: (Int) -> Unit,
    unselectBookmark: (Int) -> Unit,
    setSelectionMode: (Boolean) -> Unit,
    setBookmarksVisibility: (Boolean) -> Unit,
    setPage: suspend (Int) -> Unit,
    rowHeight: Dp
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (inSelectionMode) {
                        if (!checked) {
                            selectBookmark(bookmark.pageIndex)
                        } else {
                            unselectBookmark(bookmark.pageIndex)
                        }
                    } else {
                        coroutineScope.launch {
                            setPage(bookmark.pageIndex)
                        }
                        setBookmarksVisibility(false)
                    }
                },
                onLongClick = {
                    if (inSelectionMode) {
                        if (!checked) {
                            selectBookmark(bookmark.pageIndex)
                        } else {
                            unselectBookmark(bookmark.pageIndex)
                        }
                    } else {
                        selectBookmark(bookmark.pageIndex)
                        setSelectionMode(true)
                    }
                }
            )
            .padding(horizontal = 20.dp)
    ) {
        if (inSelectionMode) {
            if (checked) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.round_check_circle_outline_24
                    ),
                    contentDescription = "Checked circle"
                )
            } else {
                Icon(
                    painter = painterResource(
                        id = R.drawable.round_radio_button_unchecked_24
                    ),
                    contentDescription = "Unchecked circle"
                )
            }
        }
        Column {
            if (index != 0) {
                HorizontalDivider()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight)
            ) {
                Text(text = bookmark.text)
                Text(text = "${bookmark.pageIndex + 1}")
            }
        }
    }
}