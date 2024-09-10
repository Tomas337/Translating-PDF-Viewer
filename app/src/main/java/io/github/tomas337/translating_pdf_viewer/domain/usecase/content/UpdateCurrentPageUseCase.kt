package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository

class UpdateCurrentPageUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(pageIndex: Int, id: Int) {
        fileInfoRepository.updateCurrentPage(pageIndex, id)
    }
}