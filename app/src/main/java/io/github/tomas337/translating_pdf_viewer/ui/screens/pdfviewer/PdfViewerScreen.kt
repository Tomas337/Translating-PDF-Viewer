package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.ContentViewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PreferencesViewModel
import io.github.tomas337.translating_pdf_viewer.utils.PageContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfViewerScreen(
    navController: NavController,
    fileId: Int,
    contentViewModel: ContentViewModel = viewModel(factory = ContentViewModel.Factory),
    preferencesViewModel: PreferencesViewModel = viewModel(factory = PreferencesViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        contentViewModel.initFileInfo(fileId)
    }
    val fileInfo: FileModel by contentViewModel.fileInfo.observeAsState(FileModel())

    var isToolbarVisible by remember { mutableStateOf(true) }
    val isInitialized = fileInfo.pageCount != 0
    val context = LocalContext.current

    PdfViewerScaffold(
        isToolbarVisible = isToolbarVisible,
        isInitialized = isInitialized,
        navController = navController
    ) { boxWithConstraintsScope, hideBottomSheetModifier ->

        val pagerState = rememberPagerState(
            initialPage = fileInfo.curPage,
            pageCount = { fileInfo.pageCount }
        )
        var isScrollable by remember { mutableStateOf(false) }
        val pageSpacing by preferencesViewModel.pageSpacing.collectAsState()

        LaunchedEffect(pagerState.currentPage) {
            contentViewModel.updateCurrentPage(pagerState.currentPage, fileId)
        }

        val maxWidth = boxWithConstraintsScope.constraints.maxWidth

        BackHandler(enabled = !isToolbarVisible) {
            isToolbarVisible = true
        }
        VerticalPager(
            modifier = hideBottomSheetModifier
                .fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 1,
            pageSpacing = pageSpacing,
            userScrollEnabled = isScrollable
        ) { pageIndex ->
            val pageContent: List<List<PageContent>> by contentViewModel
                .getPageContent(pageIndex, fileId)
                .observeAsState(emptyList())

            val lazyColumnState = rememberLazyListState()

            val pagePadding by preferencesViewModel.pagePadding.collectAsState()
            val fontSizeScale by preferencesViewModel.fontSizeScale.collectAsState()
            val lineSpacing by preferencesViewModel.lineSpacing.collectAsState()
            val paragraphSpacing by preferencesViewModel.paragraphSpacing.collectAsState()

            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
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
                item {
                    Spacer(modifier = Modifier.height(pagePadding))
                }

                itemsIndexed(pageContent) { i, row ->
                    var y = 0
                    var x = LocalDensity.current.run {
                        val startX = maxWidth * row[0].x
                        Math.round(startX + pagePadding.roundToPx())
                    }

                    val widthModifier =
                        if (row[0].x > 0.25) {
                            val maxWidthDp = LocalDensity.current.run { maxWidth.toDp() }
                            Modifier.widthIn(max = maxWidthDp - 2 * pagePadding)
                        } else {
                            val width = LocalDensity.current.run { (maxWidth - 2 * x).toDp() }
                            Modifier.width(width)
                        }

                    val handleXPositionModifier = widthModifier
                        .onGloballyPositioned {
                            val rootPosition = it.positionInRoot()
                            y = Math.round(rootPosition.y)
                        }
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height) {
                                if (row[0].isCentered) {
                                    x = (maxWidth / 2) - (placeable.width / 2)
                                } else {
                                    val right = x + placeable.width
                                    val parentRight = maxWidth - pagePadding.roundToPx()

                                    val xDifference = right - parentRight

                                    if (xDifference > 0) {
                                        x -= xDifference
                                        if (x < 0) {
                                            x = pagePadding.roundToPx()
                                        }
                                    }
                                }

                                placeable.place(x, y)
                            }
                        }

                    if (i != 0) {
                        Spacer(modifier = Modifier.size(paragraphSpacing))
                    }
                    Row(
                        // TODO fix: when centered and row contains text, there won't be any spacing
                        // (fixed by grouping all images into one one screenshot,
                        // then there won't be the need for SpaceEvenly and only spacedBy can be used)
                        horizontalArrangement = when (row[0].isCentered) {
                            true -> Arrangement.SpaceEvenly
                            else -> Arrangement.spacedBy(10.dp)
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = handleXPositionModifier,
                    ) {
                        row.forEach { content ->
                            DrawContent(
                                content = content,
                                pageIndex = pageIndex,
                                intToTextStyleMap = fileInfo.intToTextStyleMap,
                                fontSizeScale = fontSizeScale,
                                lineSpacing = lineSpacing,
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(pagePadding))
                }
            }
        }
        PageSlider(
            pageCount = fileInfo.pageCount,
            curPage = pagerState.currentPage,
            setPage = { page -> pagerState.animateScrollToPage(page) },
            maxWidth = maxWidth,
            maxHeight = boxWithConstraintsScope.constraints.maxHeight,
            isVisible = isToolbarVisible
        )
    }
}