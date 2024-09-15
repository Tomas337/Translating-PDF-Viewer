package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
    content: @Composable() (BoxWithConstraintsScope) -> Unit
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

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        sheetContent = {
            SettingsSheet(
                setSettingsSheetVisibility = setSettingsSheetVisibility,
            )
        },
        sheetPeekHeight = 300.dp,
        scaffoldState = scaffoldState,
        topBar = {
            if (isToolbarVisible) {
                PdfViewerTopBar(
                    navController = navController,
                    setSettingsSheetVisibility = setSettingsSheetVisibility
                )
            }
        },
    ) { innerPadding ->
        if (isInitialized) {

            // TODO implementation: if bottom sheet isn't hidden, use pointerInput to hide sheet on tap outside of the sheet
            BoxWithConstraints(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            ) {
                content(this)
            }
        }
    }
}