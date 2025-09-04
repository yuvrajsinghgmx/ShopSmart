package com.yuvrajsinghgmx.shopsmart.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver

    val file = File(context.cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")

    contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return file
}

fun uriToMultipart(context: Context, uri: Uri, name: String): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val bytes = inputStream.readBytes()
    val requestBody = bytes.toRequestBody("image/*".
    toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, "file.jpg", requestBody)
}