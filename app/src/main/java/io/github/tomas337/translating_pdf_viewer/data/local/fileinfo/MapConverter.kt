package io.github.tomas337.translating_pdf_viewer.data.local.fileinfo

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.tomas337.translating_pdf_viewer.domain.utils.TextStyle

class MapConverter {

    private val gson = Gson()

    @TypeConverter
    fun toString(map: HashMap<Int, TextStyle>): String = gson.toJson(map)

    @TypeConverter
    fun toMap(stringifiedMap: String): HashMap<Int, TextStyle> {
        val mapType = object : TypeToken<HashMap<Int, TextStyle>>() {}.type
        return gson.fromJson(stringifiedMap, mapType)
    }
}