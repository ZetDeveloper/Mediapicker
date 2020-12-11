package com.jiren.mediapicker.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.usescases.FilesOthersUseCaseBase
import com.jiren.mediapicker.usescases.getInstanceUseCaseOthers

class MediaOthersViewmodel(application: Application) : BaseViewModel(application) {
    private val _files = MutableLiveData<List<FileGenericModel>>()
    val lvFiles: LiveData<List<FileGenericModel>>
        get() = _files
    private val useCase: FilesOthersUseCaseBase = getInstanceUseCaseOthers(application)

    /**
     * metodo para hacer un query de los diferentes tipos de archivos apra esta caso de uso
     * @param type filto musica / otros
     */
    fun getMedia(type: Int) {

        launchDataLoad {
            val files = useCase.invoke(type)
            _files.postValue(useCase.invoke(type))
        }
    }
}