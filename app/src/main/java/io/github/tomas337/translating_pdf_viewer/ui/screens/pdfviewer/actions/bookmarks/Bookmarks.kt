package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Bookmarks(
    bookmarks: List<BookmarkModel>,
    setBookmarksVisibility: (Boolean) -> Unit,
    setPage: suspend (Int) -> Unit,
    curPage: Int,
    addBookmark: (Int) -> Unit,
    removeBookmark: (Int) -> Unit,
) {
    val rowHeight = 50.dp
    val isCurrentPageBookmarked = bookmarks.any { it.pageIndex == curPage }
    val coroutineScope = rememberCoroutineScope()

    var inSelectionMode by remember { mutableStateOf(false) }
    val selected = mutableSetOf<Int>()

    BackHandler(enabled = inSelectionMode) {
        inSelectionMode = false
    }
    Column {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.inversePrimary)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        if (isCurrentPageBookmarked) {
                            removeBookmark(curPage)
                        } else {
                            addBookmark(curPage)
                        }
                    }
                    .padding(horizontal = 25.dp)
            ) {
                if (isCurrentPageBookmarked) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.outline_bookmark_remove_24),
                        contentDescription = "Remove bookmark",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Remove current page from bookmarks",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.outline_bookmark_add_24),
                        contentDescription = "Add bookmark",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Add current page to bookmarks",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.inversePrimary)
        }
        LazyColumn {
            itemsIndexed(bookmarks) { index, bookmark ->
                var checked by remember { mutableStateOf(index in selected) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                if (inSelectionMode) {
                                    checked = !checked
                                } else {
                                    coroutineScope.launch {
                                        setPage(bookmark.pageIndex)
                                    }
                                    setBookmarksVisibility(false)
                                }
                            },
                            onLongClick = {
                                if (inSelectionMode) {
                                    checked = !checked
                                } else {
                                    inSelectionMode = true
                                }
                            }
                        )
                        .padding(horizontal = 20.dp)
                ) {
                    if (inSelectionMode) {
                        Box {
                            Icon(
                                painter = painterResource(
                                    id = if (checked) {
                                        R.drawable.round_check_circle_outline_24
                                    } else {
                                        R.drawable.round_radio_button_unchecked_24
                                    }
                                ),
                                contentDescription = "Check circle"
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
        }
    }
}