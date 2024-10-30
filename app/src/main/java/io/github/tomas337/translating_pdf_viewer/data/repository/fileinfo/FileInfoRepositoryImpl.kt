package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoDao
import io.github.tomas337.translating_pdf_viewer.data.mapper.toFileInfoDto
import io.github.tomas337.translating_pdf_viewer.data.mapper.toFileInfoDtoList
import io.github.tomas337.translating_pdf_viewer.data.mapper.toFileInfoEntity
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FileInfoRepositoryImpl(
    private val fileInfoDao: FileInfoDao,
) : FileInfoRepository {

    override suspend fun getFileInfo(id: Int): FileInfoDto {
        return fileInfoDao.getFileInfo(id).toFileInfoDto()
    }

    override fun getAllFileInfo(): Flow<List<FileInfoDto>> {
        return fileInfoDao.getAllFileInfo().map { it.toFileInfoDtoList() }
    }

    override suspend fun getLastInsertedFileId(): Int {
        return fileInfoDao.getLastInsertedFileId()
    }

    override suspend fun insertFileInfo(fileInfoDto: FileInfoDto) {
        fileInfoDao.insertFile(fileInfoDto.toFileInfoEntity())
    }

    override suspend fun updateName(name: String, id: Int) {
        fileInfoDao.updateName(name, id)
    }

    override suspend fun updateCurrentPage(pageIndex: Int, id: Int) {
        fileInfoDao.updateCurrentPage(pageIndex, id)
    }

    override suspend fun updateIntToTextStyleMap(
        intToTextStyleMap: HashMap<Int, TextStyle>,
        id: Int
    ) {
        fileInfoDao.updateIntToTextStyleMap(intToTextStyleMap, id)
    }

    override suspend fun deleteFile(id: Int) {
        fileInfoDao.deleteFile(id)
    }
}