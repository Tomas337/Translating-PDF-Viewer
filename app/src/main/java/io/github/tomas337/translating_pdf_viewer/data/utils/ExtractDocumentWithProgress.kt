package io.github.tomas337.translating_pdf_viewer.data.utils

import io.github.tomas337.translating_pdf_viewer.data.utils.PdfExtractor.ExtractionListener
import io.github.tomas337.translating_pdf_viewer.utils.Document
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun PdfExtractor.extractDocumentWithProgress(): Flow<ExtractionEvent> = callbackFlow {
    val listener = object : ExtractionListener {
        override fun onFileInfo(title: String, pageCount: Int, thumbnailPath: String) {
            trySend(ExtractionEvent.FileInfo(title, pageCount, thumbnailPath))
        }

        override fun onPageProcessed(currentPage: Int, pagePath: String) {
            trySend(ExtractionEvent.PageProcessed(currentPage, pagePath))
        }

        override fun onDocumentExtracted(intToTextStyleMap: HashMap<Int, TextStyle>) {
            trySend(ExtractionEvent.DocumentExtracted(intToTextStyleMap))
            close()
        }
    }
    val job = CoroutineScope(Dispatchers.IO).launch {
        extractDocument(listener)
    }
    awaitClose { job.cancel() }
}