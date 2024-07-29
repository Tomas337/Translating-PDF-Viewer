package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto

fun FileInfoEntity.toFileInfoDto(): FileInfoDto {
    return FileInfoDto(
        id = id,
        name = name,
        language = language,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap
    )
}