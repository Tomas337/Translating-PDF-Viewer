package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FileInfoDao {

    @Query("SELECT * FROM file_info WHERE id == :id")
    suspend fun getFileInfo(id: Int): FileInfoEntity

    @Query("SELECT last_insert_rowid()")
    suspend fun getLastInsertedFileId(): Int

    @Insert
    suspend fun upsertFile(fileEntity: FileInfoEntity)

    @Query("DELETE FROM file_info WHERE id == :id")
    suspend fun deleteFile(id: Int)
}