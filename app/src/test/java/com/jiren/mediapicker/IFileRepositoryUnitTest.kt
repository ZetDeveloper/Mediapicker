package com.jiren.mediapicker

import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IFileRepositoryUnitTest {
    @Test
    fun  `Prueba de FileUseCase getMedia IMAGEN`()  = runBlocking {
        val repo = RepoMock()
        Assert.assertEquals(1, repo.getMedia(PickerConstant.MEDIA_TYPE_IMAGE).size)
    }

    @Test
    fun  `Prueba de FileUseCase getMedia VIDEO`()  = runBlocking {
        val repo = RepoMock()
        Assert.assertEquals(1, repo.getMedia(PickerConstant.MEDIA_TYPE_VIDEO).size)
    }

    @Test
    fun  `Prueba de FileUseCase getMedia MUSIC`()  = runBlocking {
        val repo = RepoMock()
        Assert.assertEquals(1, repo.getMedia(PickerConstant.MEDIA_TYPE_MUSIC).size)
    }

    @Test
    fun  `Prueba de FileUseCase getMedia OTHERS`()  = runBlocking {
        val repo = RepoMock()
        Assert.assertEquals(1, repo.getMedia(PickerConstant.MEDIA_TYPE_OTHERS).size)
    }
}