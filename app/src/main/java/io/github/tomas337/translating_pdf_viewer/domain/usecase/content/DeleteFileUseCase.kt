package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import android.content.Context
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.utils.extraction.PdfExtractor

class DeleteFileUseCase(
    private val fileInfoRepository: FileInfoRepository,
) {
    suspend operator fun invoke(context: Context, fileId: Int) {
        PdfExtractor.deleteDirs(context, fileId)
        fileInfoRepository.deleteFile(fileId)
    }
}