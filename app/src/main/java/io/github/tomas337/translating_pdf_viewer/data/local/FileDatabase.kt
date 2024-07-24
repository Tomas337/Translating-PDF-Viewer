package io.github.tomas337.translating_pdf_viewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FileEntity::class], version = 1)
abstract class FileDatabase : RoomDatabase() {
    abstract fun fileDao() : FileDao

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