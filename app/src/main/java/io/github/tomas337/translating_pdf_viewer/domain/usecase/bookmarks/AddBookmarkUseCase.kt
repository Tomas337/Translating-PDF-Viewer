package io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkDto
import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkRepository

class AddBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(fileId: Int, pageIndex: Int, text: String) {
        bookmarkRepository.addBookmark(BookmarkDto(
            fileId = fileId,
            pageIndex = pageIndex,
            text = text
        ))
    }
}