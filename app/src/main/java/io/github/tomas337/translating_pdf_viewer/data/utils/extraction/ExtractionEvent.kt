package io.github.tomas337.translating_pdf_viewer.data.utils.extraction

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

sealed class ExtractionEvent {
    data class FileInfo(
        val title: String,
        val pageCount: Int,
        val thumbnailPath: String
    ) : ExtractionEvent()

    data class PageProcessed(
        val pageIndex: Int,
        val pagePath: String
    ) : ExtractionEvent()

    data class DocumentExtracted(val intToTextStyleMap: HashMap<Int, TextStyle>) : ExtractionEvent()
}