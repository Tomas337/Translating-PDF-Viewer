package io.github.tomas337.translating_pdf_viewer.data.repository.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class PreferencesRepositoryImpl(context: Context) : PreferencesRepository {

    private companion object {
        // TODO: add word spacing setting
        const val DEFAULT_FONT_SIZE_SCALE = 1.5f
        const val DEFAULT_PAGE_PADDING = 25
        const val DEFAULT_LINE_SPACING = 4
        const val DEFAULT_PAGE_SPACING = 30
        const val DEFAULT_PARAGRAPH_SPACING = 10

        val FONT_SIZE_SCALE_KEY = floatPreferencesKey("font_size_scale")
        val PAGE_PADDING_KEY = intPreferencesKey("page_padding")
        val LINE_SPACING_KEY = intPreferencesKey("line_spacing")
        val PAGE_SPACING_KEY = intPreferencesKey("page_spacing")
        val PARAGRAPH_SPACING_KEY = intPreferencesKey("paragraph_spacing")
    }

    private val dataStore = context.dataStore

    override suspend fun saveFontSizeScale(fontSizeScale: Float) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE_SCALE_KEY] = fontSizeScale
        }
    }

    override fun readFontSizeScale(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[FONT_SIZE_SCALE_KEY] ?: DEFAULT_FONT_SIZE_SCALE
        }
    }

    override suspend fun savePagePadding(pagePadding: Int) {
        dataStore.edit { preferences ->
            preferences[PAGE_PADDING_KEY] = pagePadding
        }
    }

    override fun readPagePadding(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[PAGE_PADDING_KEY] ?: DEFAULT_PAGE_PADDING
        }
    }

    override suspend fun saveLineSpacing(lineSpacing: Int) {
        dataStore.edit { preferences ->
            preferences[LINE_SPACING_KEY] = lineSpacing
        }
    }

    override fun readLineSpacing(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[LINE_SPACING_KEY] ?: DEFAULT_LINE_SPACING
        }
    }

    override suspend fun savePageSpacing(pageSpacing: Int) {
        dataStore.edit { preferences ->
            preferences[PAGE_SPACING_KEY] = pageSpacing
        }
    }

    override fun readPageSpacing(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[PAGE_SPACING_KEY] ?: DEFAULT_PAGE_SPACING
        }
    }

    override suspend fun saveParagraphSpacing(paragraphSpacing: Int) {
        dataStore.edit { preferences ->
            preferences[PARAGRAPH_SPACING_KEY] = paragraphSpacing
        }
    }

    override fun readParagraphSpacing(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[PARAGRAPH_SPACING_KEY] ?: DEFAULT_PARAGRAPH_SPACING
        }
    }

    override suspend fun resetToDefaults() {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE_SCALE_KEY] = DEFAULT_FONT_SIZE_SCALE
            preferences[PAGE_PADDING_KEY] = DEFAULT_PAGE_PADDING
            preferences[LINE_SPACING_KEY] = DEFAULT_LINE_SPACING
            preferences[PAGE_SPACING_KEY] = DEFAULT_PAGE_SPACING
            preferences[PARAGRAPH_SPACING_KEY] = DEFAULT_PARAGRAPH_SPACING
        }
    }
}