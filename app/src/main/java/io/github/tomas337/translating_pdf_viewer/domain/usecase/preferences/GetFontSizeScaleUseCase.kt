package io.github.tomas337.translating_pdf_viewer.domain.usecase.preferences

import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetFontSizeScaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Float> {
        return preferencesRepository.readFontSizeScale()
    }
}