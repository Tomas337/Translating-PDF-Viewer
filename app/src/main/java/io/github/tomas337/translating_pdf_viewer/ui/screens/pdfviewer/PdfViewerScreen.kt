package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks.BookmarksDialog
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.settings.SettingsSheet
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.BookmarkViewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.ContentViewModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PreferencesViewModel

@Composable
fun PdfViewerScreen(
    navController: NavController,
    fileId: Int,
    contentViewModel: ContentViewModel = viewModel(factory = ContentViewModel.provideFactory(fileId)),
    preferencesViewModel: PreferencesViewModel = viewModel(factory = PreferencesViewModel.Factory),
    bookmarkViewModel: BookmarkViewModel = viewModel(factory = BookmarkViewModel.provideFactory(fileId)),
) {
    val fileInfo: FileModel by contentViewModel.fileInfo.collectAsState()
    val fontSizeScale by preferencesViewModel.fontSizeScale.collectAsState()
    val lineSpacing by preferencesViewModel.lineSpacing.collectAsState()
    val pagePadding by preferencesViewModel.pagePadding.collectAsState()
    val pageSpacing by preferencesViewModel.pageSpacing.collectAsState()
    val paragraphSpacing by preferencesViewModel.paragraphSpacing.collectAsState()

    val isInitialized = fileInfo.pageCount != 0 &&
            (fontSizeScale != -1f &&
             lineSpacing != -1 &&
             pagePadding.value != -1f &&
             pageSpacing.value != -1f &&
             paragraphSpacing.value != -1f)
    var isToolbarVisible by remember { mutableStateOf(true) }

    PdfViewerScaffold(
        isInitialized = isInitialized,
        isToolbarVisible = isToolbarVisible,
        setSettingsSheetVisibility = { preferencesViewModel.setSettingsSheetVisibility(it) },
        setBookmarksVisibility = { bookmarkViewModel.setBookmarksVisibility(it) },
        fileInfo = fileInfo,
        navController = navController
    ) { boxWithConstraintsScope ->

        val pagerState = rememberPagerState(
            initialPage = fileInfo.curPage,
            pageCount = { fileInfo.pageCount }
        )
        LaunchedEffect(pagerState.currentPage) {
            contentViewModel.updateCurrentPage(pagerState.currentPage)
        }

        val maxWidth = boxWithConstraintsScope.constraints.maxWidth

        val isSettingsSheetVisible by preferencesViewModel.settingsSheetVisibility.collectAsState()
        val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
        val focusManager = LocalFocusManager.current

        val hideBottomSheetModifier = Modifier
            .pointerInput(isSettingsSheetVisible, isKeyboardVisible) {
                if (isSettingsSheetVisible && !isKeyboardVisible) {
                    detectTap(PointerEventPass.Initial) { preferencesViewModel.setSettingsSheetVisibility(false) }
                } else if (isKeyboardVisible) {
                    detectTap(PointerEventPass.Initial) { focusManager.clearFocus() }
                }
            }

        val bookmarks by bookmarkViewModel.bookmarks.collectAsState()
        val isBookmarkDialogVisible by bookmarkViewModel.bookmarksVisibility.collectAsState()

        BackHandler(enabled = !isToolbarVisible) {
            isToolbarVisible = true
        }
        if (isSettingsSheetVisible) {
            SettingsSheet(
                initialHeight = 0.35f,
                maxHeight = boxWithConstraintsScope.constraints.maxHeight,
                setSettingsSheetVisibility = { preferencesViewModel.setSettingsSheetVisibility(it) },
                fontSizeScale = fontSizeScale,
                lineSpacing = lineSpacing,
                pagePadding = pagePadding,
                pageSpacing = pageSpacing,
                paragraphSpacing = paragraphSpacing,
                updateFontSizeScale = { preferencesViewModel.updateFontSizeScale(it) },
                updateLineSpacing = { preferencesViewModel.updateLineSpacing(it.toInt()) },
                updatePagePadding = { preferencesViewModel.updatePagePadding(it.toInt()) },
                updatePageSpacing = { preferencesViewModel.updatePageSpacing(it.toInt()) },
                updateParagraphSpacing = { preferencesViewModel.updateParagraphSpacing(it.toInt()) },
                resetToDefaults = { preferencesViewModel.resetToDefaults() }
            )
        }
        if (isBookmarkDialogVisible) {
            BookmarksDialog(
                bookmarks = bookmarks,
                setBookmarksVisibility = { bookmarkViewModel.setBookmarksVisibility(it) },
                setPage = { page -> pagerState.scrollToPage(page) },
                curPage = pagerState.currentPage,
                addBookmark = { pageIndex -> bookmarkViewModel.addBookmark(pageIndex) },
                removeBookmark = { pageIndex -> bookmarkViewModel.deleteBookmark(pageIndex) },
                renameBookmark = { pageIndex, text ->
                    bookmarkViewModel.updateBookmarkText(pageIndex, text)
                }
            )
        }
        // TODO: add option for a continuous column of pages instead of individual pages
        // TODO: add option for showing pages in their original, unchanged form
        // TODO: add option to not extract all pages at once, but dynamically on the go
        Pager(
            pagerState = pagerState,
            getPageContent = { contentViewModel.getPageContent(it) },
            intToTextStyleMap = fileInfo.intToTextStyleMap,
            switchToolbarVisibility = {
                isToolbarVisible = !isToolbarVisible
                return@Pager isToolbarVisible
            },
            maxWidth = maxWidth,
            fontSizeScale = fontSizeScale,
            lineSpacing = lineSpacing,
            pagePadding = pagePadding,
            pageSpacing = pageSpacing,
            paragraphSpacing = paragraphSpacing,
            modifier = hideBottomSheetModifier,
        )
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