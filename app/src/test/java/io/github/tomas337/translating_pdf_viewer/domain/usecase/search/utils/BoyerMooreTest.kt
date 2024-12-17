package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class BoyerMooreTest {

    // TODO: test more edge cases
    @TestFactory
    fun `Assert that boyerMoore function returns the expected result`() =
        listOf(
            Triple("A Seashell", " Sea", listOf(Pair(2, 4))),
            Triple("Shell Shell", "hell", listOf(Pair(1, 4), Pair(7, 10))),
        ).map { (text, pattern, expectedResult) ->
            dynamicTest(
                "Locate ${expectedResult.size} occurrence(s) of \"$pattern\" within the supplied text."
            ) {
                val result = boyerMoore(text, pattern)
                assert(result == expectedResult)
            }
        }

    @TestFactory
    fun `Assert that boyerMooreSunday function returns the expected result`() =
        listOf(
            Triple("A Seashell", " Sea", listOf(Pair(2, 4))),
            Triple("Shell Shell", "hell", listOf(Pair(1, 4), Pair(7, 10))),
        ).map { (text, pattern, expectedResult) ->
            dynamicTest(
                "Locate ${expectedResult.size} occurrence(s) of \"$pattern\" within the supplied text."
            ) {
                val result = boyerMooreSunday(text, pattern)
                assert(result == expectedResult)
            }
        }
}