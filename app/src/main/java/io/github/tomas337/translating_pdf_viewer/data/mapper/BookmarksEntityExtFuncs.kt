package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarksEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarksDto

fun List<BookmarksEntity>.toBookmarksDtoList(): List<BookmarksDto> {
    return map { bookmarksEntity ->
        bookmarksEntity.toBookmarksDto()
    }
}

fun BookmarksEntity.toBookmarksDto(): BookmarksDto {
    return BookmarksDto(
        id = id,
        fileId = fileId,
        pageIndex = pageIndex,
        text = text
    )
}
