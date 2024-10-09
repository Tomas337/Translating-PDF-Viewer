package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel
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

class BookmarkViewModel(
    private val getAllBookmarksUseCase: GetAllBookmarksUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val updateBookmarkTextUseCase: UpdateBookmarkTextUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase
) : ViewModel() {

    private val _bookmarks = MutableLiveData<List<BookmarkModel>>()
    val bookmarks: LiveData<List<BookmarkModel>> = _bookmarks

    fun initBookmarks(fileId: Int) {
        viewModelScope.launch {
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun addBookmark(fileId: Int, pageIndex: Int, text: String) {
        viewModelScope.launch {
            addBookmarkUseCase(fileId, pageIndex, text)
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun updateBookmarkText(fileId: Int, text: String) {
        viewModelScope.launch {
            updateBookmarkTextUseCase(fileId, text)
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun deleteBookmark(fileId: Int, pageIndex: Int) {
        viewModelScope.launch {
            deleteBookmarkUseCase(fileId, pageIndex)
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bookmarkRepository = MyApp.appModule.bookmarkRepository
                BookmarkViewModel(
                    GetAllBookmarksUseCase(bookmarkRepository),
                    AddBookmarkUseCase(bookmarkRepository),
                    UpdateBookmarkTextUseCase(bookmarkRepository),
                    DeleteBookmarkUseCase(bookmarkRepository)
                )
            }
        }
    }
}
