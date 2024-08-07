package io.github.tomas337.translating_pdf_viewer.data.mapper

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto

fun List<FileInfoEntity>.toFileInfoDtoList(): List<FileInfoDto> {
    return map { fileInfoEntity ->
        fileInfoEntity.toFileInfoDto()
    }
}

fun FileInfoEntity.toFileInfoDto(): FileInfoDto {
    return FileInfoDto(
        id = id,
        name = name,
        pageCount = pageCount,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap,
        thumbnailPath = thumbnailPath
    )
}