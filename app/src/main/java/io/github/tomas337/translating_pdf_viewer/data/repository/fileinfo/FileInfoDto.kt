package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import android.graphics.Bitmap
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

data class FileInfoDto(
    val id: Int = 0,
    val name: String,
    val maxPage: Int,
    val curPage: Int = 0,
    val intToTextStyleMap: HashMap<Int, TextStyle>,
    val thumbnailPath: String
)