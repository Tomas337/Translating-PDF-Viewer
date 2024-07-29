package io.github.tomas337.translating_pdf_viewer.domain.model

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileInfoModel(
    val id: Int = 0,
    val name: String,
    val language: String,
    val curPage: Int = 0,
    val intToTextStyleMap: HashMap<Int, TextStyle>,
)