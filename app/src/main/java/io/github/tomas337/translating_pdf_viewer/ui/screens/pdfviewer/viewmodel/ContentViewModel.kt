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
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetFileInfoUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetPageContentUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.UpdateCurrentPageUseCase
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import kotlinx.coroutines.launch

class ContentViewModel(
    private val getFileInfoUseCase: GetFileInfoUseCase,
    private val updateCurrentPageUseCase: UpdateCurrentPageUseCase,
    private val getPageContentUseCase: GetPageContentUseCase,
) : ViewModel() {

    private val _fileInfo = MutableLiveData<FileModel>()
    val fileInfo: LiveData<FileModel> = _fileInfo

    fun initFileInfo(id: Int) {
        viewModelScope.launch {
            _fileInfo.postValue(getFileInfoUseCase(id))
        }
    }

    fun updateCurrentPage(pageIndex: Int, id: Int) {
        viewModelScope.launch {
            updateCurrentPageUseCase(pageIndex, id)
        }
    }

    fun getPageContent(pageIndex: Int, id: Int): LiveData<List<List<PageContent>>> {
        val result = MutableLiveData<List<List<PageContent>>>()
        viewModelScope.launch {
            result.postValue(getPageContentUseCase(pageIndex, id))
        }
        return result
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fileInfoRepository = MyApp.appModule.fileInfoRepository
                val pageRepository = MyApp.appModule.pageRepository
                ContentViewModel(
                    GetFileInfoUseCase(fileInfoRepository),
                    UpdateCurrentPageUseCase(fileInfoRepository),
                    GetPageContentUseCase(pageRepository),
                )
            }
        }
    }
}