package io.github.tomas337.translating_pdf_viewer.data.local.bookmarks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks WHERE file_id == :fileId ORDER BY page_index ASC")
    fun getAllBookmarks(fileId: Int): Flow<List<BookmarkEntity>>

    @Insert
    suspend fun addBookmark(bookmarkEntity: BookmarkEntity)

    @Query("UPDATE bookmarks SET text = :text WHERE file_id == :fileId AND page_index == :pageIndex")
    suspend fun updateBookmarkText(fileId: Int, pageIndex: Int, text: String)

    @Query("DELETE FROM bookmarks WHERE file_id == :fileId AND page_index == :pageIndex")
    suspend fun deleteBookmark(fileId: Int, pageIndex: Int)
}
