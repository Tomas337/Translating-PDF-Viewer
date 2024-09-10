package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toFileModel
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel

class GetFileInfoUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(id: Int): FileModel {
        return fileInfoRepository.getFileInfo(id).toFileModel()
    }
}