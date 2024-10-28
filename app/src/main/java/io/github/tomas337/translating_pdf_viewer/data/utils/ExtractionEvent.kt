package io.github.tomas337.translating_pdf_viewer.data.utils

import io.github.tomas337.translating_pdf_viewer.utils.Document

sealed class ExtractionEvent {
    data class PageCount(val pageCount: Int) : ExtractionEvent()
    data class PageProcessed(val pageIndex: Int) : ExtractionEvent()
    data class DocumentExtracted(val document: Document) : ExtractionEvent()
}