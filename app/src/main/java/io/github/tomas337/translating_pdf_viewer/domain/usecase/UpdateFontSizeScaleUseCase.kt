package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository

class UpdateFontSizeScaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(fontSizeScale: Float) {
        preferencesRepository.saveFontSizeScale(fontSizeScale)
    }
}
