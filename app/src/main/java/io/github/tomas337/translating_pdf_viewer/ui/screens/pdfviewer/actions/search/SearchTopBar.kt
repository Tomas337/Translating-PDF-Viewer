package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    setSearchVisibility: (Boolean) -> Unit,
) {
    TopAppBar(
        modifier = Modifier,
        title = {
        },
        navigationIcon = {
            IconButton(onClick = { setSearchVisibility(false) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Hide search",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}