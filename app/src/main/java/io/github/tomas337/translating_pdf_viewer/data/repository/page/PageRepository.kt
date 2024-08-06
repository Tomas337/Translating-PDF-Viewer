package io.github.tomas337.translating_pdf_viewer.data.repository.page

interface PageRepository {
    suspend fun getPagePath(pageIndex: Int, id: Int): String
    suspend fun getAllPagePaths(id: Int): List<String>
    suspend fun insertPage(pageDto: PageDto)
}