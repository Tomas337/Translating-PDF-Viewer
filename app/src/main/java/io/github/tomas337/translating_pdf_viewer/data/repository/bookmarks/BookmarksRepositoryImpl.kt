package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarksDao
import io.github.tomas337.translating_pdf_viewer.data.mapper.toBookmarksDtoList

class BookmarksRepositoryImpl(
    private val bookmarksDao: BookmarksDao,
) : BookmarksRepository {

    override suspend fun getAllBookmarks(id: Int): List<BookmarksDto> {
        return bookmarksDao.getAllBookmarks(id).toBookmarksDtoList()
    }
}