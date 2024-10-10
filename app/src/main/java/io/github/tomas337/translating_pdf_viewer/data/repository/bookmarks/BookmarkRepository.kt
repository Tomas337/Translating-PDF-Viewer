package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

interface BookmarkRepository {
    suspend fun getAllBookmarks(fileId: Int): List<BookmarkDto>
    suspend fun addBookmark(bookmarkDto: BookmarkDto)
    suspend fun updateBookmarkText(fileId: Int, pageIndex: Int, text: String)
    suspend fun deleteBookmark(fileId: Int, pageIndex: Int)
}