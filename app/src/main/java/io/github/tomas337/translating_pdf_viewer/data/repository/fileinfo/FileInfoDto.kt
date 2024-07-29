package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileInfoDto(
    val id: Int = 0,
    val name: String,
    val language: String,
    val curPage: Int = 0,
    val intToTextStyleMap: HashMap<Int, TextStyle>
)