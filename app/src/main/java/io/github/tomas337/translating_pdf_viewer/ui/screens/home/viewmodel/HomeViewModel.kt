package io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.AddFileUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.DeleteFileUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetAllFileInfoUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.GetThumbnailPathUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.content.UpdateNameUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val addFileUseCase: AddFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
    private val getAllFileInfoUseCase: GetAllFileInfoUseCase,
    private val getThumbnailPathUseCase: GetThumbnailPathUseCase,
) : ViewModel() {

    private val _allFileInfo = MutableLiveData<List<FileModel>>()
    init {
        viewModelScope.launch {
            _allFileInfo.postValue(getAllFileInfoUseCase())
        }
    }
    val allFileInfo: LiveData<List<FileModel>> = _allFileInfo

    fun addFile(context: Context, uri: Uri) =
        viewModelScope.launch {
            addFileUseCase(context, uri)
            _allFileInfo.postValue(getAllFileInfoUseCase())
        }

    fun deleteFile(context: Context, id: Int) =
        viewModelScope.launch {
            deleteFileUseCase(context, id)
            _allFileInfo.postValue(getAllFileInfoUseCase())
        }

    fun updateName(name: String, id: Int) {
        viewModelScope.launch {
            updateNameUseCase(name, id)
            _allFileInfo.postValue(getAllFileInfoUseCase())
        }
    }

    fun getThumbnailPath(id: Int): LiveData<String> {
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            val thumbnailPath = getThumbnailPathUseCase(id)
            result.postValue(thumbnailPath)
        }
        return result
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fileInfoRepository = MyApp.appModule.fileInfoRepository
                val pageRepository = MyApp.appModule.pageRepository
                HomeViewModel(
                    AddFileUseCase(fileInfoRepository, pageRepository),
                    DeleteFileUseCase(fileInfoRepository),
                    UpdateNameUseCase(fileInfoRepository),
                    GetAllFileInfoUseCase(fileInfoRepository),
                    GetThumbnailPathUseCase(fileInfoRepository),
                )
            }
        }
    }
}