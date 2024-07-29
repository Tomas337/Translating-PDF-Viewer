package io.github.tomas337.translating_pdf_viewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoDao
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.MapConverter
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageConverter
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageDao
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity

@Database(entities = [FileInfoEntity::class, PageEntity::class], version = 1)
@TypeConverters(PageConverter::class, MapConverter::class)
abstract class FileDatabase : RoomDatabase() {

    abstract fun fileInfoDao() : FileInfoDao
    abstract fun pageDao() : PageDao

    companion object {
        @Volatile
        private var Instance: FileDatabase? = null

        fun getFileDatabase(context: Context) : FileDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = FileDatabase::class.java,
                    name = "file_db",
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}