package io.github.tomas337.translating_pdf_viewer.data.local.bookmarks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity

@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = FileInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["file_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["file_id"])
    ]
)
data class BookmarksEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "file_id")
    val fileId: Int,

    @ColumnInfo(name = "page_index")
    val pageIndex: Int,

    @ColumnInfo(name = "text")
    val text: String,
)
