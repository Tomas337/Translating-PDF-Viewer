package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.FileDao
import io.github.tomas337.translating_pdf_viewer.data.local.FileEntity

class FileRepositoryImpl(
    private val fileDao: FileDao
) : FileRepository {

    override suspend fun getFile(id: Int): FileEntity {
        return fileDao.getFile(id)
    }
}