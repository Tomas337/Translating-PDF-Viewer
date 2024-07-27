package io.github.tomas337.translating_pdf_viewer.data.local.pages

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.tomas337.translating_pdf_viewer.domain.utils.Page

class PageConverter {

    private val gson = Gson()

    @TypeConverter
    fun toString(pages: Page): String = gson.toJson(pages)

    @TypeConverter
    fun toPage(stringifiedPage: String): Page = gson.fromJson(stringifiedPage, Page::class.java)
}