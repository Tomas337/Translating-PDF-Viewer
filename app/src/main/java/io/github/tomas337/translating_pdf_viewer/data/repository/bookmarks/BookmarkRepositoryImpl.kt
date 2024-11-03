package io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkDao
import io.github.tomas337.translating_pdf_viewer.data.mapper.toBookmarkDtoList
import io.github.tomas337.translating_pdf_viewer.data.mapper.toBookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepositoryImpl(
    private val bookmarkDao: BookmarkDao,
) : BookmarkRepository {

    override fun getAllBookmarks(fileId: Int): Flow<List<BookmarkDto>> {
        return bookmarkDao.getAllBookmarks(fileId).map { it.toBookmarkDtoList() }
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