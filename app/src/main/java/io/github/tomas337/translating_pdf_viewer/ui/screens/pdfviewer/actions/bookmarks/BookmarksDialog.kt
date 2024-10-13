package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.BookmarkViewModel

@Composable
fun BookmarksDialog(
    bookmarks: List<BookmarkModel>,
    setBookmarksVisibility: (Boolean) -> Unit,
    setPage: suspend (Int) -> Unit,
    curPage: Int,
    addBookmark: (Int) -> Unit,
    removeBookmark: (Int) -> Unit,
    hasContents: Boolean = true,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = if (hasContents) listOf("Contents", "Bookmarks") else listOf("Bookmarks")

    Dialog(onDismissRequest = { setBookmarksVisibility(false) }) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.height(400.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    divider = {
                        HorizontalDivider(color = MaterialTheme.colorScheme.inversePrimary)
                    }
                ) {
                    tabs.forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(tabTitle)
                            }
                        )
                    }
                }
                if (tabs[selectedTabIndex] == "Contents") {
                    Contents()
                } else if (tabs[selectedTabIndex] == "Bookmarks") {
                    Bookmarks(
                        bookmarks = bookmarks,
                        setBookmarksVisibility = setBookmarksVisibility,
                        setPage = setPage,
                        curPage = curPage,
                        addBookmark = addBookmark,
                        removeBookmark = removeBookmark,
                    )
                }
            }
        }
    }
}
