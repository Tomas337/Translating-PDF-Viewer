package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileInfoDto(
    val id: Int = 0,
    val name: String,
    val pageCount: Int,
    val curPage: Int = 0,
    val intToTextStyleMap: HashMap<Int, TextStyle> = hashMapOf(),
    val thumbnailPath: String
)