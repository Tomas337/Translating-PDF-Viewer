package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toFileModelList
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllFileInfoUseCase(
    private val fileInfoRepository: FileInfoRepository
) {
    operator fun invoke(): Flow<List<FileModel>> {
        return fileInfoRepository.getAllFileInfo().map { it.toFileModelList() }
    }
}