package io.github.tomas337.translating_pdf_viewer.data.repository.page

data class PageDto(
    val id: Int = 0,
    val fileId: Int,
    val pagePath: String,
    val pageIndex: Int,
)