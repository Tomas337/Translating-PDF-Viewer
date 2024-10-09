package io.github.tomas337.translating_pdf_viewer.domain.usecase.bookmarks

import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkRepository

class UpdateBookmarkTextUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(fileId: Int, text: String) {
        bookmarkRepository.updateBookmarkText(fileId, text)
    }
}