package io.github.tomas337.translating_pdf_viewer.domain.mapper

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.domain.model.FileInfoModel

fun List<FileInfoDto>.toFileInfoModelList(): List<FileInfoModel> {
    return map { fileInfoDto ->
        fileInfoDto.toFileInfoModel()
    }
}

fun FileInfoDto.toFileInfoModel(): FileInfoModel {
    return FileInfoModel(
        id = id,
        name = name,
        language = language,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap,
    )
}