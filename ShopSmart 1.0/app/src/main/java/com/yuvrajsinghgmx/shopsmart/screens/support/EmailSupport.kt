package com.yuvrajsinghgmx.shopsmart.screens.support

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import java.util.regex.Pattern

// State classes
class EmailSupportState {
    var subject by mutableStateOf("")
    var messageText by mutableStateOf("")
    var orderId by mutableStateOf("")
    var attachments by mutableStateOf(listOf<EmailAttachment>())
    var showSuccessDialog by mutableStateOf(false)
    var isDropdownExpanded by mutableStateOf(false)
    var hasSubjectError by mutableStateOf(false)
    var hasMessageError by mutableStateOf(false)
    var hasOrderIdError by mutableStateOf(false)
    var hasAttachmentError by mutableStateOf(false)

    companion object {
        const val MAX_ATTACHMENTS = 5
        const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB
        const val MIN_MESSAGE_LENGTH = 20

        val ALLOWED_FILE_TYPES = setOf(
            "pdf", "doc", "docx", "jpg", "jpeg",
            "png", "txt", "csv", "xlsx", "xls"
        )
    }

    fun addAttachment(attachment: EmailAttachment) {
        if (attachments.size < MAX_ATTACHMENTS) {
            attachments = attachments + attachment
            hasAttachmentError = false
        }
    }

    fun removeAttachment(attachment: EmailAttachment) {
        attachments = attachments - attachment
    }

    fun isValid(): Boolean {
        return subject.isNotBlank() &&
                messageText.length >= MIN_MESSAGE_LENGTH &&
                (orderId.isEmpty() || isValidOrderId(orderId)) &&
                !hasAttachmentError
    }

    private fun isValidOrderId(id: String): Boolean {
        return Pattern.compile("^ORD-[A-Za-z0-9]{8}$").matcher(id).matches()
    }
}

// Data classes
data class EmailAttachment(
    val uri: Uri,
    val name: String,
    val size: String,
    val type: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSupportScreen(navController: NavController) {
    val state = remember { EmailSupportState() }
    val context = LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                cursor.moveToFirst()
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)

                val name = cursor.getString(nameIndex)
                val size = cursor.getLong(sizeIndex)
                val type = name.substringAfterLast(".", "")

                val formattedSize = when {
                    size < 1024 -> "$size B"
                    size < 1024 * 1024 -> "${size / 1024} KB"
                    else -> "${size / (1024 * 1024)} MB"
                }

                if (type.lowercase() in EmailSupportState.ALLOWED_FILE_TYPES &&
                    size < EmailSupportState.MAX_FILE_SIZE
                ) {
                    state.addAttachment(EmailAttachment(uri, name, formattedSize, type))
                } else {
                    state.hasAttachmentError = true
                }
            }
        }
    }

    val subjectCategories = listOf(
        "Order & Delivery" to listOf(
            "Track My Order",
            "Delayed Delivery",
            "Wrong Item Delivered",
            "Missing Items",
            "Delivery Address Change"
        ),
        "Returns & Refunds" to listOf(
            "Initiate Return",
            "Refund Status",
            "Return Policy Query",
            "Damaged Item Return",
            "Wrong Size Return"
        ),
        "Product & Inventory" to listOf(
            "Product Availability",
            "Product Specifications",
            "Price Match Request",
            "Product Quality Issue",
            "Product Recommendations"
        ),
        "Account & Payment" to listOf(
            "Payment Issue",
            "Account Access",
            "Update Account Info",
            "Payment Method Update",
            "Security Concerns"
        ),
        "Technical Support" to listOf(
            "App Issues",
            "Website Problems",
            "Login Troubles",
            "Error Messages",
            "Feature Request"
        ),
        "Other Inquiries" to listOf(
            "Feedback",
            "Complaints",
            "Partnership Query",
            "General Question"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F5F3))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF6F5F3),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = "Email Support",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    EmailFormCard(
                        state = state,
                        subjectCategories = subjectCategories,
                        onPickFile = { filePicker.launch("*/*") }
                    )
                }

                item {
                    InfoCard()
                }
            }
        }
    }

    if (state.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                state.showSuccessDialog = false
                navController.navigateUp()
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.check_circle_24px),
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
            },
            title = { Text("Email Sent Successfully") },
            text = {
                Text("We'll get back to you within 24 hours. You'll receive a confirmation email shortly.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.showSuccessDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailFormCard(
    state: EmailSupportState,
    subjectCategories: List<Pair<String, List<String>>>,
    onPickFile: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Subject Dropdown
            ExposedDropdownMenuBox(
                expanded = state.isDropdownExpanded,
                onExpandedChange = { state.isDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = state.subject,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Subject") },
                    placeholder = { Text("Select a subject") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isDropdownExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF006D40),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )

                ExposedDropdownMenu(
                    expanded = state.isDropdownExpanded,
                    onDismissRequest = { state.isDropdownExpanded = false }
                ) {
                    subjectCategories.forEach { (category, options) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    category,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF006D40)
                                )
                            },
                            onClick = { },
                            enabled = false
                        )
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                },
                                onClick = {
                                    state.subject = option
                                    state.isDropdownExpanded = false
                                    state.hasSubjectError = false
                                }
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }

            // Order ID Field
            OutlinedTextField(
                value = state.orderId,
                onValueChange = {
                    state.orderId = it
                    state.hasOrderIdError = it.isNotEmpty() && !it.matches(Regex("^ORD-[A-Za-z0-9]{8}$"))
                },
                label = { Text("Order ID (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.hasOrderIdError,
                supportingText = if (state.hasOrderIdError) {
                    { Text("Format: ORD-XXXXXXXX") }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF006D40),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            // Message Field
            OutlinedTextField(
                value = state.messageText,
                onValueChange = {
                    state.messageText = it
                    state.hasMessageError = it.isNotEmpty() &&
                            it.length < EmailSupportState.MIN_MESSAGE_LENGTH
                },
                label = { Text("Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                isError = state.hasMessageError,
                supportingText = if (state.hasMessageError) {
                    { Text("Minimum ${EmailSupportState.MIN_MESSAGE_LENGTH} characters required") }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF006D40),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            // Attachments Section
            AnimatedVisibility(visible = state.attachments.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Attachments",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    state.attachments.forEach { attachment ->
                        AttachmentItem(
                            attachment = attachment,
                            onRemove = { state.removeAttachment(attachment) }
                        )
                    }
                }
            }

            if (state.hasAttachmentError) {
                Text(
                    "Invalid file type or size exceeds limit (max 10MB)",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Add Attachment Button
            OutlinedButton(
                onClick = onPickFile,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.attachments.size < EmailSupportState.MAX_ATTACHMENTS
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Add Attachment (${state.attachments.size}/" +
                            "${EmailSupportState.MAX_ATTACHMENTS})"
                )
            }

            // Send Button
            Button(
                onClick = { state.showSuccessDialog = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isValid(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D40)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send Email")
            }
        }
    }
}

@Composable
private fun AttachmentItem(
    attachment: EmailAttachment,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF6F5F3),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = when (attachment.type.lowercase()) {
                        "pdf" -> painterResource(id = R.drawable.picture_as_pdf_24px)
                        "jpg", "jpeg", "png" -> painterResource(id = R.drawable.image_24px)
                        "doc", "docx" -> painterResource(id = R.drawable.description_24px)
                        else -> painterResource(id = R.drawable.attach_file_24px)
                    },
                    contentDescription = null,
                    tint = Color(0xFF637478),
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = attachment.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF332D25)
                    )
                    Text(
                        text = attachment.size,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF637478)
                    )
                }
            }
            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color(0xFF637478)
                )
            }
        }
    }
}

@Composable
private fun InfoCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.info_24px),
                contentDescription = null,
                tint = Color(0xFF006D40)
            )
            Column {
                Text(
                    "Response Time",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF006D40)
                )
                Text(
                    "Our support team typically responds within 24 hours during business days.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF006D40)
                )
            }
        }
    }
}