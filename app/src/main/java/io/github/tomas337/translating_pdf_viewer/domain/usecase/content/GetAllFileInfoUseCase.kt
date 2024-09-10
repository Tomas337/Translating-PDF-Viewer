package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toFileModelList
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel

class GetAllFileInfoUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(): List<FileModel> {
        return fileInfoRepository.getAllFileInfo().toFileModelList()
    }
}