package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

import io.github.tomas337.translating_pdf_viewer.utils.TextStyle

interface FileInfoRepository {
    suspend fun getFileInfo(id: Int): FileInfoDto
    suspend fun getAllFileInfo(): List<FileInfoDto>
    suspend fun getLastInsertedFileId(): Int
    suspend fun getThumbnailPath(id: Int): String
    suspend fun insertFileInfo(fileInfoDto: FileInfoDto)
    suspend fun updateName(name: String, id: Int)
    suspend fun updateCurrentPage(pageIndex: Int, id: Int)
    suspend fun updateIntToTextStyleMap(intToTextStyleMap: HashMap<Int, TextStyle>, id: Int)
    suspend fun deleteFile(id: Int)
}