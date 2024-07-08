package io.github.tomas337.translating_pdf_viewer.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.net.Uri

class MainActivityViewModel : ViewModel() {
    private val _uri = MutableStateFlow(Uri.EMPTY)
    val uri = _uri.asStateFlow()

    fun setUri(uri: Uri) {
        _uri.value = uri
    }
}