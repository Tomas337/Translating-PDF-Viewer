package io.github.tomas337.translating_pdf_viewer.data.repository.page

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageDao
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity
import io.github.tomas337.translating_pdf_viewer.data.utils.Page

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {

    override suspend fun getPage(pageNumber: Int, id: Int): Page {
        return pageDao.getPage(pageNumber, id)
    }

    override suspend fun upsertPage(pageEntity: PageEntity) {
        pageDao.upsertPage(pageEntity)
    }
}