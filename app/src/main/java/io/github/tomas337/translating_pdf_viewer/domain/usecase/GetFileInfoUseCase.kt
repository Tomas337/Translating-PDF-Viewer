package io.github.tomas337.translating_pdf_viewer.domain.usecase

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toFileInfoModel
import io.github.tomas337.translating_pdf_viewer.domain.model.FileInfoModel

class GetFileInfoUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    suspend operator fun invoke(id: Int): FileInfoModel {
        return fileInfoRepository.getFileInfo(id).toFileInfoModel()
    }
}