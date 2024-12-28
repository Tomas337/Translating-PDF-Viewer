package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.GetPageSearchResultsUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils.Highlight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getPageSearchResultsUseCase: GetPageSearchResultsUseCase
) : ViewModel() {

    private val _searchVisibility = MutableStateFlow(false)
    val searchVisibility: StateFlow<Boolean> = _searchVisibility

    private val _highlightsStructured = mutableListOf<HashMap<Pair<Int, Int>, List<Highlight>>>()
    val highlightsStructured: List<HashMap<Pair<Int, Int>, List<Highlight>>> = _highlightsStructured

    private val _highlights = mutableListOf<Pair<Int, Highlight>>()
    val highlights: List<Pair<Int, Highlight>> = _highlights

    private val _currentlySelected = MutableStateFlow(-1)
    val currentlySelected: StateFlow<Int> = _currentlySelected

    private val _highlightsSize = MutableStateFlow(0)
    val highlightsSize: StateFlow<Int> = _highlightsSize

    fun setSearchVisibility(isVisible: Boolean) {
        _searchVisibility.value = isVisible
    }

    fun findHighlights(fileId: Int, pageCount: Int, pattern: String) {
        resetState()
        viewModelScope.launch {
            launch {
                getPageSearchResultsUseCase.getNumberOfOccurrences().collect {
                    _highlightsSize.value = it
                }
            }
            for (pageIndex in 0 until pageCount) {
                val pageHighlights = getPageSearchResultsUseCase(fileId, pageIndex, pattern)
                _highlightsStructured.add(pageHighlights)

                pageHighlights.values.forEach {
                    _highlights.addAll(it.map { Pair(pageIndex, it) })
                }
            }
        }
    }

    fun selectNextHighlight() {
        if (_currentlySelected.value < _highlightsSize.value - 1) {
            _currentlySelected.value += 1
        } else if (_highlightsSize.value > 0) {
            _currentlySelected.value = 0
        }
    }

    fun selectPreviousHighlight() {
        if (_currentlySelected.value > 0) {
            _currentlySelected.value -= 1
        } else {
            _currentlySelected.value = _highlightsSize.value - 1
        }
    }

    fun resetState() {
        _highlightsStructured.clear()
        _currentlySelected.value = -1
        _highlightsSize.value = 0
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