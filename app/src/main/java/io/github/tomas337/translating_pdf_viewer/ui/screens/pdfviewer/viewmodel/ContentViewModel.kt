package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetFileInfoUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetPageContentUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.UpdateCurrentPageUseCase
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ContentViewModel(
    val fileId: Int,
    private val getFileInfoUseCase: GetFileInfoUseCase,
    private val updateCurrentPageUseCase: UpdateCurrentPageUseCase,
    private val getPageContentUseCase: GetPageContentUseCase,
) : ViewModel() {

    private val _fileInfo = MutableStateFlow(FileModel())
    val fileInfo: StateFlow<FileModel> = _fileInfo

    init {
        viewModelScope.launch {
            _fileInfo.value = getFileInfoUseCase(fileId)
        }
    }

    fun updateCurrentPage(pageIndex: Int) {
        viewModelScope.launch {
            updateCurrentPageUseCase(pageIndex, fileId)
        }
    }

    fun getPageContent(pageIndex: Int): Flow<List<List<PageContent>>> = flow {
        emit(getPageContentUseCase(pageIndex, fileId))
    }

    companion object {
        fun provideFactory(fileId: Int): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val fileInfoRepository = MyApp.appModule.fileInfoRepository
                    val pageRepository = MyApp.appModule.pageRepository
                    ContentViewModel(
                        fileId,
                        GetFileInfoUseCase(fileInfoRepository),
                        UpdateCurrentPageUseCase(fileInfoRepository),
                        GetPageContentUseCase(pageRepository),
                    )
                }
            }
        }
    }
}