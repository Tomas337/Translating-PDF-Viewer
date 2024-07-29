package io.github.tomas337.translating_pdf_viewer.domain.mapper

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.domain.model.FileInfoModel

fun FileInfoDto.toFileInfoModel(): FileInfoModel {
    return FileInfoModel(
        id = id,
        name = name,
        language = language,
        curPage = curPage,
        intToTextStyleMap = intToTextStyleMap,
    )
}