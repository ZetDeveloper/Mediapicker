package com.jiren.mediapicker.usescases

import android.content.Context
import com.jiren.mediapicker.domain.models.FileModel
import com.jiren.mediapicker.data.local.repository.FilesRepository
import com.jiren.mediapicker.domain.repository.IFileRepository

typealias FilesUseCaseBase = BaseUseCase<Int, MutableList<FileModel>>

fun getInstanceUseCase(ctx: Context): FilesMediaUseCase{
    return FilesMediaUseCase(FilesRepository(ctx))
}

class FilesMediaUseCase(private val repository: IFileRepository): FilesUseCaseBase {
    /**
     * define tipo de filtro
     * @param params
     */
    override suspend fun invoke(params: Int): MutableList<FileModel> {
        return repository.getMedia(params)
    }
}

