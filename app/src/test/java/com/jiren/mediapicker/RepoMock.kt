package com.jiren.mediapicker

import android.net.Uri
import com.jiren.mediapicker.domain.models.FileGeneric
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.domain.models.FileModel
import com.jiren.mediapicker.domain.repository.IFileRepository

class RepoMock: IFileRepository {
    override suspend fun getMedia(type: Int): MutableList<FileModel> {
        return mutableListOf(FileModel(type.toLong(),"name",  Uri.parse("anyString"), 0))
    }

    override suspend fun getMusic(): List<FileGenericModel> {
        return listOf(FileGenericModel(0,"name",
                Uri.parse("anyString"),
                null,
                null,
                FileGeneric("mp3", arrayOf("mp3"), R.drawable.musica2)))
    }

    override suspend fun getOthers(): List<FileGenericModel> {
        return listOf(FileGenericModel(0,"name",
                Uri.parse("anyString"),
                null,
                null,
                FileGeneric("pdf", arrayOf("pdf"), R.drawable.file)))
    }

}
