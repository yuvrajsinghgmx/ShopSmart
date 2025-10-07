package com.yuvrajsinghgmx.shopsmart.sharedComponents

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun rememberCameraLauncher(
    onImageTaken: (Uri) -> Unit
): () -> Unit {
    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempCameraUri?.let { onImageTaken(it) }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = createImageUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            showPermissionDeniedAlert = true
        }
    }
    if (showPermissionDeniedAlert) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedAlert = false },
            title = { Text("Permission Denied") },
            text = { Text("Camera permission is required to take pictures. Please enable it in the app settings.") },
            confirmButton = {
                Button(onClick = { showPermissionDeniedAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    return {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                val uri = createImageUri(context)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val imageFile = File.createTempFile("camera_photo_", ".jpg", context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}