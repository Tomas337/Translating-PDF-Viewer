package io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.usecase.AddFileUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetAllFileInfoUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetFileInfoUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val addFileUseCase: AddFileUseCase,
    private val getFileInfoUseCase: GetFileInfoUseCase,
    private val getAllFileInfoUseCase: GetAllFileInfoUseCase
) : ViewModel() {

    fun addFile(context: Context, uri: Uri) =
        viewModelScope.launch {
            addFileUseCase(context, uri)
        }

    fun getAllFileInfo() =
        viewModelScope.launch {
            TODO("create composables to load the info to the page")
            getAllFileInfoUseCase()
        }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fileInfoRepository = MyApp.appModule.fileInfoRepository
                val pageRepository = MyApp.appModule.pageRepository
                HomeViewModel(
                    AddFileUseCase(fileInfoRepository, pageRepository),
                    GetFileInfoUseCase(fileInfoRepository),
                    GetAllFileInfoUseCase(fileInfoRepository)
                )
            }
        }
    }
}