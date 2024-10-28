package io.github.tomas337.translating_pdf_viewer.ui.screens.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import android.net.Uri
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val addFileUseCase: AddFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
    private val getAllFileInfoUseCase: GetAllFileInfoUseCase,
    private val getThumbnailPathUseCase: GetThumbnailPathUseCase,
) : ViewModel() {

    private val _allFileInfo = MutableStateFlow<List<FileModel>>(emptyList())
    val allFileInfo: StateFlow<List<FileModel>> = _allFileInfo

    private val _addFileProgress = MutableStateFlow(0f)
    val addFileProgress: StateFlow<Float> = _addFileProgress

    init {
        viewModelScope.launch {
            _allFileInfo.value = getAllFileInfoUseCase()
        }
    }

    fun addFile(context: Context, uri: Uri) =
        viewModelScope.launch {
            launch {
                addFileUseCase.getProgress().collect {
                    _addFileProgress.value = it
                }
            }
            launch {
                addFileUseCase(context, uri)
                _allFileInfo.value = getAllFileInfoUseCase()
            }
        }

    fun deleteFile(context: Context, id: Int) =
        viewModelScope.launch {
            deleteFileUseCase(context, id)
            _allFileInfo.value = getAllFileInfoUseCase()
        }

    fun updateName(name: String, id: Int) {
        viewModelScope.launch {
            updateNameUseCase(name, id)
            _allFileInfo.value = getAllFileInfoUseCase()
        }
    }

    fun getThumbnailPath(id: Int): Flow<String> = flow {
        emit(getThumbnailPathUseCase(id))
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