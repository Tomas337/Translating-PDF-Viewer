package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.internal.LinkedTreeMap
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PdfViewerViewModel
import io.github.tomas337.translating_pdf_viewer.utils.Image
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock
import kotlin.math.max

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
            val pageContent: List<PageContent> by pdfViewerViewModel
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
                                    val toastMessage =
                                        "Long press the screen or press back to exit fullscreen."
                                    Toast
                                        .makeText(context, toastMessage, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        )
                    },
            ) {
                items(pageContent) { content ->
                    DrawContent(
                        content,
                        pageIndex,
                        fileInfo.intToTextStyleMap
                    )
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
