package io.github.tomas337.translating_pdf_viewer.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.tomas337.translating_pdf_viewer.domain.utils.Page
import io.github.tomas337.translating_pdf_viewer.domain.utils.TextStyle

class Converter {

    private val gson = Gson()

    @TypeConverter
    fun pagesToString(pages: List<Page>): String = gson.toJson(pages)

    @TypeConverter
    fun stringToPages(stringifiedPage: String): List<Page> {
        val listType = object : TypeToken<List<Page>>() {}.type
        return gson.fromJson(stringifiedPage, listType)
    }

    @TypeConverter
    fun mapToString(map: HashMap<Int, TextStyle>): String = gson.toJson(map)

    @TypeConverter
    fun stringToMap(stringifiedMap: String): HashMap<Int, TextStyle> {
        val mapType = object : TypeToken<HashMap<Int, TextStyle>>() {}.type
        return gson.fromJson(stringifiedMap, mapType)
    }
}