package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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

    var isToolbarVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current

    PdfViewerContainer(
        isVisible = isToolbarVisible,
        navController = navController
    ) { boxWithConstraintsScope ->

        val pagerState = rememberPagerState(
            initialPage = fileInfo.curPage,
            pageCount = { fileInfo.pageCount }
        )
        var isScrollable by remember { mutableStateOf(false) }


        BackHandler(enabled = !isToolbarVisible) {
            isToolbarVisible = true
        }
        VerticalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            userScrollEnabled = isScrollable
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
                        detectAndHandleScroll(
                            lazyColumnState = lazyColumnState,
                            setScrollable = { isScrollable = it }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                isToolbarVisible = !isToolbarVisible
                                if (!isToolbarVisible) {
                                    val toastMessage = "Long press the screen or press back to exit fullscreen."
                                    Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        )
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
            pageCount = fileInfo.pageCount,
            curPage = pagerState.currentPage,
            setPage = { page -> pagerState.animateScrollToPage(page) },
            maxWidth = boxWithConstraintsScope.constraints.maxWidth,
            maxHeight = boxWithConstraintsScope.constraints.maxHeight,
            isVisible = isToolbarVisible
        )
    }
}
