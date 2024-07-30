package io.github.tomas337.translating_pdf_viewer.data.repository.page

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity
import io.github.tomas337.translating_pdf_viewer.utils.Page

interface PageRepository {
    suspend fun getPage(pageNumber: Int, id: Int): String
    suspend fun getAllPages(id: Int): List<String>
    suspend fun upsertPage(pageDto: PageDto)
}