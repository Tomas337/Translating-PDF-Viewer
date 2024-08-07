package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PdfViewerViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfViewerScreen(
    navController: NavController,
    fileId: Int,
    pdfViewerViewModel: PdfViewerViewModel = viewModel(factory = PdfViewerViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        pdfViewerViewModel.initFileInfo(fileId)
    }
    val fileInfo: FileModel by pdfViewerViewModel.fileInfo.observeAsState(FileModel())

    PdfViewerContainer(navController = navController) { boxWithConstraintsScope ->
        val pagerState = rememberPagerState(
            initialPage = fileInfo.curPage,
            pageCount = { fileInfo.maxPage }
        )
        var scrollable by remember { mutableStateOf(false) }

        VerticalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            userScrollEnabled = scrollable
        ) { pageIndex ->
            val pageContent: List<Any> by pdfViewerViewModel
                .getPageContent(pageIndex, fileId)
                .observeAsState(emptyList())

            val lazyColumnState = rememberLazyListState()

            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            var previousEvent = PointerEvent(emptyList())
                            while (true) {
                                val curEvent = awaitPointerEvent(PointerEventPass.Initial)

                                if (previousEvent.type == PointerEventType.Move &&
                                    curEvent.type == PointerEventType.Release
                                ) {
                                    val pointerEvent = previousEvent.changes.first()
                                    val delta =
                                        pointerEvent.position - pointerEvent.previousPosition

                                    val isDraggingUpwards = delta.y < 0f
                                    val isDraggingDownwards = delta.y > 0f
                                    val isAtBottom = !lazyColumnState.canScrollForward
                                    val isAtTop = !lazyColumnState.canScrollBackward

                                    scrollable = (isAtBottom && isDraggingUpwards) ||
                                            (isAtTop && isDraggingDownwards)

                                } else if (curEvent.type == PointerEventType.Move) {
                                    val pointerEvent = curEvent.changes.first()
                                    val delta =
                                        pointerEvent.position - pointerEvent.previousPosition

                                    val isDraggingUpwards = delta.y < 0f
                                    val isDraggingDownwards = delta.y > 0f
                                    val isAtBottom = !lazyColumnState.canScrollForward
                                    val isAtTop = !lazyColumnState.canScrollBackward

                                    if ((isAtBottom && isDraggingDownwards) ||
                                        (isAtTop && isDraggingUpwards)
                                    ) {
                                        scrollable = false
                                    }
                                }
                                previousEvent = curEvent
                            }
                        }
                    },
            ) {
                item {
                    Text(text = "text-$pageIndex")
                    Spacer(modifier = Modifier.height(1000.dp))
                    Text(text = "end of page $pageIndex")
                }
            }
        }
        PageSlider(
            maxPage = fileInfo.maxPage,
            curPage = pagerState.currentPage,
            setPage = { page -> pagerState.animateScrollToPage(page) },
            maxWidth = boxWithConstraintsScope.maxWidth
        )
    }
}
