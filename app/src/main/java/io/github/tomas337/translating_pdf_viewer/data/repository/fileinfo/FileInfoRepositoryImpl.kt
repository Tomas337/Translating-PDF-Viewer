package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoDao
import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity
import io.github.tomas337.translating_pdf_viewer.data.mapper.toFileInfoDto
import io.github.tomas337.translating_pdf_viewer.data.mapper.toFileInfoDtoList
import io.github.tomas337.translating_pdf_viewer.data.mapper.toFileInfoEntity

class FileInfoRepositoryImpl(
    private val fileInfoDao: FileInfoDao,
) : FileInfoRepository {

    override suspend fun getFileInfo(id: Int): FileInfoDto {
        return fileInfoDao.getFileInfo(id).toFileInfoDto()
    }

    override suspend fun getAllFileInfo(): List<FileInfoDto> {
        return fileInfoDao.getAllFileInfo().toFileInfoDtoList()
    }

    override suspend fun getLastInsertedFileId(): Int {
        return fileInfoDao.getLastInsertedFileId()
    }

    override suspend fun upsertFileInfo(fileInfoDto: FileInfoDto) {
        fileInfoDao.upsertFile(fileInfoDto.toFileInfoEntity())
    }

    override suspend fun deleteFile(id: Int) {
        fileInfoDao.deleteFile(id)
    }
}