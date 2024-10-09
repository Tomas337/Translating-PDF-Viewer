package io.github.tomas337.translating_pdf_viewer.data.local.bookmarks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks WHERE file_id == :fileId")
    suspend fun getAllBookmarks(fileId: Int): List<BookmarkEntity>

    @Insert
    suspend fun addBookmark(bookmarkEntity: BookmarkEntity)

    @Query("UPDATE bookmarks SET text = :text WHERE file_id == :fileId")
    suspend fun updateBookmarkText(fileId: Int, text: String)

    @Query("DELETE FROM bookmarks WHERE file_id == :fileId AND page_index == :pageIndex")
    suspend fun deleteBookmark(fileId: Int, pageIndex: Int)
}