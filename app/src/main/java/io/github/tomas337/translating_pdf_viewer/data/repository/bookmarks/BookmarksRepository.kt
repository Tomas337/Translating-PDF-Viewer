package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

interface BookmarksRepository {
    suspend fun getAllBookmarks(id: Int): List<BookmarksDto>
}