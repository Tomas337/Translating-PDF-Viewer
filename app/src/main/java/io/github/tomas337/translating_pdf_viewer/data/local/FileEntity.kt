package io.github.tomas337.translating_pdf_viewer.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.tomas337.translating_pdf_viewer.data.utils.Page
import io.github.tomas337.translating_pdf_viewer.data.utils.TextStyle

// TODO split into two entities for efficiency?
@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "pages")
//    TODO @TypeConverters(PageListConverter::class)
    val pages: List<Page>,

    @ColumnInfo(name = "int_to_textstyle_map")
//    TODO @TypeConverters(TextStyleMapConverter::class)
    val intToTextStyleMap: HashMap<Int, TextStyle>
)
