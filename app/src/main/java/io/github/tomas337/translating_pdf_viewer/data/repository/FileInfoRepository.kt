package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.fileinfo.FileInfoEntity

interface FileInfoRepository {
    suspend fun getFileInfo(id: Int): FileInfoEntity
    suspend fun upsertFileInfo(fileInfoEntity: FileInfoEntity)
    suspend fun deleteFile(id: Int)
}