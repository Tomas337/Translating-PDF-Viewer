package io.github.tomas337.translating_pdf_viewer.data.repository.page

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity
import io.github.tomas337.translating_pdf_viewer.utils.Page

interface PageRepository {
    suspend fun getPage(pageNumber: Int, id: Int): Page
    suspend fun upsertPage(pageDto: PageDto)
}