package io.github.tomas337.translating_pdf_viewer.data.repository.fileinfo

interface FileInfoRepository {
    suspend fun getFileInfo(id: Int): FileInfoDto
    suspend fun upsertFileInfo(fileInfoDto: FileInfoDto)
    suspend fun deleteFile(id: Int)
}