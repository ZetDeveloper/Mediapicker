package com.jiren.mediapicker

import com.jiren.mediapicker.domain.models.FileGenericModel
import java.util.*

enum class SortingTypes(val comparator: Comparator<FileGenericModel>?) {
    NAME(NameComparator()), NONE(null);
}

class NameComparator : Comparator<FileGenericModel> {
    override fun compare(o1: FileGenericModel, o2: FileGenericModel): Int {
        return o1.name.toLowerCase(Locale.getDefault()).compareTo(o2.name.toLowerCase(Locale.getDefault()))
    }
}
object PickerConstant {
    const val MEDIA_TYPE_IMAGE = 1
    const val MEDIA_TYPE_VIDEO = 3
    const val MEDIA_TYPE_MUSIC = 8
    const val MEDIA_TYPE_OTHERS = 4
    const val PDF = "PDF"
    const val PPT = "PPT"
    const val DOC = "DOC"
    const val XLS = "XLS"
    const val TXT = "TXT"
    const val MP3 = "MP3"
    var sortingType = SortingTypes.NONE
}