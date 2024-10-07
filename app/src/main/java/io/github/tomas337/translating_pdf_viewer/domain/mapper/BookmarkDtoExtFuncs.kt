package io.github.tomas337.translating_pdf_viewer.domain.mapper

import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkDto
import io.github.tomas337.translating_pdf_viewer.domain.model.BookmarkModel

fun List<BookmarkDto>.toBookmarkModelList(): List<BookmarkModel> {
    return map { bookmarkDto ->
        bookmarkDto.toBookmarkModel()
    }
}

fun BookmarkDto.toBookmarkModel(): BookmarkModel {
    return BookmarkModel(
        pageIndex = pageIndex,
        text = text
    )
}