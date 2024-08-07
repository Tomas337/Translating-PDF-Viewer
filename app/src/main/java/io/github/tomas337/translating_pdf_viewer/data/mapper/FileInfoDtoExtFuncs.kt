package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto

fun FileInfoDto.toFileInfoEntity(): FileInfoEntity {
    return FileInfoEntity(
        id = id,
        name = name,
        pageCount = pageCount,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap,
        thumbnailPath = thumbnailPath
    )
}