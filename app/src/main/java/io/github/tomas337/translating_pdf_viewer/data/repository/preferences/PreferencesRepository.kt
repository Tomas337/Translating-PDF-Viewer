package io.github.tomas337.translating_pdf_viewer.data.repository.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
     suspend fun saveFontSizeScale(fontSizeScale: Float)
     fun readFontSizeScale(): Flow<Float>
     suspend fun savePagePadding(pagePadding: Int)
     fun readPagePadding(): Flow<Int>
     suspend fun saveLineSpacing(lineSpacing: Int)
     fun readLineSpacing(): Flow<Int>
     suspend fun savePageSpacing(pageSpacing: Int)
     fun readPageSpacing(): Flow<Int>
     suspend fun saveParagraphSpacing(paragraphSpacing: Int)
     fun readParagraphSpacing(): Flow<Int>
}