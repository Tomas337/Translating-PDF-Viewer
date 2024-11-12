package io.github.tomas337.translating_pdf_viewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkDao
import io.github.tomas337.translating_pdf_viewer.data.local.bookmarks.BookmarkEntity
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoDao
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.MapConverter
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageDao
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity

@Database(
    entities = [
        FileInfoEntity::class,
        PageEntity::class,
        BookmarkEntity::class
    ],
    version = 7
)
@TypeConverters(MapConverter::class)
abstract class TestFileDatabase : RoomDatabase() {

    abstract fun fileInfoDao() : FileInfoDao
    abstract fun pageDao() : PageDao
    abstract fun bookmarksDao() : BookmarkDao

    companion object {
        @Volatile
        private var Instance: FileDatabase? = null

        fun getFileDatabase(context: Context) : FileDatabase {
            return Instance ?: synchronized(this) {
                Room.inMemoryDatabaseBuilder(
                    context = context,
                    klass = FileDatabase::class.java,
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}