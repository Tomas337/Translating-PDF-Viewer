package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import android.util.Log
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.system.measureNanoTime

class BoyerMooreTest {

    private val testData = listOf(
        Triple("abcdefg", "cde", listOf(Pair(2, 4))),
        Triple("hello world", "world", listOf(Pair(6, 10))),
        Triple("banana", "apple", emptyList()),
        Triple("mississippi", "issi", listOf(Pair(1, 4))),
        Triple("foobarbaz", "bar", listOf(Pair(3, 5))),
        Triple("abcabcabc", "abc", listOf(Pair(0, 2), Pair(3, 5), Pair(6, 8))),
        Triple("abcdefg", "cde", listOf(Pair(2, 4))),
        Triple("hello world", "world", listOf(Pair(6, 10))),
        Triple("banana", "apple", emptyList()),
        Triple("mississippi", "issi", listOf(Pair(1, 4))),
        Triple("foobarbaz", "bar", listOf(Pair(3, 5))),
        Triple("abcabcabc", "abc", listOf(Pair(0, 2), Pair(3, 5), Pair(6, 8))),
        Triple("abcdefgh", "abcdefgh", listOf(Pair(0, 7))),
        Triple("xyz", "xyz", listOf(Pair(0, 2))),
        Triple("foo", "foo", listOf(Pair(0, 2))),
        Triple("bar", "baz", emptyList()),
        Triple("a", "a", listOf(Pair(0, 0))),
        Triple("", "pattern", emptyList()),
        Triple("pattern", "", emptyList()),
        Triple("abcabcabc", "bcd", emptyList()),
        Triple("aabbaabbaabbaabb", "aabbaabb", listOf(Pair(0, 7), Pair(8, 15))),
        Triple("aabbaabbaabbaabb", "bb", listOf(Pair(2, 3), Pair(6, 7), Pair(10, 11), Pair(14, 15))),
        Triple("aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz", "xxyy", listOf(Pair(46, 49))),
        Triple("aaaabbaaaaaababaaabaaa", "aaa", listOf(Pair(0, 2), Pair(6, 8), Pair(9, 11), Pair(15, 17), Pair(19, 21))),
        Triple("abcdefghijklmnopqrstuvwxyz", "xyz", listOf(Pair(23, 25)))
    )

    @TestFactory
    fun `Assert that boyerMoore function returns the expected result`() =
        testData.map { (text, pattern, expectedResult) ->
            dynamicTest(
                "Locate ${expectedResult.size} occurrence(s) of \"$pattern\" within the supplied text."
            ) {
                var result: List<Pair<Int, Int>>
                val executionTime = measureNanoTime {
                    result = boyerMoore(text, pattern)
                }
                assert(result == expectedResult) {
                    "Unexpected result: expected $expectedResult and got $result."
                }
                // TODO: more descriptive message, eg. test 1, test 2...
                print("boyerMoore execution time: $executionTime ns\n")
            }
        }

    @TestFactory
    fun `Assert that boyerMooreSunday function returns the expected result`() =
        testData.map { (text, pattern, expectedResult) ->
            dynamicTest(
                "Locate ${expectedResult.size} occurrence(s) of \"$pattern\" within the supplied text."
            ) {
                var result: List<Pair<Int, Int>>
                val executionTime = measureNanoTime {
                    result = boyerMooreSunday(text, pattern)
                }
                assert(result == expectedResult) {
                    "Unexpected result: expected $expectedResult and got $result."
                }
                print("boyerMooreSunday execution time: $executionTime ns\n")
            }
        }
}