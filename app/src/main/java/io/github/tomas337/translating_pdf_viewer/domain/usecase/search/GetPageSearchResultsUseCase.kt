package io.github.tomas337.translating_pdf_viewer.domain.usecase.search

import com.google.gson.GsonBuilder
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.utils.PageDeserializer
import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils.boyerMooreSunday
import io.github.tomas337.translating_pdf_viewer.utils.Page
import io.github.tomas337.translating_pdf_viewer.utils.PageContent
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import java.io.File

class GetPageSearchResultsUseCase(
    private val pageRepository: PageRepository
) {
    private val _occurrencesFound = MutableStateFlow(0)

    fun getNumberOfOccurrences(): Flow<Int> {
        _occurrencesFound.value = 0
        return _occurrencesFound
    }

    suspend operator fun invoke(
        id: Int,
        pageIndex: Int,
        pattern: String
    ): HashMap<Pair<Int, Int>, List<Pair<Int, Int>>> {

        val pagePath =  pageRepository.getPagePath(pageIndex, id)
        val file = File(pagePath)
        val stringifiedPage = file.readText()
        val builder = GsonBuilder()
        builder.registerTypeAdapter(PageContent::class.java, PageDeserializer())
        val gson = builder.create()
        val page = gson.fromJson(stringifiedPage, Page::class.java)

        val highlights = HashMap<Pair<Int, Int>, List<Pair<Int, Int>>>()

        for (i in 0 until page.orderedData.size) {
            for (j in 0 until page.orderedData[i].size) {
                val pageContent = page.orderedData[i][j]
                if (pageContent is TextBlock) {
                    val text = pageContent.texts.joinToString("")
                    val searchResult = boyerMooreSunday(text, pattern)
                    highlights[Pair(i, j)] = searchResult
                    _occurrencesFound.value += searchResult.size
                }
            }
        }

        return highlights
    }
}