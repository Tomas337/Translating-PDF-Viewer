package io.github.tomas337.translating_pdf_viewer.data.repository.page

import io.github.tomas337.translating_pdf_viewer.data.local.page.PageDao
import io.github.tomas337.translating_pdf_viewer.data.mapper.toPageEntity

class PageRepositoryImpl(
    private val pageDao: PageDao
) : PageRepository {

    override suspend fun getPagePath(pageIndex: Int, id: Int): String {
        return pageDao.getPagePath(pageIndex, id)
    }

    override suspend fun getAllPagePaths(id: Int): List<String> {
        return pageDao.getAllPagePaths(id)
    }

    override suspend fun insertPage(pageDto: PageDto) {
        pageDao.insertPage(pageDto.toPageEntity())
    }
}