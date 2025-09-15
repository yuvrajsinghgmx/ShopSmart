package com.yuvrajsinghgmx.shopsmart.sharedComponents

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

//Generic PickerButton
@Composable
fun PickerButton(
    label: String,
    icon: ImageVector = Icons.Default.Add,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(30.dp))
        }
        Text(label, fontSize = 12.sp)
    }
}

//Generic FileItem
@Composable
fun FileItem(
    uri: Uri,
    isImage: Boolean = true,
    onRemove: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (isImage) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null)
                Text(uri.lastPathSegment ?: "Document")
            }
        }
        IconButton(
            onClick = { onRemove(uri) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Remove")
        }
    }
}

//Generic PickerRow/PickerColumn
@Composable
fun FilePickerRow(
    items: List<Uri>,
    isImage: Boolean = true,
    onAdd: () -> Unit,
    onRemove: (Uri) -> Unit
) {
    LazyRow {
        items(items) { uri ->
            FileItem(uri = uri, isImage = isImage, onRemove = onRemove, modifier = Modifier.size(80.dp).padding(4.dp))
        }
        item {
            PickerButton(label = if (isImage) "Add Image" else "Add Document") { onAdd() }
        }
    }
}

@Composable
fun FilePickerColumn(
    items: List<Uri>,
    isImage: Boolean = false,
    onAdd: () -> Unit,
    onRemove: (Uri) -> Unit
) {
    LazyColumn {
        items(items) { uri ->
            FileItem(uri = uri, isImage = isImage, onRemove = onRemove, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
        }
        item {
            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
                Text("Add Document")
            }
        }
    }
}



