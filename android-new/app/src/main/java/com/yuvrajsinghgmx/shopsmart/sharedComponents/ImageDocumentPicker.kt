package com.yuvrajsinghgmx.shopsmart.sharedComponents

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

//Generic PickerButton
@Composable
fun PickerButton(
    label: String,
    icon: ImageVector = Icons.Default.Add,
    size: Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.size(size) // make the whole button square
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(size * 0.7f) // 70% of parent size
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
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
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
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
    uris: List<Uri>,
    isImage: Boolean = true,
    onAdd: () -> Unit,
    onRemove: (Uri) -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemSize = screenWidth * 0.2f // 20% of screen width
    val spacing = 5.dp

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemSize),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        items(uris) { uri ->
            //FileItem(uri = uri, isImage = isImage, onRemove = onRemove, modifier = Modifier.size(80.dp).padding(4.dp))
            Box(
                modifier = Modifier
                    .size(itemSize) // square size
            ) {// keep square) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small)
                )
                // Cross/remove button
                IconButton(
                    onClick = { onRemove(uri) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.width(2.dp)) }
        item {
            PickerButton(
                label = if (isImage) "Add Image" else "Add Document",
                size = itemSize
            ) { onAdd() }
        }
    }
}

//@Composable
//fun FilePickerColumn(
//    uris: List<Uri>,
//    isImage: Boolean = false,
//    onAdd: () -> Unit,
//    onRemove: (Uri) -> Unit
//) {
//    LazyColumn {
//        items(uris) { uri ->
//            FileItem(uri = uri, isImage = isImage, onRemove = onRemove, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
//        }
//        item {
//            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
//                Text("Add Document")
//            }
//        }
//    }
//}

@Composable
fun FilePickerColumn(
    uris: List<Uri>,
    isImage: Boolean = false,
    onAdd: () -> Unit,
    onRemove: (Uri) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 1.dp, max = if (uris.isEmpty()) 56.dp else 200.dp)
    ) {
        items(uris) { uri ->
            FileItem(
                uri = uri,
                isImage = isImage,
                onRemove = onRemove,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
        item {
            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
                Text("Add Document")
            }
        }
    }

}




