package com.jiren.mediapicker.usescases

import android.content.Context
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.PickerConstant
import com.jiren.mediapicker.data.local.repository.FilesRepository
import com.jiren.mediapicker.domain.repository.IFileRepository

typealias FilesOthersUseCaseBase = BaseUseCase<Int, List<FileGenericModel>>

fun getInstanceUseCaseOthers(ctx: Context): FilesOthersUseCaseBase{
    return FileOthersMediaUseCase(FilesRepository(ctx))
}

class FileOthersMediaUseCase(private val repository: IFileRepository): FilesOthersUseCaseBase {
    /**
     * Metodo para obtener musica / otros
     * @param params tipo de filtro musica/otros
     */
    override suspend fun invoke(params: Int): List<FileGenericModel> {
        return if (params == PickerConstant.MEDIA_TYPE_MUSIC)  repository.getMusic() else repository.getOthers()
    }
}
