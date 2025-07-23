package com.yuvrajsinghgmx.shopsmart.screens.policies

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.payments.CheckStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PrivacyCheck(
    val id: Int,
    val title: String,
    val description: String,
    var status: CheckStatus = CheckStatus.PENDING,
    val icon: Int,
    var details: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyCheckScreen(navController: NavController) {
    var scanStarted by remember { mutableStateOf(false) }
    var scanCompleted by remember { mutableStateOf(false) }
    var currentCheckIndex by remember { mutableStateOf(-1) }
    val coroutineScope = rememberCoroutineScope()

    val privacyChecks = remember {
        mutableStateListOf(
            PrivacyCheck(
                1,
                "Data Sharing Settings",
                "Checking your data sharing preferences",
                icon = R.drawable.share_24px
            ),
            PrivacyCheck(
                2,
                "App Permissions",
                "Reviewing app access permissions",
                icon = R.drawable.verified_user_24px
            ),
            PrivacyCheck(
                3,
                "Privacy Settings",
                "Analyzing privacy configuration",
                icon = R.drawable.privacy_tip_24px
            ),
            PrivacyCheck(
                4,
                "Account Activity",
                "Monitoring recent account activities",
                icon = R.drawable.manage_accounts_24px
            ),
            PrivacyCheck(
                5,
                "Third-Party Access",
                "Checking connected applications",
                icon = R.drawable.link_24px
            )
        )
    }

    //delay
    fun startPrivacyCheck() {
        coroutineScope.launch {
            scanStarted = true
            currentCheckIndex = -1
            scanCompleted = false

            privacyChecks.forEach { check ->
                check.status = CheckStatus.PENDING
                check.details = ""
            }

            delay(1000) // Initial delay

            privacyChecks.forEachIndexed { index, check ->
                currentCheckIndex = index
                check.status = CheckStatus.IN_PROGRESS
                delay(1500) // Simulate check duration

                // Simulate different privacy check outcomes
                check.status = when (index) {
                    1 -> {
                        check.details = "Location services have unnecessary access"
                        CheckStatus.WARNING
                    }
                    3 -> {
                        check.details = "Unusual login activity detected"
                        CheckStatus.ERROR
                    }
                    else -> {
                        check.details = "Settings are properly configured"
                        CheckStatus.COMPLETED
                    }
                }
            }

            scanCompleted = true
            scanStarted = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TopBar without extra padding
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Privacy Check",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1D1B20)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFFBFBFB)
            ),
            modifier = Modifier.padding(0.dp),
            windowInsets = WindowInsets(0.dp)
        )

        // Main Content
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Privacy Shield Icon and Scan Button
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (scanStarted && !scanCompleted) {
                        // Rotating shield during scan
                        val infiniteTransition = rememberInfiniteTransition(label = "")
                        val rotation by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = ""
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.privacy_tip_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .rotate(rotation),
                            tint = Color(0xFF006D40)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.privacy_tip_24px),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = if (scanCompleted) Color(0xFF006D40) else Color(0xFF49454F)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (!scanStarted || scanCompleted) {
                        Button(
                            onClick = { startPrivacyCheck() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF006D40)
                            ),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (scanCompleted) R.drawable.refresh_24px
                                    else R.drawable.privacy_tip_24px
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (scanCompleted) "Check Again" else "Start Privacy Check",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            // Privacy Check Items
            items(privacyChecks) { check ->
                PrivacyCheckItem(
                    check = check,
                    isCurrentCheck = privacyChecks.indexOf(check) == currentCheckIndex
                )
            }

            // Information Card at the bottom
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = Color(0xFFF0F7FF)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.info_24px),
                            contentDescription = null,
                            tint = Color(0xFF0055D4)
                        )
                        Column {
                            Text(
                                "About Privacy Check",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0055D4)
                            )
                            Text(
                                "Regular privacy checks help protect your personal information. We recommend running a check every month.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF0055D4)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PrivacyCheckItem(
    check: PrivacyCheck,
    isCurrentCheck: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isCurrentCheck && check.status == CheckStatus.IN_PROGRESS) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = Color(0xFF006D40)
            )
        } else {
            Icon(
                painter = painterResource(
                    id = when (check.status) {
                        CheckStatus.COMPLETED -> R.drawable.check_circle_24px
                        CheckStatus.WARNING -> R.drawable.warning_24px
                        CheckStatus.ERROR -> R.drawable.error_24px
                        else -> check.icon
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = when (check.status) {
                    CheckStatus.COMPLETED -> Color(0xFF006D40)
                    CheckStatus.WARNING -> Color(0xFFB25E02)
                    CheckStatus.ERROR -> Color(0xFFB3261E)
                    else -> Color(0xFF49454F)
                }
            )
        }

        Column {
            Text(
                check.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1D1B20)
            )
            Text(
                if (isCurrentCheck && check.status == CheckStatus.IN_PROGRESS)
                    check.description
                else
                    check.details.ifEmpty { check.description },
                style = MaterialTheme.typography.bodyMedium,
                color = when (check.status) {
                    CheckStatus.COMPLETED -> Color(0xFF006D40)
                    CheckStatus.WARNING -> Color(0xFFB25E02)
                    CheckStatus.ERROR -> Color(0xFFB3261E)
                    else -> Color(0xFF49454F)
                }
            )
        }
    }
}