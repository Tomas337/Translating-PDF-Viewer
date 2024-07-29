package io.github.tomas337.translating_pdf_viewer.di

import io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo.FileInfoRepository
import io.github.tomas337.translating_pdf_viewer.data.repository.page.PageRepository

interface AppModule {
    val fileInfoRepository: FileInfoRepository
    val pageRepository: PageRepository
}