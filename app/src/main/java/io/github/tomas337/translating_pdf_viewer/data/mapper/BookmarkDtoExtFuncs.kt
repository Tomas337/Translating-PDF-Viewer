package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkDto

fun BookmarkDto.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        id = id,
        fileId = fileId,
        pageIndex = pageIndex,
        text = text
    )
}