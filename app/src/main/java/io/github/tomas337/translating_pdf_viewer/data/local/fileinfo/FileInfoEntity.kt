package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

@Entity(tableName = "file_info")
data class FileInfoEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "page_count")
    val pageCount: Int,

    @ColumnInfo(name = "cur_page")
    val curPage: Int = 0,

    @ColumnInfo(name = "int_to_textstyle_map")
    val intToTextStyleMap: HashMap<Int, TextStyle>,

    @ColumnInfo(name = "thumbnail_path")
    val thumbnailPath: String
)