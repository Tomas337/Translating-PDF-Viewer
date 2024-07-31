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
import io.github.tomas337.translating_pdf_viewer.domain.usecase.AddFileUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.DeleteFileUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetAllFileInfoUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetThumbnailPathUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.UpdateNameUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val addFileUseCase: AddFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val getAllFileInfoUseCase: GetAllFileInfoUseCase,
    private val getThumbnailPathUseCase: GetThumbnailPathUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
) : ViewModel() {

    fun addFile(context: Context, uri: Uri) =
        viewModelScope.launch {
            addFileUseCase(context, uri)
        }

    fun deleteFile(context: Context, fileId: Int) =
        viewModelScope.launch {
            deleteFileUseCase(context, fileId)
        }

    fun getAllFileInfo(): LiveData<List<FileModel>> {
        val result = MutableLiveData<List<FileModel>>()
        viewModelScope.launch {
            val allFileInfo = getAllFileInfoUseCase()
            result.postValue(allFileInfo)
        }
        return result
    }

    fun getThumbnailPath(id: Int): LiveData<String> {
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            val thumbnailPath = getThumbnailPathUseCase(id)
            result.postValue(thumbnailPath)
        }
        return result
    }

    fun updateName(name: String, id: Int) {
        viewModelScope.launch {
            updateNameUseCase(name, id)
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fileInfoRepository = MyApp.appModule.fileInfoRepository
                val pageRepository = MyApp.appModule.pageRepository
                HomeViewModel(
                    AddFileUseCase(fileInfoRepository, pageRepository),
                    DeleteFileUseCase(fileInfoRepository),
                    GetAllFileInfoUseCase(fileInfoRepository),
                    GetThumbnailPathUseCase(fileInfoRepository),
                    UpdateNameUseCase(fileInfoRepository)
                )
            }
        }
    }
}