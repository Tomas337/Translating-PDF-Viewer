package io.github.tomas337.translating_pdf_viewer.data.repository.page

import io.github.tomas337.translating_pdf_viewer.utils.Page

data class PageDto(
    val id: Int = 0,
    val fileId: Int,
    val pagePath: String,
    val pageNumber: Int,
)