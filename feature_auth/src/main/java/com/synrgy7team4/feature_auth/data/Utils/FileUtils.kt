package com.synrgy7team4.feature_auth.data.Utils

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun getFile(context: Context, uri: Uri): File {
        val path = uri.path ?: throw IllegalArgumentException("Invalid URI")
        return File(path)
    }
}