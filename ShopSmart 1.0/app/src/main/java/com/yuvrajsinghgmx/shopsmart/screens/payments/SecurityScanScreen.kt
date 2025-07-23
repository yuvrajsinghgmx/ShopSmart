package com.yuvrajsinghgmx.shopsmart.screens.payments

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SecurityCheck(
    val id: Int,
    val title: String,
    val description: String,
    var status: CheckStatus = CheckStatus.PENDING,
    val icon: Int,
    var details: String = ""
)

enum class CheckStatus {
    PENDING, IN_PROGRESS, COMPLETED, WARNING, ERROR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScanScreen(navController: NavController) {
    var scanStarted by remember { mutableStateOf(false) }
    var scanCompleted by remember { mutableStateOf(false) }
    var currentCheckIndex by remember { mutableStateOf(-1) }
    val coroutineScope = rememberCoroutineScope()

    val securityChecks = remember {
        mutableStateListOf(
            SecurityCheck(
                1,
                "Device Security",
                "Checking device security settings",
                icon = R.drawable.android_24px
            ),
            SecurityCheck(
                2,
                "Payment Methods",
                "Verifying saved payment methods",
                icon = R.drawable.credit_card_24px
            ),
            SecurityCheck(
                3,
                "Recent Transactions",
                "Analyzing recent transaction patterns",
                icon = R.drawable.history_24px
            ),
            SecurityCheck(
                4,
                "Account Settings",
                "Reviewing account security settings",
                icon = R.drawable.setting
            ),
            SecurityCheck(
                5,
                "Login Activity",
                "Checking recent login attempts",
                icon = R.drawable.brand_sports_24px
            )
        )
    }
    // Delay
    fun startSecurityScan() {
        coroutineScope.launch {
            scanStarted = true
            currentCheckIndex = -1
            scanCompleted = false

            securityChecks.forEach { check ->
                check.status = CheckStatus.PENDING
                check.details = ""
            }

            delay(1000) // Initial delay

            securityChecks.forEachIndexed { index, check ->
                currentCheckIndex = index
                check.status = CheckStatus.IN_PROGRESS
                delay(1500) // Simulate check duration

                // Simulate check outcomes
                check.status = when (index) {
                    1 -> {
                        check.details = "Some payment methods need verification"
                        CheckStatus.WARNING
                    }
                    3 -> {
                        check.details = "Suspicious activity detected"
                        CheckStatus.ERROR
                    }
                    else -> {
                        check.details = "No issues found"
                        CheckStatus.COMPLETED
                    }
                }
            }

            scanCompleted = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TopBar with minimal height
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Security Scan",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }, modifier = Modifier.padding(0.dp)) {
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
            // Shield Icon and Scan Button
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
                            painter = painterResource(id = R.drawable.security_24px),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .rotate(rotation),
                            tint = Color(0xFF006D40)
                        )
                    } else {
                        // Static shield
                        Icon(
                            painter = painterResource(id = R.drawable.security_24px),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = if (scanCompleted) Color(0xFF006D40) else Color(0xFF49454F)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (!scanStarted || scanCompleted) {
                        Button(
                            onClick = { startSecurityScan() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF006D40)
                            ),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (scanCompleted) R.drawable.refresh_24px
                                    else R.drawable.security_24px
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (scanCompleted) "Scan Again" else "Start Security Scan",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            // Security Check Items
            items(securityChecks) { check ->
                SecurityCheckItem(
                    check = check,
                    isCurrentCheck = securityChecks.indexOf(check) == currentCheckIndex
                )
            }
        }
    }
}

@Composable
private fun SecurityCheckItem(
    check: SecurityCheck,
    isCurrentCheck: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Check Icon or Progress Indicator
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

        // Check Content
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