package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import android.graphics.Bitmap
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

class BitmapConverter {

    private val gson = Gson()

    @TypeConverter
    fun toString(bitmap: Bitmap): String = gson.toJson(bitmap)

    @TypeConverter
    fun toBitmap(stringifiedBitmap: String): Bitmap {
        val bitmapType = object : TypeToken<Bitmap>() {}.type
        return gson.fromJson(stringifiedBitmap, bitmapType)
    }
}