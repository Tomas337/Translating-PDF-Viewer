package io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkRepository
import io.github.tomas337.translating_pdf_viewer.domain.mapper.toBookmarkModelList
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    operator fun invoke(fileId: Int): Flow<List<BookmarkModel>> {
        return bookmarkRepository.getAllBookmarks(fileId).map { it.toBookmarkModelList() }
    }
}