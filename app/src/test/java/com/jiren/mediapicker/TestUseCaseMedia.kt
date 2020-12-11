package com.jiren.mediapicker

import android.net.Uri
import com.jiren.mediapicker.domain.models.FileGeneric
import com.jiren.mediapicker.domain.models.FileGenericModel
import com.jiren.mediapicker.domain.models.FileModel
import com.jiren.mediapicker.domain.repository.IFileRepository
import com.jiren.mediapicker.usescases.FileOthersMediaUseCase
import com.jiren.mediapicker.usescases.FilesMediaUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class TestUseCaseMedia {
    @Test
    fun  `Prueba de FileUseCase`()  = runBlocking {
        val useCase = FilesMediaUseCase(RepoMock())
        assertEquals(1, useCase.invoke(0).size)
    }

    @Test
    fun  `Prueba de FileUseCase type`()  = runBlocking {
        val useCase = FilesMediaUseCase(RepoMock())
        var res = useCase.invoke(1).get(0)
        assertEquals(0, res.mediaType)
    }
}
