package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.FileContent
import io.github.tomas337.translating_pdf_viewer.data.local.FileDao
import io.github.tomas337.translating_pdf_viewer.data.local.FileEntity
import io.github.tomas337.translating_pdf_viewer.data.local.FileInfo

class FileRepositoryImpl(
    private val fileDao: FileDao
) : FileRepository {

    override suspend fun getFileInfo(id: Int): FileInfo {
        return fileDao.getFileInfo(id)
    }

    override suspend fun getFileContent(id: Int): FileContent {
        return fileDao.getFileContent(id)
    }

    override suspend fun upsertFile(fileEntity: FileEntity) {
        fileDao.upsertFile(fileEntity)
    }

    override suspend fun deleteFile(id: Int) {
        fileDao.deleteFile(id)
    }
}