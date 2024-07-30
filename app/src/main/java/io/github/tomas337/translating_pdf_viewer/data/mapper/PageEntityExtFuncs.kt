package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageDto

fun PageEntity.toPageDto(): PageDto {
    return PageDto(
        id = id,
        fileId = fileId,
        pagePath = pagePath,
        pageNumber = pageNumber
    )
}
