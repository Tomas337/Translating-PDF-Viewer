package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

@Entity(tableName = "file_info")
data class FileInfoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "int_to_textstyle_map")
    val intToTextStyleMap: HashMap<Int, TextStyle>
)