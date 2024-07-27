package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.pages.PageDao
import io.github.tomas337.translating_pdf_viewer.data.local.pages.PageEntity

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {

    override suspend fun getPage(pageNumber: Int, id: Int) {
        pageDao.getPage(pageNumber, id)
    }

    override suspend fun upsertPage(pageEntity: PageEntity) {
        pageDao.upsertPage(pageEntity)
    }
}