package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FileInfoDao {

    @Query("SELECT * FROM file_info WHERE id == :id")
    suspend fun getFileInfo(id: Int): FileInfoEntity

    @Query("SELECT * FROM file_info")
    suspend fun getAllFileInfo(): List<FileInfoEntity>

    @Query("SELECT last_insert_rowid()")
    suspend fun getLastInsertedFileId(): Int

    @Query("SELECT thumbnail_path FROM file_info WHERE id == :id")
    suspend fun getThumbnailPath(id: Int): String

    @Insert
    suspend fun upsertFile(fileEntity: FileInfoEntity)

    @Query("UPDATE file_info SET name = :name WHERE id == :id")
    suspend fun updateName(name: String, id: Int)

    @Query("DELETE FROM file_info WHERE id == :id")
    suspend fun deleteFile(id: Int)
}