package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoDao
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.local.pages.PageDao

class FileInfoRepositoryImpl(
    private val fileInfoDao: FileInfoDao,
) : FileInfoRepository {

    override suspend fun getFileInfo(id: Int): FileInfoEntity {
        return fileInfoDao.getFileInfo(id)
    }

    override suspend fun upsertFileInfo(fileInfoEntity: FileInfoEntity) {
        fileInfoDao.upsertFile(fileInfoEntity)
    }

    override suspend fun deleteFile(id: Int) {
        fileInfoDao.deleteFile(id)
    }

}