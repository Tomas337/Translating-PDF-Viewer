package io.github.tomas337.translating_pdf_viewer.data.local

import io.github.tomas337.translating_pdf_viewer.domain.utils.Page
import io.github.tomas337.translating_pdf_viewer.domain.utils.TextStyle

data class FileContent(
    val pages: List<Page>,
    val intToTextStyleMap: HashMap<Int, TextStyle>
)