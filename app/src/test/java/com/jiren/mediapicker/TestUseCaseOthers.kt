package com.jiren.mediapicker

import com.jiren.mediapicker.usescases.FileOthersMediaUseCase
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class TestUseCaseOthers {
    @Test
    fun  `Prueba de FileUseCase`()  = runBlocking {
        val useCase = FileOthersMediaUseCase(RepoMock())
        Assert.assertEquals(1, useCase.invoke(PickerConstant.MEDIA_TYPE_MUSIC).size)
    }

    @Test
    fun  `Prueba de icono pdf`()  = runBlocking {
        val useCase = FileOthersMediaUseCase(RepoMock())
        var res = useCase.invoke(PickerConstant.MEDIA_TYPE_OTHERS).get(0)
        Assert.assertEquals(res.fileType!!.drawable, R.drawable.file)
    }

    @Test
    fun  `Prueba de icono musica`()  = runBlocking {
        val useCase = FileOthersMediaUseCase(RepoMock())
        var res = useCase.invoke(PickerConstant.MEDIA_TYPE_MUSIC).get(0)
        Assert.assertEquals(res.fileType!!.drawable, R.drawable.musica2)
    }
}