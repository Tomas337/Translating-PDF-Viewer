package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkDao
import io.github.tomas337.translating_pdf_viewer.data.mapper.toBookmarkDtoList

class BookmarkRepositoryImpl(
    private val bookmarkDao: BookmarkDao,
) : BookmarkRepository {

    override suspend fun getAllBookmarks(id: Int): List<BookmarkDto> {
        return bookmarkDao.getAllBookmarks(id).toBookmarkDtoList()
    }
}