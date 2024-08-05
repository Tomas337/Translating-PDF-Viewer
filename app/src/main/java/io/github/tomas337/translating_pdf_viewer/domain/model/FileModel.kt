package io.github.tomas337.translating_pdf_viewer.domain.model

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileModel(
    val id: Int = 0,
    val name: String,
    val maxPage: Int,
    val curPage: Int = 0,
    val intToTextStyleMap: HashMap<Int, TextStyle>,
    val thumbnailPath: String
)