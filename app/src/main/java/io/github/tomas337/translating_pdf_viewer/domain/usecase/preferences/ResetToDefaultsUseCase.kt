package io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences;

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository;

class ResetToDefaultsUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke() {
        preferencesRepository.resetToDefaults()
    }
}
