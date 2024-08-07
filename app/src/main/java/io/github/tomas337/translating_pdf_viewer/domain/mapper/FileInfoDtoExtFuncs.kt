package io.github.tomas337.translating_pdf_viewer.domain.mapper

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel

fun List<FileInfoDto>.toFileModelList(): List<FileModel> {
    return map { fileInfoDto ->
        fileInfoDto.toFileModel()
    }
}

fun FileInfoDto.toFileModel(): FileModel {
    return FileModel(
        id = id,
        name = name,
        pageCount = pageCount,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap,
        thumbnailPath = thumbnailPath
    )
}