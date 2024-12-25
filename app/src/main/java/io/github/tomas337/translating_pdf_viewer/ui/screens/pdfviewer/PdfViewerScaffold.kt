package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.search.SearchTopBar
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScaffold(
    isInitialized: Boolean,
    isToolbarVisible: Boolean,
    setSettingsSheetVisibility: (Boolean) -> Unit,
    setBookmarksVisibility: (Boolean) -> Unit,
    fileInfo: FileModel,
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
    content: @Composable (BoxWithConstraintsScope) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val isSearchVisible by searchViewModel.searchVisibility.collectAsState()

            if (isToolbarVisible && !isSearchVisible) {
                PdfViewerTopBar(
                    navController = navController,
                    setSettingsSheetVisibility = setSettingsSheetVisibility,
                    setBookmarksVisibility = setBookmarksVisibility,
                    setSearchVisibility = { searchViewModel.setSearchVisibility(it) },
                    modifier = Modifier
                        .pointerInput(isKeyboardVisible) {
                            if (isKeyboardVisible) {
                                detectTap(PointerEventPass.Initial) { focusManager.clearFocus() }
                            }
                        }
                        .semantics { contentDescription = "PdfViewer screen top bar" }
                )
            } else if (isToolbarVisible && isSearchVisible) {
                val currentlySelected by searchViewModel.currentlySelected.collectAsState()
                val highlightsSize by searchViewModel.highlightsSize.collectAsState()

                SearchTopBar(
                    setSearchVisibility = { searchViewModel.setSearchVisibility(it) },
                    findHighlights = { searchViewModel.findHighlights(fileInfo.id, fileInfo.pageCount, it) },
                    resetState = { searchViewModel.resetState() },
                    currentlySelected = currentlySelected,
                    highlightsSize = highlightsSize
                )
            }
        }
    ) { innerPadding ->
        if (isInitialized) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                content(this)
            }
        }
    }
}