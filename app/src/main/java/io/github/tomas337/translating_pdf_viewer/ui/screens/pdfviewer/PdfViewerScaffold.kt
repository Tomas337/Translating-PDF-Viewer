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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.bookmarks.BookmarksDialog
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.settings.SettingsSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScaffold(
    fileId: Int,
    isToolbarVisible: Boolean,
    isInitialized: Boolean,
    navController: NavController,
    content: @Composable (BoxWithConstraintsScope, Modifier) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var isSettingsSheetVisible by remember { mutableStateOf(false) }
    val setSettingsSheetVisibility: (Boolean) -> Unit = {
        isSettingsSheetVisible = it
    }
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val focusManager = LocalFocusManager.current

    var isBookmarkDialogVisible by remember { mutableStateOf(false) }
    val setBookmarkDialogVisibility: (Boolean) -> Unit = {
        isBookmarkDialogVisible = it
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (isToolbarVisible) {
                PdfViewerTopBar(
                    navController = navController,
                    setSettingsSheetVisibility = setSettingsSheetVisibility,
                    setBookmarkDialogVisibility = setBookmarkDialogVisibility,
                    modifier = Modifier
                        .pointerInput(isKeyboardVisible) {
                            if (isKeyboardVisible) {
                                detectTap(PointerEventPass.Initial) { focusManager.clearFocus() }
                            }
                        }
                )
            }
        }
    ) { innerPadding ->
        if (isBookmarkDialogVisible) {
            BookmarksDialog(
                fileId = fileId,
                setDialogVisibility = setBookmarkDialogVisibility
            )
        }
        if (isInitialized) {
            val hideBottomSheetModifier = Modifier
                .pointerInput(isSettingsSheetVisible, isKeyboardVisible) {
                    if (isSettingsSheetVisible && !isKeyboardVisible) {
                        detectTap(PointerEventPass.Initial) { setSettingsSheetVisibility(false) }
                    } else if (isKeyboardVisible) {
                        detectTap(PointerEventPass.Initial) { focusManager.clearFocus() }
                    }
                }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                content(this, hideBottomSheetModifier)
                if (isSettingsSheetVisible) {
                    SettingsSheet(
                        initialHeight = 0.3f,
                        maxHeight = this.constraints.maxHeight,
                        setSettingsSheetVisibility = setSettingsSheetVisibility,
                    )
                }
            }
        }
    }
}