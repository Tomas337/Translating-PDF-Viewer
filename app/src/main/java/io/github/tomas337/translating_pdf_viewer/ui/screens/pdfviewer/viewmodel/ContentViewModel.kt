package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.AddBookmarkUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.DeleteBookmarkUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.GetAllBookmarksUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.UpdateBookmarkTextUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetFileInfoUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetPageContentUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.UpdateCurrentPageUseCase
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import kotlinx.coroutines.launch

class ContentViewModel(
    val fileId: Int,
    private val getFileInfoUseCase: GetFileInfoUseCase,
    private val updateCurrentPageUseCase: UpdateCurrentPageUseCase,
    private val getPageContentUseCase: GetPageContentUseCase,
) : ViewModel() {

    private val _fileInfo = MutableLiveData<FileModel>()
    val fileInfo: LiveData<FileModel> = _fileInfo

    init {
        viewModelScope.launch {
            _fileInfo.postValue(getFileInfoUseCase(fileId))
        }
    }

    fun updateCurrentPage(pageIndex: Int) {
        viewModelScope.launch {
            updateCurrentPageUseCase(pageIndex, fileId)
        }
    }

    fun getPageContent(pageIndex: Int): LiveData<List<List<PageContent>>> {
        val result = MutableLiveData<List<List<PageContent>>>()
        viewModelScope.launch {
            result.postValue(getPageContentUseCase(pageIndex, fileId))
        }
        return result
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