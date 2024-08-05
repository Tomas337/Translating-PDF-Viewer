package io.github.tomas337.translating_pdf_viewer.data.repository.page

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageDao
import io.github.tomas337.translating_pdf_viewer.data.local.page.PageEntity
import io.github.tomas337.translating_pdf_viewer.data.mapper.toPageEntity
import io.github.tomas337.translating_pdf_viewer.utils.Page

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {

    override suspend fun getPage(pageNumber: Int, id: Int): String {
        return pageDao.getPage(pageNumber, id)
    }

    override suspend fun getAllPages(id: Int): List<String> {
        return pageDao.getAllPages(id)
    }

    override suspend fun insertPage(pageDto: PageDto) {
        pageDao.insertPage(pageDto.toPageEntity())
    }
}