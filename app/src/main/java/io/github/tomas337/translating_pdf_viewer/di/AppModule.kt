package io.github.tomas337.translating_pdf_viewer.di

import io.github.tomas337.translating_pdf_viewer.data.repository.bookmarks.BookmarkRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository

interface AppModule {
    val fileInfoRepository: FileInfoRepository
    val pageRepository: PageRepository
    val preferencesRepository: PreferencesRepository
    val bookmarkRepository: BookmarkRepository
}