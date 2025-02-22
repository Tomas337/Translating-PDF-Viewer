package io.github.tomas337.translating_pdf_viewer.domain.usecase.search.utils

import java.text.Normalizer
import kotlin.math.max

fun boyerMoore(text: String, pattern: String): List<Highlight> {
    if (text.isEmpty() ||
        pattern.isEmpty() ||
        pattern.length > text.length
    ) {
        return emptyList()
    }

    val text = Normalizer.normalize(text.lowercase(), Normalizer.Form.NFC)
    val pattern = Normalizer.normalize(pattern.lowercase(), Normalizer.Form.NFC)

    val highlights = mutableListOf<Highlight>()

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
            highlights.add(Highlight(t, t+m-1))
            t += m
        } else {
            val char = text[t+j]
            val lastOccurrence = badCharShift.getOrDefault(char, -1)
            t += max(j - lastOccurrence, goodSuffixShift[j])
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

private fun computeGoodSuffixRule(pattern: String): IntArray {
    val m = pattern.length
    val borders = IntArray(m+1)
    val shift = IntArray(m)
    var i = m
    var j = m + 1

    borders[i] = j

    while (i > 0) {
        while (j <= m && pattern[i-1] != pattern[j-1]) {
            if (shift[j-1] == 0) {
                shift[j-1] = j-i
            }
            j = borders[j]
        }
        i--
        j--
        borders[i] = j
    }

    var b = borders[0]

    for (i in 0 until m) {
        if (i == b) {
            b = borders[b]
        }
        if (shift[i] == 0) {
            shift[i] = b
        }
    }

    return shift
}