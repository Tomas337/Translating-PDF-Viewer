package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository

class GetThumbnailPathUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(id: Int): String {
        return fileInfoRepository.getThumbnailPath(id)
    }
}