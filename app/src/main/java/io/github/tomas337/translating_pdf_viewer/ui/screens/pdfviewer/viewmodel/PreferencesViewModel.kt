package io.github.tomas337.translating_pdf_viewer.ui.screens.pdfviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.tomas337.translating_pdf_viewer.di.MyApp
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetFontSizeScaleUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetLineSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetPagePaddingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetPageSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.GetParagraphSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.UpdateFontSizeScaleUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.UpdateLineSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.UpdatePagePaddingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.UpdatePageSpacingUseCase
import io.github.tomas337.translating_pdf_viewer.domain.usecase.UpdateParagraphSpacingUseCase

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
