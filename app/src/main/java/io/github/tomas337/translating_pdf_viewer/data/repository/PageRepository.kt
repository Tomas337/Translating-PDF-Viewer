package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.pages.PageEntity

interface PageRepository {
    suspend fun getPage(pageNumber: Int, id: Int)
    suspend fun upsertPage(pageEntity: PageEntity)
}