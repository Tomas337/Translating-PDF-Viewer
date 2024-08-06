package io.github.tomas337.translating_pdf_viewer.domain.usecase

import com.google.gson.Gson
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.utils.Page
import java.io.File

class GetPageContentUseCase(
    private val pageRepository: PageRepository
) {
    suspend operator fun invoke(pageIndex: Int, id: Int): List<Any> {
        val pagePath =  pageRepository.getPagePath(pageIndex, id)
        val file = File(pagePath)
        val stringifiedPage = file.readText()
        val page = Gson().fromJson(stringifiedPage, Page::class.java)
        return page.orderedData
    }
}