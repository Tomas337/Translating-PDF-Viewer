package io.github.tomas337.translating_pdf_viewer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.io.File

@Dao
interface FileDao {
    @Query("SELECT name, language FROM files WHERE id == :id")
    suspend fun getFileInfo(id: Int): FileInfo

    @Query("SELECT pages, int_to_textstyle_map FROM files WHERE id == :id")
    suspend fun getFileContent(id: Int): FileContent

    @Insert
    suspend fun upsertFile(fileEntity: FileEntity)

    @Query("DELETE FROM files WHERE id == :id")
    suspend fun deleteFile(id: Int)
}