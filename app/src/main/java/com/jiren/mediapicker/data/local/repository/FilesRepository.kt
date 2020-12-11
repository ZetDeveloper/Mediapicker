package com.jiren.mediapicker.data.local.repository

import android.content.Context
import com.jiren.mediapicker.*
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.domain.models.FileModel
import com.jiren.mediapicker.domain.repository.IFileRepository
import com.jiren.mediapicker.utils.PickerUtils
import com.jiren.mediapicker.utils.QueryFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class FilesRepository (app: Context): IFileRepository {
    private var app = app

    /**
     * Metodo para obtener fotos o videos haciendo query
     * @param type filto imagen o video
     */
    override suspend fun getMedia(type: Int): MutableList<FileModel> {
        val medias = mutableListOf<FileModel>()
        withContext(Dispatchers.IO) {

            QueryFiles().queryImages(bucketId = null,
                    mediaType = type ,
                    application = app).map { dir ->
                if (medias.size < 40000){
                    medias.addAll(dir.medias)
                }
            }
        }

        return medias
    }

    /**
     * Metodo para obtener otros archivos del query
     * @param type filto imagen o video
     */
    private suspend fun getFiles(type: Int): List<FileGenericModel> {
        val medias = mutableListOf<FileGenericModel>()

        withContext(Dispatchers.IO) {

            QueryFiles().queryDocs(
                    fileTypes =  if (type == PickerConstant.MEDIA_TYPE_MUSIC) PickerUtils.getMusicFileTypes() else PickerUtils.getDocFileTypes() ,
                    application = app,
                    comparator = PickerConstant.sortingType.comparator,
                    mediaType =type).map { dir ->
                if (medias.size < 80000){
                    medias.addAll(dir.value)
                }
            }

        }

        return medias
    }

    /**
     * mando query de tipo musica
     *
     */
    override suspend fun getMusic(): List<FileGenericModel> {
       return getFiles(PickerConstant.MEDIA_TYPE_MUSIC)
    }

    /**
     * mando query de tipo otros
     *
     */
    override suspend fun getOthers(): List<FileGenericModel> {
        return getFiles(PickerConstant.MEDIA_TYPE_OTHERS)
    }
}