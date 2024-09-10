package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository

class UpdateNameUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(name: String, id: Int) {
        fileInfoRepository.updateName(name, id)
    }
}