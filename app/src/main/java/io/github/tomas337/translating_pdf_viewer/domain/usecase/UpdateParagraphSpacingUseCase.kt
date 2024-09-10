package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository

class UpdateParagraphSpacingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(paragraphSpacing: Int) {
        preferencesRepository.saveParagraphSpacing(paragraphSpacing)
    }
}
