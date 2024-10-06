package io.github.tomas337.translating_pdf_viewer.data.local.bookmarks

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookmarksDao {

    @Query("SELECT * FROM bookmarks WHERE file_id == :fileId")
    suspend fun getAllBookmarks(fileId: Int): List<BookmarksEntity>
}
