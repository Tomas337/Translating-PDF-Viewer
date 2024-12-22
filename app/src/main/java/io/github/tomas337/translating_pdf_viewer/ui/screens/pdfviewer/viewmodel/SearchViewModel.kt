package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.GetPageSearchResultsUseCase
import kotlinx.coroutines.runBlocking

class SearchViewModel(
    private val getPageSearchResultsUseCase: GetPageSearchResultsUseCase
) : ViewModel() {

    // TODO: make it emit values after each page
    fun getAllHighlights(fileId: Int, pageCount: Int, pattern: String) = runBlocking {
        val fileHighlights = HashMap<Int, HashMap<Pair<Int, Int>, List<Pair<Int, Int>>>>()

        for (pageIndex in 0 until pageCount) {
            fileHighlights[pageIndex] = getPageSearchResultsUseCase(fileId, pageIndex, pattern)
        }

        return@runBlocking fileHighlights
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