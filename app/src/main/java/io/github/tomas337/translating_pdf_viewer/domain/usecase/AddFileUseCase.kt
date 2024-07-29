package io.github.tomas337.translating_pdf_viewer.domain.usecase

import android.content.Context
import android.net.Uri
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageDto
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.domain.utils.PdfExtractor

class AddFileUseCase(
    private val fileInfoRepository: FileInfoRepository,
    private val pageRepository: PageRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri) {
        val pdfExtractor = PdfExtractor(context, uri)
        val document = pdfExtractor.extractDocument()

        fileInfoRepository.upsertFileInfo(FileInfoDto(
            name = document.name,
            language = document.language,
            intToTextStyleMap = document.intToTextStyleMap
        ))

        val fileId = fileInfoRepository.getLastInsertedFileId()

        for ((i, page) in document.pages.withIndex()) {
            pageRepository.upsertPage(PageDto(
                fileId = fileId,
                page = page,
                pageNumber = i+1,
            ))
        }
    }
}
