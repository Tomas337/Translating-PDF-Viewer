package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import org.junit.jupiter.api.Test

class BoyerMooreTest {

    // TODO: convert to a parametrized test
    @Test
    fun oneOccurrence() {
        val text = "A Seashell"
        val pattern = " Sea"
        val expectedResult: List<Pair<Int, Int>> = listOf(Pair(2, 4))
        val result = boyerMoore(text, pattern)
        assert(result == expectedResult)
    }

    @Test
    fun multipleOccurrences() {
        val text = "Shell Shell"
        val pattern = "hell"
        val expectedResult: List<Pair<Int, Int>> = listOf(Pair(1, 4), Pair(7, 10))
        val result = boyerMoore(text, pattern)
        assert(result == expectedResult)
    }

    // TODO: test more edge cases
}