package com.yuvrajsinghgmx.shopsmart.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
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
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class SupportContact(
    val title: String,
    val description: String,
    val phoneNumber: String,
    val isAvailable: Boolean,
    val tag: String? = null
)

data class SupportHours(
    val day: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallSupportScreen(navController: NavController) {
    val context = LocalContext.current
    val currentTime = remember { LocalTime.now() }
    val currentDay = remember { DayOfWeek.from(LocalDateTime.now()) }

    val supportHours = remember {
        mapOf(
            DayOfWeek.MONDAY to SupportHours(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(20, 0)),
            DayOfWeek.TUESDAY to SupportHours(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(20, 0)),
            DayOfWeek.WEDNESDAY to SupportHours(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(20, 0)),
            DayOfWeek.THURSDAY to SupportHours(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(20, 0)),
            DayOfWeek.FRIDAY to SupportHours(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(20, 0)),
            DayOfWeek.SATURDAY to SupportHours(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(18, 0))
        )
    }

    // Check if support is currently available
    val isWithinSupportHours = remember(currentTime, currentDay) {
        supportHours[currentDay]?.let { hours ->
            currentTime.isAfter(hours.startTime) && currentTime.isBefore(hours.endTime)
        } ?: false
    }

    val supportContacts = remember {
        listOf(
            SupportContact(
                "General Support",
                "For general inquiries and assistance",
                "+1-800-123-4567",
                isAvailable = isWithinSupportHours
            ),
            SupportContact(
                "Technical Support",
                "For app and website related issues",
                "+1-800-123-4568",
                isAvailable = isWithinSupportHours
            ),
            SupportContact(
                "Order Support",
                "For order related queries",
                "+1-800-123-4569",
                isAvailable = isWithinSupportHours
            ),
            SupportContact(
                "Emergency Support",
                "24/7 emergency assistance",
                "+1-800-123-4570",
                isAvailable = true,
                tag = "24/7"
            )
        )
    }

    var showCallDialog by remember { mutableStateOf<SupportContact?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Call Support",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF6F5F3)
                )
            )
        },
        containerColor = Color(0xFFF6F5F3)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SupportStatusCard(isWithinSupportHours)
            }

            items(supportContacts) { contact ->
                ContactCard(
                    contact = contact,
                    onClick = { showCallDialog = contact }
                )
            }

            item {
                SupportHoursCard(supportHours)
            }

            item {
                EmergencyInfoCard()
            }
        }
    }

    // Call Dialog
    showCallDialog?.let { contact ->
        AlertDialog(
            onDismissRequest = { showCallDialog = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
            },
            title = {
                Text(contact.title)
            },
            text = {
                Column {
                    Text("Would you like to call ${contact.phoneNumber}?")
                    if (!contact.isAvailable && contact.tag != "24/7") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Note: Support is currently unavailable. You may experience longer wait times.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${contact.phoneNumber}")
                        }
                        context.startActivity(intent)
                        showCallDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006D40)
                    )
                ) {
                    Text("Call")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCallDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SupportStatusCard(isAvailable: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = if (isAvailable) Color(0xFFE7F5EC) else Color(0xFFFFF1F0)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (isAvailable) R.drawable.support_agent_24px
                    else R.drawable.support_off_24px
                ),
                contentDescription = null,
                tint = if (isAvailable) Color(0xFF006D40) else Color(0xFFB42318)
            )
            Column {
                Text(
                    if (isAvailable) "Support Available" else "Support Unavailable",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isAvailable) Color(0xFF006D40) else Color(0xFFB42318)
                )
                Text(
                    if (isAvailable)
                        "Our support team is ready to assist you"
                    else
                        "Please call back during support hours or leave a message",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isAvailable) Color(0xFF006D40) else Color(0xFFB42318)
                )
            }
        }
    }
}

@Composable
private fun ContactCard(
    contact: SupportContact,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        contact.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    contact.tag?.let {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = Color(0xFF006D40).copy(alpha = 0.1f)
                        ) {
                            Text(
                                it,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF006D40)
                            )
                        }
                    }
                }
                Text(
                    contact.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF637478)
                )
                Text(
                    contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF006D40),
                    fontWeight = FontWeight.Medium
                )
            }
            FilledTonalButton(
                onClick = onClick,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFF006D40).copy(alpha = 0.1f),
                    contentColor = Color(0xFF006D40)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call"
                )
            }
        }
    }
}

@Composable
private fun SupportHoursCard(supportHours: Map<DayOfWeek, SupportHours>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
                Text(
                    "Support Hours",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            supportHours.entries.sortedBy { it.key }.forEach { (day, hours) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        day.name.lowercase()
                            .replaceFirstChar { it.titlecase() },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                    Text(
                        "${hours.startTime.format(DateTimeFormatter.ofPattern("h:mm a"))} - " +
                                "${hours.endTime.format(DateTimeFormatter.ofPattern("h:mm a"))}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                }
            }

            Text(
                "Sunday: Closed",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF637478)
            )
        }
    }
}

@Composable
private fun EmergencyInfoCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFFFF1F0)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.error_24px),
                contentDescription = null,
                tint = Color(0xFFB42318)
            )
            Column {
                Text(
                    "Emergency Support",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB42318)
                )
                Text(
                    "For urgent issues outside of regular hours, " +
                            "please use our 24/7 emergency support line.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB42318)
                )
            }
        }
    }
}