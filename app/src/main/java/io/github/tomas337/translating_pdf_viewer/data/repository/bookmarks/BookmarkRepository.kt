package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

interface BookmarkRepository {
    suspend fun getAllBookmarks(id: Int): List<BookmarkDto>
}