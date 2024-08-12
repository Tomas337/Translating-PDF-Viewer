package io.github.tomas337.translating_pdf_viewer.domain.usecase

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.utils.PageDeserializer
import io.github.tomas337.translating_pdf_viewer.utils.Page
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import java.io.File


class GetPageContentUseCase(
    private val pageRepository: PageRepository
) {
    suspend operator fun invoke(pageIndex: Int, id: Int): List<PageContent> {
        val pagePath =  pageRepository.getPagePath(pageIndex, id)
        val file = File(pagePath)
        val stringifiedPage = file.readText()

        val builder = GsonBuilder()
        builder.registerTypeAdapter(PageContent::class.java, PageDeserializer())
        val gson = builder.create()

        val page = gson.fromJson(stringifiedPage, Page::class.java)
        return page.orderedData
    }
}