package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

data class BookmarksDto(
    val id: Int,
    val fileId: Int,
    val pageIndex: Int,
    val text: String,
)