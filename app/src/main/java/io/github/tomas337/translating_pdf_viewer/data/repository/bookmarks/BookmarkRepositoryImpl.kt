package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkDao
import io.github.tomas337.translating_pdf_viewer.data.mapper.toBookmarkDtoList
import io.github.tomas337.translating_pdf_viewer.data.mapper.toBookmarkEntity

class BookmarkRepositoryImpl(
    private val bookmarkDao: BookmarkDao,
) : BookmarkRepository {

    override suspend fun getAllBookmarks(fileId: Int): List<BookmarkDto> {
        return bookmarkDao.getAllBookmarks(fileId).toBookmarkDtoList()
    }

    override suspend fun addBookmark(bookmarkDto: BookmarkDto) {
        bookmarkDao.addBookmark(bookmarkDto.toBookmarkEntity())
    }

    override suspend fun updateBookmarkText(fileId: Int, pageIndex: Int, text: String) {
        bookmarkDao.updateBookmarkText(fileId, pageIndex, text)
    }

    override suspend fun deleteBookmark(fileId: Int, pageIndex: Int) {
        bookmarkDao.deleteBookmark(fileId, pageIndex)
    }
}