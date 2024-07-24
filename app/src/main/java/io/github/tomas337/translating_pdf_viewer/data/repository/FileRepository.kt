package io.github.tomas337.translating_pdf_viewer.data.repository

import io.github.tomas337.translating_pdf_viewer.data.local.FileEntity

interface FileRepository {
    suspend fun getFile(id: Int): FileEntity
}