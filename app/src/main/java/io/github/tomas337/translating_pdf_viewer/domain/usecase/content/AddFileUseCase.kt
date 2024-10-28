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
    private val latestTaskDone = MutableStateFlow(0)
    private val totalTasks = MutableStateFlow(1)

    fun getProgress(): Flow<Float> {
        latestTaskDone.value = 0
        totalTasks.value = 1
        return combine(latestTaskDone, totalTasks) { latestTaskDone, totalTasks ->
            latestTaskDone.toFloat() / totalTasks
        }
    }

    suspend operator fun invoke(context: Context, uri: Uri) {
        val fileId = fileInfoRepository.getLastInsertedFileId() + 1
        val pdfExtractor = PdfExtractor(context, uri, fileId)
        try {
            val document = pdfExtractor.extractDocumentWithProgress()
                .onEach { event ->
                    if (event is ExtractionEvent.PageCount) {
                        // Extract and insert pages and insert file info
                        totalTasks.value = event.pageCount * 2 + 1
                    } else if (event is ExtractionEvent.PageProcessed) {
                        latestTaskDone.value = event.pageIndex + 1
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
            latestTaskDone.value += 1

            for ((i, pagePath) in document.pagePaths.withIndex()) {
                pageRepository.insertPage(
                    PageDto(
                        fileId = fileId,
                        pagePath = pagePath,
                    pageIndex = i,
                ))
                latestTaskDone.value += 1
            }
        } catch (e: IOException) {
            pdfExtractor.deleteDirs()
            Log.e("file extraction failed", e.stackTraceToString())
        }
    }
}
