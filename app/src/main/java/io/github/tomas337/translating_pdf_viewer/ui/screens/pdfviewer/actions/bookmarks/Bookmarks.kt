package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel

@Composable
fun Bookmarks(
    bookmarks: List<BookmarkModel>,
    setBookmarksVisibility: (Boolean) -> Unit,
    setPage: suspend (Int) -> Unit,
    curPage: Int,
    addBookmark: (Int) -> Unit,
    removeBookmark: (Int) -> Unit,
    renameBookmark: (Int, String) -> Unit,
) {
    val rowHeight = 50.dp
    val isCurrentPageBookmarked = bookmarks.any { it.pageIndex == curPage }

    var inSelectionMode by remember { mutableStateOf(false) }
    val selected = remember { mutableStateListOf<Int>() }
    val setSelectionMode: (Boolean) -> Unit = {
        if (!it) {
            selected.removeAll(selected)
        }
        inSelectionMode = it
    }

    var showDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = inSelectionMode) {
        setSelectionMode(false)
    }
    Column {
        BookmarkPageButton(
            curPage = curPage,
            isCurrentPageBookmarked = isCurrentPageBookmarked,
            addBookmark = addBookmark,
            removeBookmark = removeBookmark,
            rowHeight = rowHeight
        )
        LazyColumn(
            modifier = Modifier
                .weight(5f)
                .semantics { contentDescription = "Bookmark list" }
        ) {
            itemsIndexed(bookmarks) { index, bookmark ->
                var checked by remember { mutableStateOf(false) }

                LaunchedEffect(selected.size) {
                    checked = bookmark.pageIndex in selected
                }
                if (showDialog &&
                    checked &&
                    selected.size == 1
                ) {
                    RenameBookmarkDialog(
                        initialText = bookmark.text,
                        pageIndex = bookmark.pageIndex,
                        setShowDialog = { showDialog = it },
                        renameBookmark = renameBookmark
                    )
                }

                BookmarkItem(
                    index = index,
                    bookmark = bookmark,
                    checked = checked,
                    inSelectionMode = inSelectionMode,
                    selectBookmark = { selected.add(it) },
                    unselectBookmark = { selected.remove(it) },
                    setSelectionMode = setSelectionMode,
                    setBookmarksVisibility = setBookmarksVisibility,
                    setPage = setPage,
                    rowHeight = rowHeight
                )

            }
        }
        if (inSelectionMode && selected.size != 0) {
            BookmarksBottomBar(
                selected = selected,
                height = 60.dp,
                removeBookmark = removeBookmark,
                showDialog = { showDialog = true },
                setSelectionMode = setSelectionMode,
            )
        }
    }
}