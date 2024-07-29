package io.github.tomas337.translating_pdf_viewer.data.local.page

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.tomas337.translating_pdf_viewer.utils.Page

@Dao
interface PageDao {

    @Query("SELECT page FROM pages WHERE page_number == :pageNumber AND file_id == :fileId")
    suspend fun getPage(pageNumber: Int, fileId: Int): Page

    @Insert
    suspend fun upsertPage(pageEntity: PageEntity)
}