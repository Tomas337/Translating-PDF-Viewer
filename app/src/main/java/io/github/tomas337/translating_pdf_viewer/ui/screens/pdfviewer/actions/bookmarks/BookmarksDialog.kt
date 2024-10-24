package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun BookmarksDialog(
    bookmarks: List<BookmarkModel>,
    setBookmarksVisibility: (Boolean) -> Unit,
    setPage: suspend (Int) -> Unit,
    curPage: Int,
    addBookmark: (Int) -> Unit,
    removeBookmark: (Int) -> Unit,
    renameBookmark: (Int, String) -> Unit,
    hasContents: Boolean = false,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = if (hasContents) listOf("Contents", "Bookmarks") else listOf("Bookmarks")

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }
    val indicatorPadding = 12.dp
    val density = LocalDensity.current

    Dialog(onDismissRequest = { setBookmarksVisibility(false) }) {
        Surface(
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.height(600.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    divider = {},
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier
                                    .customWidthTabIndicatorOffset(
                                        currentTabPosition = tabPositions[selectedTabIndex],
                                        tabWidth = tabWidths[selectedTabIndex]
                                    )
                                    .padding(
                                        vertical = 10.dp
                                    )
                            )
                        }
                    },
                ) {
                    tabs.forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = tabTitle,
                                    onTextLayout = { textLayoutResult ->
                                        tabWidths[index] = with(density) {
                                            textLayoutResult.size.width.toDp() + indicatorPadding
                                        }
                                    }
                                )
                            },
                            interactionSource = object : MutableInteractionSource {
                                override val interactions: Flow<Interaction> = emptyFlow()

                                override suspend fun emit(interaction: Interaction) {}

                                override fun tryEmit(interaction: Interaction) = true
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

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
                        renameBookmark = renameBookmark
                    )
                }
            }
        }
    }
}
