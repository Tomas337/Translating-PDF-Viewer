package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.system.measureNanoTime

class BoyerMooreTest {

    private val testData = listOf(
        Triple("abcdefg", "cde", listOf(Highlight(2, 4))),
        Triple("hello world", "world", listOf(Highlight(6, 10))),
        Triple("banana", "apple", emptyList()),
        Triple("mississippi", "issi", listOf(Highlight(1, 4))),
        Triple("foobarbaz", "bar", listOf(Highlight(3, 5))),
        Triple("abcabcabc", "abc", listOf(Highlight(0, 2), Highlight(3, 5), Highlight(6, 8))),
        Triple("abcdefg", "cde", listOf(Highlight(2, 4))),
        Triple("hello world", "world", listOf(Highlight(6, 10))),
        Triple("banana", "apple", emptyList()),
        Triple("mississippi", "issi", listOf(Highlight(1, 4))),
        Triple("foobarbaz", "bar", listOf(Highlight(3, 5))),
        Triple("abcabcabc", "abc", listOf(Highlight(0, 2), Highlight(3, 5), Highlight(6, 8))),
        Triple("abcdefgh", "abcdefgh", listOf(Highlight(0, 7))),
        Triple("xyz", "xyz", listOf(Highlight(0, 2))),
        Triple("foo", "foo", listOf(Highlight(0, 2))),
        Triple("bar", "baz", emptyList()),
        Triple("a", "a", listOf(Highlight(0, 0))),
        Triple("", "pattern", emptyList()),
        Triple("pattern", "", emptyList()),
        Triple("abcabcabc", "bcd", emptyList()),
        Triple("aabbaabbaabbaabb", "aabbaabb", listOf(Highlight(0, 7), Highlight(8, 15))),
        Triple("aabbaabbaabbaabb", "bb", listOf(Highlight(2, 3), Highlight(6, 7), Highlight(10, 11), Highlight(14, 15))),
        Triple("aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz", "xxyy", listOf(Highlight(46, 49))),
        Triple("aaaabbaaaaaababaaabaaa", "aaa", listOf(Highlight(0, 2), Highlight(6, 8), Highlight(9, 11), Highlight(15, 17), Highlight(19, 21))),
        Triple("abcdefghijklmnopqrstuvwxyz", "xyz", listOf(Highlight(23, 25))),

        Triple("fooFoOfoOFoofOo", "FoO", listOf(Highlight(0, 2), Highlight(3, 5), Highlight(6, 8), Highlight(9, 11), Highlight(12, 14))),

        // From Anpanman by BTS (방탄소년단)
        // 진 = U+110C + U+1175 + U+11AB
        Triple("내가 가진 건 이 노래 한방", "진", listOf(Highlight(4, 4))),
        // 진 = U+C9C4
        Triple("내가 가진 건 이 노래 한방", "진", listOf(Highlight(4, 4))),
    )

    @TestFactory
    fun `Assert that boyerMoore function returns the expected result`() =
        testData.mapIndexed { i, (text, pattern, expectedResult) ->
            dynamicTest(
                "Locate ${expectedResult.size} occurrence(s) of \"$pattern\" within the supplied text."
            ) {
                var result: List<Highlight>
                val executionTime = measureNanoTime {
                    result = boyerMoore(text, pattern)
                }
                assert(result == expectedResult) {
                    "Unexpected result: expected $expectedResult and got $result."
                }
                print("${i+1}) boyerMoore execution time: $executionTime ns\n")
            }
        }

    @TestFactory
    fun `Assert that boyerMooreSunday function returns the expected result`() =
        testData.mapIndexed { i, (text, pattern, expectedResult) ->
            dynamicTest(
                "Locate ${expectedResult.size} occurrence(s) of \"$pattern\" within the supplied text."
            ) {
                var result: List<Highlight>
                val executionTime = measureNanoTime {
                    result = boyerMooreSunday(text, pattern)
                }
                assert(result == expectedResult) {
                    "Unexpected result: expected $expectedResult and got $result."
                }
                print("${i+1}) boyerMooreSunday execution time: $executionTime ns\n")
            }
        }
}