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
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.AddBookmarkUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.DeleteBookmarkUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.GetAllBookmarksUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks.UpdateBookmarkTextUseCase
import kotlinx.coroutines.launch

class BookmarkViewModel(
    val fileId: Int,
    private val getAllBookmarksUseCase: GetAllBookmarksUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val updateBookmarkTextUseCase: UpdateBookmarkTextUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase
) : ViewModel() {

    private val _bookmarksVisibility = MutableLiveData(false)
    val bookmarksVisibility: LiveData<Boolean> = _bookmarksVisibility

    private val _bookmarks = MutableLiveData<List<BookmarkModel>>()
    val bookmarks: LiveData<List<BookmarkModel>> = _bookmarks

    init {
        viewModelScope.launch {
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun addBookmark(pageIndex: Int, text: String = "Bookmark") {
        viewModelScope.launch {
            addBookmarkUseCase(fileId, pageIndex, text)
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun updateBookmarkText(pageIndex: Int, text: String) {
        viewModelScope.launch {
            updateBookmarkTextUseCase(fileId, pageIndex, text)
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun deleteBookmark(pageIndex: Int) {
        viewModelScope.launch {
            deleteBookmarkUseCase(fileId, pageIndex)
            _bookmarks.postValue(getAllBookmarksUseCase(fileId))
        }
    }

    fun setBookmarksVisibility(isVisible: Boolean) {
        _bookmarksVisibility.postValue(isVisible)
    }

    companion object {
        fun provideFactory(fileId: Int): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val bookmarkRepository = MyApp.appModule.bookmarkRepository
                    BookmarkViewModel(
                        fileId,
                        GetAllBookmarksUseCase(bookmarkRepository),
                        AddBookmarkUseCase(bookmarkRepository),
                        UpdateBookmarkTextUseCase(bookmarkRepository),
                        DeleteBookmarkUseCase(bookmarkRepository)
                    )
                }
            }
        }
    }
}
