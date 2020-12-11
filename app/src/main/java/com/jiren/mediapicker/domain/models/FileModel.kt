package com.jiren.mediapicker.domain.models

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

class FileModel @JvmOverloads constructor(override var id: Long = 0,
                                          override var name: String,
                                          override var path: Uri,
                                          var mediaType: Int = 0) : BaseFile(id, name, path)

@Parcelize
class FileGenericModel @JvmOverloads constructor(override var id: Long = 0,
                                         override var name: String,
                                         override var path: Uri,
                                         var mimeType: String? = null,
                                         var size: String? = null,
                                         var fileType: FileGeneric? = null
) : BaseFile(id, name, path)

@Parcelize
class FileGeneric constructor(
    var title: String,
    var extensions : Array<String>,
    @DrawableRes
    var drawable: Int
) : Parcelable