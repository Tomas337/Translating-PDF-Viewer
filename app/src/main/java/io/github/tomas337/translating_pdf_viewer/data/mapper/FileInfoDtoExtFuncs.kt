package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import kotlin.math.max

fun FileInfoDto.toFileInfoEntity(): FileInfoEntity {
    return FileInfoEntity(
        id = id,
        name = name,
        maxPage = maxPage,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap,
        thumbnailPath = thumbnailPath
    )
}