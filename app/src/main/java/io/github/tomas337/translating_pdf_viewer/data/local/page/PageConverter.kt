package io.github.tomas337.translating_pdf_viewer.data.local.page

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.github.tomas337.translating_pdf_viewer.data.utils.Page

class PageConverter {

    private val gson = Gson()

    @TypeConverter
    fun toString(pages: Page): String = gson.toJson(pages)

    @TypeConverter
    fun toPage(stringifiedPage: String): Page = gson.fromJson(stringifiedPage, Page::class.java)
}