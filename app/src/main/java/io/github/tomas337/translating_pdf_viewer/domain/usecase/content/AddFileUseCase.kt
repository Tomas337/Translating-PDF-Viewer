package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageDto
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.utils.ExtractionEvent
import io.github.tomas337.translating_pdf_viewer.data.utils.PdfExtractor
import io.github.tomas337.translating_pdf_viewer.data.utils.extractDocumentWithProgress
import io.github.tomas337.translating_pdf_viewer.utils.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import java.io.IOException

class AddFileUseCase(
    private val fileInfoRepository: FileInfoRepository,
    private val pageRepository: PageRepository
) {
    val latestProcessedPage = MutableStateFlow(0)

    suspend operator fun invoke(context: Context, uri: Uri) {
        val fileId = fileInfoRepository.getLastInsertedFileId() + 1
        val pdfExtractor = PdfExtractor(context, uri, fileId)
        try {
            val document = pdfExtractor.extractDocumentWithProgress()
                .onEach { event ->
                    if (event is ExtractionEvent.PageProcessed) {
                        Log.d("PageProcessed", event.pageIndex.toString())
                        latestProcessedPage.value = event.pageIndex
                    }
                }
                .first { it is ExtractionEvent.DocumentExtracted }
                .let { (it as ExtractionEvent.DocumentExtracted).document }

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
