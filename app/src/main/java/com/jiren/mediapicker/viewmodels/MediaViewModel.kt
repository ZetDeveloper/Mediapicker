package com.jiren.mediapicker.viewmodels


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jiren.mediapicker.domain.models.FileModel
import com.jiren.mediapicker.usescases.FilesUseCaseBase
import com.jiren.mediapicker.usescases.getInstanceUseCase


class MediaViewModel(application: Application): BaseViewModel(application) {
    private val useCase: FilesUseCaseBase = getInstanceUseCase(application)
    private val _files = MutableLiveData<List<FileModel>>()
    val lvFiles: LiveData<List<FileModel>>
        get() = _files

    /**
     * Metodo para obtener fotos o videos de caso de uso
     * @param type filto imagen o video
     */
    fun getMedia(type: Int) {
        launchDataLoad {
            _files.postValue(useCase.invoke(type))
        }
    }


}