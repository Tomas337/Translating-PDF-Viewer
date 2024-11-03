package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getAllBookmarks(fileId: Int): Flow<List<BookmarkDto>>
    suspend fun addBookmark(bookmarkDto: BookmarkDto)
    suspend fun updateBookmarkText(fileId: Int, pageIndex: Int, text: String)
    suspend fun deleteBookmark(fileId: Int, pageIndex: Int)
}