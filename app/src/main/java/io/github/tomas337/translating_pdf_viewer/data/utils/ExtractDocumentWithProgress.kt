package io.github.tomas337.translating_pdf_viewer.data.utils

import io.github.tomas337.translating_pdf_viewer.data.utils.PdfExtractor.ExtractionListener
import io.github.tomas337.translating_pdf_viewer.utils.Document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun PdfExtractor.extractDocumentWithProgress(): Flow<ExtractionEvent> = callbackFlow {
    val listener = object : ExtractionListener {
        override fun onPageCount(pageCount: Int) {
            trySend(ExtractionEvent.PageCount(pageCount))
        }

        override fun onPageProcessed(currentPage: Int) {
            trySend(ExtractionEvent.PageProcessed(currentPage))
        }

        override fun onDocumentExtracted(document: Document) {
            trySend(ExtractionEvent.DocumentExtracted(document))
            close()
        }
    }
    val job = CoroutineScope(Dispatchers.IO).launch {
        extractDocument(listener)
    }
    awaitClose { job.cancel() }
}