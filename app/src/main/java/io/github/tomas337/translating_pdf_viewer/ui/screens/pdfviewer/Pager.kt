package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.SearchViewModel
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle
import kotlinx.coroutines.flow.Flow

val FontSizeScaleKey = SemanticsPropertyKey<Float>("FontSizeScale")
var SemanticsPropertyReceiver.fontSizeScale by FontSizeScaleKey

val LineSpacingKey = SemanticsPropertyKey<Int>("LineSpacing")
var SemanticsPropertyReceiver.lineSpacing by LineSpacingKey

val PagePaddingKey = SemanticsPropertyKey<Float>("PagePadding")
var SemanticsPropertyReceiver.pagePadding by PagePaddingKey

val PageSpacingKey = SemanticsPropertyKey<Float>("PageSpacing")
var SemanticsPropertyReceiver.pageSpacing by PageSpacingKey

val ParagraphSpacingKey = SemanticsPropertyKey<Float>("ParagraphSpacing")
var SemanticsPropertyReceiver.paragraphSpacing by ParagraphSpacingKey

@Composable
fun Pager(
    pagerState: PagerState,
    getPageContent: (Int) -> Flow<List<List<PageContent>>>,
    intToTextStyleMap: Map<Int, TextStyle>,
    switchToolbarVisibility: () -> Boolean,
    maxWidth: Int,
    fontSizeScale: Float,
    lineSpacing: Int,
    pagePadding: Dp,
    pageSpacing: Dp,
    paragraphSpacing: Dp,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
) {
    val context = LocalContext.current
    var isScrollable by remember { mutableStateOf(false) }

    val currentlySelected by searchViewModel.currentlySelected.collectAsState()
    val highlightsSize by searchViewModel.highlightsSize.collectAsState()

    LaunchedEffect(currentlySelected) {
        if (currentlySelected != -1) {
            pagerState.scrollToPage(searchViewModel.highlights[currentlySelected].first)
        }
    }

    VerticalPager(
        modifier = modifier
            .fillMaxSize(),
        state = pagerState,
        beyondViewportPageCount = 1,
        pageSpacing = pageSpacing,
        userScrollEnabled = isScrollable
    ) { pageIndex ->
        val pageContent: List<List<PageContent>> by getPageContent(pageIndex).collectAsState(emptyList())
        val lazyColumnState = rememberLazyListState()

        val pageHighlights by remember(highlightsSize) {
            derivedStateOf { searchViewModel.highlightsStructured.getOrDefault(pageIndex, emptyMap()) }
        }

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
                            val isToolbarVisible = switchToolbarVisibility()
                            if (!isToolbarVisible) {
                                val toastMessage =
                                    "Long press the screen or press back to exit fullscreen."
                                Toast
                                    .makeText(context, toastMessage, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    )
                }
                .semantics {
                    contentDescription = "Page $pageIndex"
                    this.fontSizeScale = fontSizeScale
                    this.lineSpacing = lineSpacing
                    this.pagePadding = pagePadding.value
                    this.pageSpacing = pageSpacing.value
                    this.paragraphSpacing = paragraphSpacing.value
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
                    row.forEachIndexed { j, content ->
                        DrawContent(
                            content = content,
                            pageIndex = pageIndex,
                            intToTextStyleMap = intToTextStyleMap,
                            fontSizeScale = fontSizeScale,
                            lineSpacing = lineSpacing,
                            highlights = pageHighlights.getOrDefault(Pair(i, j), emptyList()),
                            isHighlightSelected = {
                                if (currentlySelected != -1) {
                                    Log.d("comparedValue", "")
                                    Log.d("comparedValue", it.toString())
                                    Log.d("currentlySelected", searchViewModel.highlights[currentlySelected].toString())
                                    Log.d("result", (it === searchViewModel.highlights[currentlySelected].second).toString())

                                    it === searchViewModel.highlights[currentlySelected].second
                                }
                                false
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(pagePadding))
            }
        }
    }
}