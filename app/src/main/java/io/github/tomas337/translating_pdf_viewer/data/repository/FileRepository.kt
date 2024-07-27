package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.FileContent
import io.github.tomas337.translating_pdf_viewer.data.local.FileEntity
import io.github.tomas337.translating_pdf_viewer.data.local.FileInfo

interface FileRepository {
    suspend fun getFileInfo(id: Int): FileInfo
    suspend fun getFileContent(id: Int): FileContent
    suspend fun upsertFile(fileEntity: FileEntity)
    suspend fun deleteFile(id: Int)
}