package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toFileInfoModelList
import io.github.tomas337.translating_pdf_viewer.domain.model.FileInfoModel

class GetAllFileInfoUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(): List<FileInfoModel> {
        return fileInfoRepository.getAllFileInfo().toFileInfoModelList()
    }
}