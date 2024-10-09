package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

data class BookmarkDto(
    val id: Int = 0,
    val fileId: Int,
    val pageIndex: Int,
    val text: String,
)