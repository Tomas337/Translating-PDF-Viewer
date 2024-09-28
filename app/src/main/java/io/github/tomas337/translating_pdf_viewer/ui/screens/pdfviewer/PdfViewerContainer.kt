package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerContainer(
    isToolbarVisible: Boolean,
    isInitialized: Boolean,
    navController: NavController,
    content: @Composable (BoxWithConstraintsScope, Modifier) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    )
    val setSettingsSheetVisibility: (Boolean) -> Job = {
        coroutineScope.launch {
            if (it) {
                scaffoldState.bottomSheetState.partialExpand()
            } else {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }
    LaunchedEffect(Unit) {  // Workaround for bug where sheet state is set to expanded on every recomposition.
        coroutineScope.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val focusManager = LocalFocusManager.current

    Box (
        modifier = Modifier
            .pointerInput(isKeyboardVisible) {
                if (isKeyboardVisible) {
                    detectTap { focusManager.clearFocus() }
                }
            }
    ) {
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            sheetContent = {
                SettingsSheet()
            },
            sheetPeekHeight = 300.dp,
            scaffoldState = scaffoldState,
            topBar =
                if (isToolbarVisible) {{
                    PdfViewerTopBar(
                        navController = navController,
                        setSettingsSheetVisibility = setSettingsSheetVisibility
                    )
                }} else null,
        ) { innerPadding ->
            if (isInitialized) {
                val hideBottomSheetModifier = Modifier
                    .pointerInput(scaffoldState.bottomSheetState.isVisible, isKeyboardVisible) {
                        if (scaffoldState.bottomSheetState.isVisible && !isKeyboardVisible) {
                            detectTap(PointerEventPass.Initial) { setSettingsSheetVisibility(false) }
                        } else if (isKeyboardVisible) {
                            detectTap(PointerEventPass.Initial) { focusManager.clearFocus() }
                        }
                    }

                BoxWithConstraints(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                ) {
                    content(this, hideBottomSheetModifier)
                }
            }
        }
    }
}