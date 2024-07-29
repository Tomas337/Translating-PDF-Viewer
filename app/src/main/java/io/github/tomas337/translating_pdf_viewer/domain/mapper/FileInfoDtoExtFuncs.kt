package io.github.tomas337.translating_pdf_viewer.domain.mapper

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoDto
import io.github.tomas337.translating_pdf_viewer.domain.model.FileModel
import io.github.tomas337.translating_pdf_viewer.data.utils.Page

fun FileInfoDto.toFileModel(
    curPage: Page
): FileModel {
    return FileModel(
        id = id,
        name = name,
        language = language,
        intToTextStyleMap = intToTextStyleMap,
        curPage = curPage
    )
}