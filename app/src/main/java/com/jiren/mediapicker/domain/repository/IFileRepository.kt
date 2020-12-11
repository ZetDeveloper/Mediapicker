package com.jiren.mediapicker.domain.repository

import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.domain.models.FileModel
/**
 * para para repsositorio de archivos
 */
interface  IFileRepository {
    suspend fun getMedia(type: Int): MutableList<FileModel>
    suspend fun getMusic(): List<FileGenericModel>
    suspend fun getOthers(): List<FileGenericModel>
}