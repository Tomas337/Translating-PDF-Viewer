package io.github.tomas337.translating_pdf_viewer.data.local.pages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity

@Dao
interface PageDao {

    @Query("SELECT * FROM pages WHERE page_number == :pageNumber AND file_id == :fileId")
    suspend fun getPage(pageNumber: Int, fileId: Int): FileInfoEntity

    @Insert
    suspend fun upsertPage(pageEntity: PageEntity)
}