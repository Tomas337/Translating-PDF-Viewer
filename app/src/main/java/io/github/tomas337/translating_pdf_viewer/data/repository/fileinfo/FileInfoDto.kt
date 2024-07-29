package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import io.github.tomas337.translating_pdf_viewer.data.utils.TextStyle

data class FileInfoDto(
    val id: Int,
    val name: String,
    val language: String,
    val intToTextStyleMap: HashMap<Int, TextStyle>
)