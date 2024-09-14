package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
    val scaffoldState = rememberBottomSheetScaffoldState()
    val setSettingsSheetVisibility: (Boolean) -> Job = {
        coroutineScope.launch {
            if (it) {
                scaffoldState.bottomSheetState.show()
            } else {
                scaffoldState.bottomSheetState.hide()}
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        sheetContent = {
            SettingsSheet(setSettingsSheetVisibility)
        },
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
            BoxWithConstraints(
                modifier = Modifier.padding(innerPadding)
            ) {
                content(this)
            }
        }
    }
}