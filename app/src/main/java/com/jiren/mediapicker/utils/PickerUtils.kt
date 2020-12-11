package com.jiren.mediapicker.utils

import android.webkit.MimeTypeMap
import com.jiren.mediapicker.PickerConstant
import com.jiren.mediapicker.R
import com.jiren.mediapicker.domain.models.FileGeneric
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object PickerUtils {
    private val fileAllTypes: LinkedHashSet<FileGeneric> = LinkedHashSet()
    private val fileTypes: LinkedHashSet<FileGeneric> = LinkedHashSet()
    private val fileTypesOthers: LinkedHashSet<FileGeneric> = LinkedHashSet()
    /**
     * Aqui se pueden agregar facilmente nuevos tipos de extensiones para visualizar
     */
    fun addDocTypes() {
        val pdfs = arrayOf("pdf")
        fileTypes.add(FileGeneric(PickerConstant.PDF, pdfs, R.drawable.file))

        val docs = arrayOf("doc", "docx", "dot", "dotx")
        fileTypes.add(FileGeneric(PickerConstant.DOC, docs, R.drawable.file))

        val ppts = arrayOf("ppt", "pptx")
        fileTypes.add(FileGeneric(PickerConstant.PPT, ppts, R.drawable.file))

        val xlss = arrayOf("xls", "xlsx")
        fileTypes.add(FileGeneric(PickerConstant.XLS, xlss, R.drawable.pie_chart))

        val txts = arrayOf("txt","js","log")
        fileTypes.add(FileGeneric(PickerConstant.TXT, txts, R.drawable.cpu))


    }

    /**
     * Extensiones para musica
     */
    fun addMusicTypes() {
        val mp3s = arrayOf("mp3")
        fileTypesOthers.add(FileGeneric(PickerConstant.MP3, mp3s, R.drawable.musica2))
    }
    fun getMusicFileTypes(): java.util.ArrayList<FileGeneric> {
        fileTypesOthers.clear()
        addMusicTypes()
        return ArrayList(fileTypesOthers)
    }

    fun getAllTypes(): java.util.ArrayList<FileGeneric> {
        fileAllTypes.clear()
        addMusicTypes()
        addDocTypes()
        fileAllTypes.addAll(fileTypesOthers)
        fileAllTypes.addAll(fileTypes)
        return ArrayList(fileAllTypes)
    }

    fun getDocFileTypes(): java.util.ArrayList<FileGeneric> {
        fileTypes.clear()
        addDocTypes()
        return ArrayList(fileTypes)
    }

    fun getFileExtension(file: File): String {
        val name = file.name
        try {
            return name.substring(name.lastIndexOf(".") + 1)
        } catch (e: Exception) {
            return ""
        }

    }

    fun contains(types: Array<String>, mimeType: String?): Boolean {
        for (type in types) {
            if(MimeTypeMap.getSingleton().getMimeTypeFromExtension(type) == mimeType){
                return true
            }
        }
        return false
    }

    /**
     * Metodo para obtener el tiempo de los videos y musica
     * @param item tiempo en milisegundos
     */
    fun formatDurationLongToString(time: String):String {

        val timeInMillisecond = java.lang.Long.parseLong(time)
        if (TimeUnit.MILLISECONDS.toHours(timeInMillisecond) > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMillisecond),
                    TimeUnit.MILLISECONDS.toMinutes(timeInMillisecond) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(timeInMillisecond) % TimeUnit.MINUTES.toSeconds(1))
        }else {
            return String.format(Locale.US, "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(timeInMillisecond) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(timeInMillisecond) % TimeUnit.MINUTES.toSeconds(1))
        }
    }
}