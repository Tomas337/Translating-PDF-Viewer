package io.github.tomas337.translating_pdf_viewer.domain.usecase.content

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageDto
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.utils.extraction.ExtractionEvent
import io.github.tomas337.translating_pdf_viewer.data.utils.extraction.PdfExtractor
import io.github.tomas337.translating_pdf_viewer.data.utils.extraction.extractDocumentWithProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import java.io.IOException

class AddFileUseCase(
    private val fileInfoRepository: FileInfoRepository,
    private val pageRepository: PageRepository
) {
    private val pagesProcessed = MutableStateFlow(0)
    private val pageCount = MutableStateFlow(1)

    fun getProgress(): Flow<Float> {
        pagesProcessed.value = 0
        pageCount.value = 1
        return combine(pagesProcessed, pageCount) { latestTaskDone, totalTasks ->
            latestTaskDone.toFloat() / totalTasks
        }
    }

    suspend operator fun invoke(context: Context, uri: Uri) {
        val fileId = fileInfoRepository.getLastInsertedFileId() + 1
        val pdfExtractor =
            PdfExtractor(
                context,
                uri,
                fileId
            )
        try {
            pdfExtractor.extractDocumentWithProgress()
                .onEach { event ->
                    if (event is ExtractionEvent.FileInfo) {
                        pageCount.value = event.pageCount
                        fileInfoRepository.insertFileInfo(
                            FileInfoDto(
                                id = fileId,
                                name = event.title,
                                pageCount = event.pageCount,
                                thumbnailPath = event.thumbnailPath
                            )
                        )
                    } else if (event is ExtractionEvent.PageProcessed) {
                        pageRepository.insertPage(
                            PageDto(
                                fileId = fileId,
                                pagePath = event.pagePath,
                                pageIndex = event.pageIndex,
                            ))
                        pagesProcessed.value += 1
                    }
                }
                .first { it is ExtractionEvent.DocumentExtracted }
                .let {
                    val event = it as ExtractionEvent.DocumentExtracted
                    fileInfoRepository.updateIntToTextStyleMap(
                        id = fileId,
                        intToTextStyleMap = event.intToTextStyleMap
                    )
                }
        } catch (e: IOException) {
            pdfExtractor.deleteDirs()
            fileInfoRepository.deleteFile(fileId)
            Log.e("file extraction failed", e.stackTraceToString())
        }
    }
}
