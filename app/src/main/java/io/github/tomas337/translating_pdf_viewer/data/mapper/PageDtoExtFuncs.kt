package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageDto

fun PageDto.toPageEntity(): PageEntity {
    return PageEntity(
        id = id,
        fileId = fileId,
        pagePath = pagePath,
        pageNumber = pageNumber
    )
}