package com.jiren.mediapicker

import com.jiren.mediapicker.utils.PickerUtils

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class PickerUtilsUnitTest {
    @Test
    fun  `Prueba de texto de duracion para musica`()  {
        var duration  = PickerUtils.formatDurationLongToString(1000.toString())

        assertEquals("00:01", duration)
    }

    @Test
    fun `Nunca debe de estar vacio un lote de tipos MUSICA`() {
       assertFalse(PickerUtils.getMusicFileTypes().size == 0)
    }

    @Test
    fun `Nunca debe de estar vacio un lote de OTROS`() {
        assertFalse(PickerUtils.getDocFileTypes().size == 0)
    }
}