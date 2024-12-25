package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.actions.search

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    setSearchVisibility: (Boolean) -> Unit,
    findHighlights: (String) -> Unit,
    resetState: () -> Unit,
    currentlySelected: Int,
    highlightsSize: Int,
) {
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember { mutableStateOf("") }

    TopAppBar(
        modifier = Modifier,
        title = {
        },
        navigationIcon = {
            IconButton(onClick = {
                setSearchVisibility(false)
                resetState()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Hide search",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            TextField(
                value = textFieldValue,
                onValueChange = {
                    resetState()
                    textFieldValue = it
                },
                placeholder = {
                    Text("Search")
                },
                suffix = {
                    if (highlightsSize != 0) {
                        Text("$currentlySelected/$highlightsSize")
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    if (textFieldValue.isNotEmpty()) {
                        findHighlights(textFieldValue)
                        focusManager.clearFocus()
                    }
                }),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedSuffixColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedSuffixColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .width(193.dp)
                    .semantics { contentDescription = "Search text field" }
            )
            Spacer(Modifier.width(8.dp))
            VerticalDivider(
                modifier = Modifier.height(40.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.width(4.dp))
            IconButton(
                enabled = highlightsSize > 0,
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Go to the previous highlight",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(
                enabled = highlightsSize > 0,
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Go to the next highlight",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(
                enabled = highlightsSize > 0,
                onClick = {
                    textFieldValue = ""
                    resetState()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear the text field",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}