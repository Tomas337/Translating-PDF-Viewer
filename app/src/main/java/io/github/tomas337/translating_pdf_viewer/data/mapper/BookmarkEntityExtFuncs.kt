package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkDto

fun List<BookmarkEntity>.toBookmarkDtoList(): List<BookmarkDto> {
    return map { bookmarkEntity ->
        bookmarkEntity.toBookmarkDto()
    }
}

fun BookmarkEntity.toBookmarkDto(): BookmarkDto {
    return BookmarkDto(
        id = id,
        fileId = fileId,
        pageIndex = pageIndex,
        text = text
    )
}
