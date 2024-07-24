package io.github.tomas337.translating_pdf_viewer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.io.File

@Dao
interface FileDao {
    @Query("SELECT * FROM files WHERE id == :id")
    fun getFile(id: Int): FileEntity

    @Insert
    suspend fun upsertFile(fileEntity: FileEntity)

    @Query("DELETE FROM files WHERE id == :id")
    suspend fun deleteSet(id: Int)
}