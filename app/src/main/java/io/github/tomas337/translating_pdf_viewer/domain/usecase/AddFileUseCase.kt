package io.github.tomas337.translating_pdf_viewer.domain.usecase

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageDto
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.utils.PdfExtractor
import java.io.IOException

class AddFileUseCase(
    private val fileInfoRepository: FileInfoRepository,
    private val pageRepository: PageRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri) {
        val fileId = fileInfoRepository.getLastInsertedFileId() + 1
        val pdfExtractor = PdfExtractor(context, uri, fileId)
        try {
            val document = pdfExtractor.extractAndSaveDocument()

            fileInfoRepository.insertFileInfo(
                FileInfoDto(
                    id = fileId,
                    name = document.name,
                    pageCount = document.pageCount,
                    intToTextStyleMap = document.intToTextStyleMap,
                    thumbnailPath = document.pathOfThumbnail
                )
            )

            for ((i, pagePath) in document.pagePaths.withIndex()) {
                pageRepository.insertPage(
                    PageDto(
                        fileId = fileId,
                        pagePath = pagePath,
                    pageIndex = i,
                ))
            }
        } catch (e: IOException) {
            pdfExtractor.deleteDirs()
            Log.e("file extraction failed", e.stackTraceToString())
        }
    }
}
