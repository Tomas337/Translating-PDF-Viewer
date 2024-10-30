package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle
import kotlinx.coroutines.flow.Flow

@Dao
interface FileInfoDao {

    @Query("SELECT * FROM file_info WHERE id == :id")
    suspend fun getFileInfo(id: Int): FileInfoEntity

    @Query("SELECT * FROM file_info")
    fun getAllFileInfo(): Flow<List<FileInfoEntity>>

    @Query("SELECT MAX(id) FROM file_info")
    suspend fun getLastInsertedFileId(): Int

    @Insert
    suspend fun insertFile(fileEntity: FileInfoEntity)

    @Query("UPDATE file_info SET name = :name WHERE id == :id")
    suspend fun updateName(name: String, id: Int)

    @Query("UPDATE file_info SET cur_page = :pageIndex WHERE id == :id")
    suspend fun updateCurrentPage(pageIndex: Int, id: Int)

    @Query("UPDATE file_info SET int_to_textstyle_map = :intToTextStyleMap WHERE id == :id")
    suspend fun updateIntToTextStyleMap(intToTextStyleMap: HashMap<Int, TextStyle>, id: Int)

    @Query("DELETE FROM file_info WHERE id == :id")
    suspend fun deleteFile(id: Int)
}