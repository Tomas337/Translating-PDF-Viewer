package io.github.tomas337.translating_pdf_viewer.domain.model

import io.github.tomas337.translating_pdf_viewer.utils.Page
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileModel(
    val id: Int = 0,
    val name: String,
    val language: String,
    val intToTextStyleMap: HashMap<Int, TextStyle>,
    val curPage: Page
)