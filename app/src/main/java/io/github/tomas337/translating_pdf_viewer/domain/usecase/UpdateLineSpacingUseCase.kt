package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository

class UpdateLineSpacingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(lineSpacing: Int) {
        preferencesRepository.saveLineSpacing(lineSpacing)
    }
}
