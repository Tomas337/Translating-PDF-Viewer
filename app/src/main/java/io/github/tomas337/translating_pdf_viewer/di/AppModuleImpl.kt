package io.github.tomas337.translating_pdf_viewer.di

import android.content.Context
import io.github.tomas337.translating_pdf_viewer.data.local.FileDatabase
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepositoryImpl
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepositoryImpl
import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.preferences.PreferencesRepositoryImpl

class AppModuleImpl(
    private val appContext: Context
) : AppModule {

    private val db = FileDatabase.getFileDatabase(appContext)
    private val fileInfoDao = db.fileInfoDao()
    private val pageDao = db.pageDao()

    override val fileInfoRepository: FileInfoRepository by lazy {
        FileInfoRepositoryImpl(fileInfoDao)
    }

    override val pageRepository: PageRepository by lazy {
        PageRepositoryImpl(pageDao)
    }

    override val preferencesRepository: PreferencesRepository by lazy {
        PreferencesRepositoryImpl(appContext)
    }
}