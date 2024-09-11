package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.GetFontSizeScaleUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.GetLineSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.GetPagePaddingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.GetPageSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.GetParagraphSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.UpdateFontSizeScaleUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.UpdateLineSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.UpdatePagePaddingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.UpdatePageSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences.UpdateParagraphSpacingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val getFontSizeScaleUseCase: GetFontSizeScaleUseCase,
    private val getLineSpacingUseCase: GetLineSpacingUseCase,
    private val getPagePaddingUseCase: GetPagePaddingUseCase,
    private val getPageSpacingUseCase: GetPageSpacingUseCase,
    private val getParagraphSpacingUseCase: GetParagraphSpacingUseCase,
    private val updateFontSizeScaleUseCase: UpdateFontSizeScaleUseCase,
    private val updateLineSpacingUseCase: UpdateLineSpacingUseCase,
    private val updatePagePaddingUseCase: UpdatePagePaddingUseCase,
    private val updatePageSpacingUseCase: UpdatePageSpacingUseCase,
    private val updateParagraphSpacingUseCase: UpdateParagraphSpacingUseCase
) : ViewModel() {

    private val _fontSizeScale = MutableStateFlow(0f)
    private val _lineSpacing = MutableStateFlow(0)
    private val _pagePadding = MutableStateFlow(0.dp)
    private val _pageSpacing = MutableStateFlow(0.dp)
    private val _paragraphSpacing = MutableStateFlow(0.dp)

    val fontSizeScale = _fontSizeScale
    val lineSpacing = _lineSpacing
    val pagePadding = _pagePadding
    val pageSpacing = _pageSpacing
    val paragraphSpacing = _paragraphSpacing

    init {
        viewModelScope.launch {
            getFontSizeScaleUseCase().collect {
                _fontSizeScale.value = it
            }
            getLineSpacingUseCase().collect {
                _lineSpacing.value = it
            }
            getPagePaddingUseCase().collect {
                _pagePadding.value = it.dp
            }
            getPageSpacingUseCase().collect {
                _pageSpacing.value = it.dp
            }
            getParagraphSpacingUseCase().collect {
                _paragraphSpacing.value = it.dp
            }
        }
    }

    fun getFontSizeScale(): Flow<Float> {
        return fontSizeScale
    }

    fun getLineSpacing(): Flow<Int> {
        return lineSpacing
    }

    fun getPagePadding(): Flow<Dp> {
        return pagePadding
    }

    fun getPageSpacing(): Flow<Dp> {
        return pageSpacing
    }

    fun getParagraphSpacing(): Flow<Dp> {
        return paragraphSpacing
    }

    fun updateFontSizeScale(newFontSizeScale: Float) {
        viewModelScope.launch {
            updateFontSizeScaleUseCase(newFontSizeScale)
        }
    }

    fun updateLineSpacing(newLineSpacing: Int) {
        viewModelScope.launch {
            updateLineSpacingUseCase(newLineSpacing)
        }
    }

    fun updatePagePadding(newPagePadding: Int) {
        viewModelScope.launch {
            updatePagePaddingUseCase(newPagePadding)
        }
    }

    fun updatePageSpacing(newPageSpacing: Int) {
        viewModelScope.launch {
            updatePageSpacingUseCase(newPageSpacing)
        }
    }

    fun updateParagraphSpacing(newParagraphSpacing: Int) {
        viewModelScope.launch {
            updateParagraphSpacingUseCase(newParagraphSpacing)
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val preferencesRepository = MyApp.appModule.preferencesRepository
                PreferencesViewModel(
                    GetFontSizeScaleUseCase(preferencesRepository),
                    GetLineSpacingUseCase(preferencesRepository),
                    GetPagePaddingUseCase(preferencesRepository),
                    GetPageSpacingUseCase(preferencesRepository),
                    GetParagraphSpacingUseCase(preferencesRepository),
                    UpdateFontSizeScaleUseCase(preferencesRepository),
                    UpdateLineSpacingUseCase(preferencesRepository),
                    UpdatePagePaddingUseCase(preferencesRepository),
                    UpdatePageSpacingUseCase(preferencesRepository),
                    UpdateParagraphSpacingUseCase(preferencesRepository)
                )
            }
        }
    }
}
