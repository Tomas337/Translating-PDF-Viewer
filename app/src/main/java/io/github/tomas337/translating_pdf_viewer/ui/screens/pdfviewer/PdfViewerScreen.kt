package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.tomas337.translating_pdf_viewer.R
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.ui.main.navigation.NavRoute
import io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel.PdfViewerViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PdfViewerScreen(
    navController: NavController,
    fileId: Int,
    pdfViewerViewModel: PdfViewerViewModel = viewModel(factory = PdfViewerViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(Unit) {
        pdfViewerViewModel.initFileInfo(fileId)
    }
    val fileInfo: FileModel by pdfViewerViewModel.fileInfo.observeAsState(FileModel())

    val pagerState = rememberPagerState(
        initialPage = fileInfo.curPage,
        pageCount = { fileInfo.maxPage }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoute.Home.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to home screen",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Contents",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.bookmarks_24dp),
                            contentDescription = "Bookmarks",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Display settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        VerticalPager(
            modifier = Modifier.padding(innerPadding),
            state = pagerState
        ) { pageNumber ->


        }
    }
}