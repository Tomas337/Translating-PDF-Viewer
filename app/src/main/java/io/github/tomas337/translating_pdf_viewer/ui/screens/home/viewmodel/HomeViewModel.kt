package io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory

class HomeViewModel(

) : ViewModel() {

    private val _uri = MutableStateFlow(Uri.EMPTY)
    val uri = _uri.asStateFlow()

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as MyFriendsApplication)
//                MainViewModel(application.container.myFriendsRepository)
//            }
        }
    }
}