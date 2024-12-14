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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScaffold(
    isInitialized: Boolean,
    isToolbarVisible: Boolean,
    setSettingsSheetVisibility: (Boolean) -> Unit,
    setBookmarksVisibility: (Boolean) -> Unit,
    navController: NavController,
    content: @Composable (BoxWithConstraintsScope) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (isToolbarVisible) {
                PdfViewerTopBar(
                    navController = navController,
                    setSettingsSheetVisibility = setSettingsSheetVisibility,
                    setBookmarksVisibility = setBookmarksVisibility,
                    modifier = Modifier
                        .pointerInput(isKeyboardVisible) {
                            if (isKeyboardVisible) {
                                detectTap(PointerEventPass.Initial) { focusManager.clearFocus() }
                            }
                        }
                        .semantics { contentDescription = "PdfViewer screen top bar" }
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