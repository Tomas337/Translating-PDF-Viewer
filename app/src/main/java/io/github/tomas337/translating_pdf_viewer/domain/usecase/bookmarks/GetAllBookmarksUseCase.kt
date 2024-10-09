package io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toBookmarkModelList
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel

class GetAllBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(fileId: Int): List<BookmarkModel> {
        return bookmarkRepository.getAllBookmarks(fileId).toBookmarkModelList()
    }
}