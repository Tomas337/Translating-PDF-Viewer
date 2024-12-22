package io.github.tomas337.translating_pdf_viewer.domain.usecase.search

import io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils.boyerMooreSunday

class GetSearchResultsUseCase {
    operator fun invoke(texts: List<String>, pattern: String): List<Pair<Int, Int>> {
        return boyerMooreSunday(texts.joinToString(""), pattern)
    }
}