package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository

class UpdatePagePaddingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(pagePadding: Int) {
        preferencesRepository.savePagePadding(pagePadding)
    }
}
