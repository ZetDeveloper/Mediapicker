package com.jiren.mediapicker.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import com.jiren.mediapicker.PickerConstant
import com.jiren.mediapicker.domain.models.FileGeneric
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.domain.models.MediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.ArrayList
import java.util.Comparator
import java.util.HashMap

/**
 *Clase para filtras archivos usando tecnica de Cursos con un conten resolver
 *
 */
class QueryFiles() {

    /**
     * Metodo para obtener fotos o videos de caso de uso
     * @param fileTypes tipos de archivos a filtrar
     * @param comparator para implementaciones futuras, arquitectura
     * @param application context para el conten resolver
     * @param mediaType tipo de filtro musica/otros
     */
    @WorkerThread
    private fun createDocumentType(fileTypes: List<FileGeneric>,
                                   comparator: Comparator<FileGenericModel>?,
                                   documents: MutableList<FileGenericModel>): HashMap<FileGeneric, List<FileGenericModel>> {
        val documentMap = HashMap<FileGeneric, List<FileGenericModel>>()

        for (fileType in fileTypes) {
            val documentListFilteredByType = documents.filter { document -> PickerUtils.contains(fileType.extensions, document.mimeType) }

            comparator?.let {
                documentListFilteredByType.sortedWith(comparator)
            }

            documentMap[fileType] = documentListFilteredByType
        }

        return documentMap
    }

    /**
     * Metodo para obtener fotos o videos de caso de uso
     * @param fileTypes tipos de archivos a filtrar
     * @param comparator para implementaciones futuras, arquitectura
     * @param application context para el conten resolver
     * @param mediaType tipo de filtro musica/otros
     */
    @WorkerThread
    suspend fun queryDocs(fileTypes: List<FileGeneric>,
                          comparator: Comparator<FileGenericModel>?,
                          application: Context,
                          mediaType: Int): HashMap<FileGeneric, List<FileGenericModel>> {
        var data = HashMap<FileGeneric, List<FileGenericModel>>()
        withContext(Dispatchers.IO) {

            val selection = ("${MediaStore.Files.FileColumns.MEDIA_TYPE}!=${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}" +
                    " AND ${MediaStore.Files.FileColumns.MEDIA_TYPE}!=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}")

            val DOC_PROJECTION = arrayOf(MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.TITLE)

            val cursor = application.contentResolver.query(MediaStore.Files.getContentUri("external"),
                DOC_PROJECTION,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC")

            if (cursor != null) {
                data = createDocumentType(fileTypes, comparator, getDocumentFromCursor(cursor, mediaType))
                cursor.close()
            }
        }
        return data
    }

    private fun getFileType(types: ArrayList<FileGeneric>, path: String): FileGeneric? {
        for (index in types.indices) {
            for (string in types[index].extensions) {
                if (path.endsWith(string)) return types[index]
            }
        }
        return null
    }

    /**
     * Metodo para obtener documentos filtrados por tipo
     * @param data para la busqueda ya viene con el query
     * @param mediaType tipo de filtro imagenes/videos
     */
    @WorkerThread
    private fun getDocumentFromCursor(data: Cursor ,mediaType: Int): MutableList<FileGenericModel> {
        val documents = mutableListOf<FileGenericModel>()
        while (data.moveToNext()) {

            val imageId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID))
            val path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
            val title = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))


            if (path != null) {

                val fileType = getFileType(if (mediaType == PickerConstant.MEDIA_TYPE_MUSIC) PickerUtils.getMusicFileTypes() else PickerUtils.getDocFileTypes(), path)

                val file = File(path)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    imageId
                )

                val a = file.parent
                if (fileType != null && !file.isDirectory && file.exists()) {

                    val document = FileGenericModel(imageId, title, contentUri)
                    document.name += "."+ PickerUtils.getFileExtension(file)
                    document.fileType = fileType

                    val mimeType = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                    if (mimeType != null && !TextUtils.isEmpty(mimeType)) {
                        document.mimeType = mimeType
                    } else {
                        document.mimeType = ""
                    }

                    document.size = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))

                    if (!documents.contains(document)) documents.add(document)
                }
            }
        }

        return documents
    }

    /**
     * Metodo para obtener imagenes o videos
     * @param bucketId para mas filtros
     * @param application context para el conten resolver
     * @param mediaType tipo de filtro imagenes/videos
     */
    @WorkerThread
    suspend fun queryImages(bucketId: String?, mediaType: Int, application: Context): MutableList<MediaModel> {
        var data = mutableListOf<MediaModel>()
        withContext(Dispatchers.IO) {
            val projection = null
            val uri = MediaStore.Files.getContentUri("external")
            val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
            val selectionArgs = mutableListOf<String>()

            var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

            if (mediaType == PickerConstant.MEDIA_TYPE_VIDEO) {
                selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

            } else {
                selection += " AND " + MediaStore.Images.Media.MIME_TYPE + "!='" + MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif") + "'"
            }

            if (bucketId != null)
                selection += " AND " + MediaStore.Images.Media.BUCKET_ID + "='" + bucketId + "'"

            val cursor = application.contentResolver.query(uri, projection, selection, selectionArgs.toTypedArray(), sortOrder)

            if (cursor != null) {
                data = getPhotoDirectories(mediaType, cursor)
                cursor.close()
            }
        }
        return data
    }

    /**
     * Filtrado por whatsapp y otros como lo requiere el Caso 1
     * @param fileType para mas filtros
     * @param application context para el conten resolver
     * @param mediaType tipo de filtro imagenes/videos
     */
    @WorkerThread
    private fun getPhotoDirectories(fileType: Int, data: Cursor): MutableList<MediaModel> {
        val directories = mutableListOf<MediaModel>()

        while (data.moveToNext()) {

            val imageId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID))
            val bucketId = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID))
            val name = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
            val fileName = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
            val mediaType = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))

            val photoDirectory = MediaModel()
            photoDirectory.id = imageId
            photoDirectory.bucketId = bucketId
            photoDirectory.name = name

            var contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageId
            )

            if (!(name.toLowerCase().contains("whatsapp")
                            || name.toLowerCase().contains("otros")
                            || name.toLowerCase().contains("others"))){
                continue
            }


            if (fileType == PickerConstant.MEDIA_TYPE_VIDEO) {
                contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )
            }
            if (!directories.contains(photoDirectory)) {
                photoDirectory.addPhoto(imageId, fileName, contentUri, mediaType)
                photoDirectory.dateAdded = data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                directories.add(photoDirectory)
            } else {
                directories[directories.indexOf(photoDirectory)]
                    .addPhoto(imageId, fileName, contentUri, mediaType)
            }
        }

        return directories
    }

}