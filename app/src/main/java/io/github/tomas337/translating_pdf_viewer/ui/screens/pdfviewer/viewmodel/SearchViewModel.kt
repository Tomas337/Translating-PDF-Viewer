package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.GetPageSearchResultsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class SearchViewModel(
    private val getPageSearchResultsUseCase: GetPageSearchResultsUseCase
) : ViewModel() {

    private val _searchVisibility = MutableStateFlow(false)
    val searchVisibility: StateFlow<Boolean> = _searchVisibility

    fun setSearchVisibility(isVisible: Boolean) {
        _searchVisibility.value = isVisible
    }

    fun getHighlights(fileId: Int, pageCount: Int, pattern: String) = flow {
        for (pageIndex in 0 until pageCount) {
            emit(getPageSearchResultsUseCase(fileId, pageIndex, pattern))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val pageRepository = MyApp.appModule.pageRepository
                SearchViewModel(
                    GetPageSearchResultsUseCase(pageRepository)
                )
            }
        }
    }
}