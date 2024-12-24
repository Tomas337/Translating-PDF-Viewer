package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import java.text.Normalizer

fun boyerMooreSunday(text: String, pattern: String): List<Pair<Int, Int>> {
    if (text.isEmpty() ||
        pattern.isEmpty() ||
        pattern.length > text.length
    ) {
        return emptyList()
    }

    val text = Normalizer.normalize(text.lowercase(), Normalizer.Form.NFC)
    val pattern = Normalizer.normalize(pattern.lowercase(), Normalizer.Form.NFC)

    val highlights = mutableListOf<Pair<Int, Int>>()

    val n = text.length
    val m = pattern.length
    val badCharShift = computeBadCharacterRule(pattern)
    var t = 0

    while (t <= n-m) {
        var j = 0

        while (j < m && text[t+j] == pattern[j]) {
            j++
        }

        if (j == m) {
            highlights.add(Pair(t, t+m-1))
            t += m
        } else if (t < n-m) {
            val nextChar = text[t+m]
            t += badCharShift.getOrDefault(nextChar, m+1)
        } else {
            break
        }
    }

    return highlights
}

private fun computeBadCharacterRule(pattern: String): HashMap<Char, Int> {
    val occurrenceMap = hashMapOf<Char, Int>()
    for (i in pattern.indices) {
        occurrenceMap[pattern[i]] = pattern.length - i
    }
    return occurrenceMap
}