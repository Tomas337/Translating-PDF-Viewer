package io.github.tomas337.translating_pdf_viewer.domain.model

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileModel(
    val id: Int = 0,
    val name: String = "",
    val maxPage: Int = 0,
    val curPage: Int = 0,
    val intToTextStyleMap: HashMap<Int, TextStyle> = hashMapOf(),
    val thumbnailPath: String = ""
)