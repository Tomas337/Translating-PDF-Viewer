package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetLineSpacingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Int> {
        return preferencesRepository.readLineSpacing()
    }
}
