package com.yuvrajsinghgmx.shopsmart.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.yuvrajsinghgmx.shopsmart.screens.shops.MultipartWithName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) name = it.getString(index)
        }
    }
    return name ?: "file_${System.currentTimeMillis()}"
}

fun uriToMultipart(context: Context, uri: Uri, partName: String): MultipartWithName? {
    val contentResolver = context.contentResolver
    val mime = contentResolver.getType(uri) ?: return null
    val inputStream = contentResolver.openInputStream(uri) ?: return null

    val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}")
    tempFile.outputStream().use { output -> inputStream.copyTo(output) }

    val fileName = getFileNameFromUri(context, uri)
    val requestFile = tempFile.asRequestBody(mime.toMediaTypeOrNull())
    val part = MultipartBody.Part.createFormData(partName, fileName, requestFile)

    return MultipartWithName(part, fileName)
}

fun String.toRequestBodyJson(): RequestBody =
    toRequestBody("application/json".toMediaTypeOrNull())

fun String.toRequestBodyText(): RequestBody =
    toRequestBody("text/plain".toMediaTypeOrNull())

