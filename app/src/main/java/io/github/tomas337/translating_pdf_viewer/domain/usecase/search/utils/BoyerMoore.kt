package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import kotlin.math.max

// TODO: unimplemented
fun boyerMoore(text: String, pattern: String): List<Pair<Int, Int>> {
    val highlights = mutableListOf<Pair<Int, Int>>()

    val n = text.length
    val m = pattern.length
    val badCharShift = computeBadCharacterRule(pattern)
    val goodSuffixShift = computeGoodSuffixRule(pattern)
    var t = 0

    while (t <= n-m) {
        var j = m-1

        while (j >= 0 && pattern[j] == text[t+j]) {
            j--
        }

        if (j == -1) {
            highlights.add(Pair(t, t+m-1))
            t += m
        } else {
            val char = text[t+j]
            val lastOccurrence = badCharShift.getOrDefault(char, -1)
            t += max(j - lastOccurrence, goodSuffixShift.getOrDefault(j, 1))
        }
    }

    return highlights
}

private fun computeBadCharacterRule(pattern: String): HashMap<Char, Int> {
    val occurrenceMap = hashMapOf<Char, Int>()
    for (i in pattern.indices) {
        occurrenceMap[pattern[i]] = i
    }
    return occurrenceMap
}

private fun computeGoodSuffixRule(pattern: String): HashMap<Int, Int> {

}