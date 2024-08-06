package io.github.tomas337.translating_pdf_viewer.data.local.page

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PageDao {

    @Query("SELECT page_path FROM pages WHERE page_index == :pageNumber AND file_id == :fileId")
    suspend fun getPagePath(pageNumber: Int, fileId: Int): String

    @Query("SELECT page_path FROM pages WHERE file_id == :fileId")
    suspend fun getAllPagePaths(fileId: Int): List<String>

    @Insert
    suspend fun insertPage(pageEntity: PageEntity)
}