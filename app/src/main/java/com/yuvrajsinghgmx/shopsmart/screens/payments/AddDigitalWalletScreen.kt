package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class WalletOption(
    val name: String,
    val icon: Int,
    val description: String,
    val features: List<String>,
    val supportedCountries: List<String>,
    val route: String,
    val isPopular: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDigitalWalletScreen(navController: NavController) {
    val walletOptions = listOf(
        WalletOption(
            name = "Google Pay",
            icon = R.drawable.google_pay_large,
            description = "Fast, simple way to pay in millions of places",
            features = listOf(
                "Contactless payments",
                "Online shopping",
                "Peer-to-peer transfers",
                "Loyalty cards",
                "Transit passes"
            ),
            supportedCountries = listOf("USA", "UK", "India", "Canada", "Australia"),
            route = "google_pay_setup",
            isPopular = true
        ),
        WalletOption(
            name = "PayPal",
            icon = R.drawable.paypal_large,
            description = "Secure payments and money transfers worldwide",
            features = listOf(
                "Online payments",
                "International transfers",
                "Buyer protection",
                "Merchant services",
                "Crypto capabilities"
            ),
            supportedCountries = listOf("Global coverage in 200+ countries"),
            route = "paypal_settings",
            isPopular = true
        ),
        WalletOption(
            name = "Apple Pay",
            icon = R.drawable.apple_pay_large,
            description = "Easy, secure payments with Apple devices",
            features = listOf(
                "Face ID/Touch ID payments",
                "In-app purchases",
                "Web payments",
                "Express transit",
                "Digital keys"
            ),
            supportedCountries = listOf("USA", "UK", "Canada", "Australia", "UAE"),
            route = "apple_pay_setup"
        )
    )

    var selectedWallet by remember { mutableStateOf<WalletOption?>(null) }
    var showDetails by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add Digital Wallet",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24px),
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Info Card
            item {
                InfoCard()
            }

            // Popular Wallets Section
            item {
                Text(
                    "Popular Digital Wallets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(walletOptions.filter { it.isPopular }) { wallet ->
                WalletCard(
                    wallet = wallet,
                    onClick = {
                        selectedWallet = wallet
                        showDetails = true
                    }
                )
            }

            // All Available Wallets Section
            item {
                Text(
                    "All Available Wallets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            items(walletOptions.filterNot { it.isPopular }) { wallet ->
                WalletCard(
                    wallet = wallet,
                    onClick = {
                        selectedWallet = wallet
                        showDetails = true
                    }
                )
            }

            // Security Note
            item {
                SecurityNoteCard()
            }
        }
    }

    // Wallet Details Bottom Sheet
    if (showDetails && selectedWallet != null) {
        WalletDetailsSheet(
            wallet = selectedWallet!!,
            onDismiss = { showDetails = false },
            onSetup = { route ->
                showDetails = false
                navController.navigate(route)
            }
        )
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.info_24px),
                contentDescription = null,
                tint = Color(0xFF006D40)
            )
            Column {
                Text(
                    "Choose Your Digital Wallet",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF006D40)
                )
                Text(
                    "Select from our supported digital payment services. You can add multiple wallets for convenience.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF006D40)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletCard(wallet: WalletOption, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = wallet.icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = wallet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = wallet.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF637478)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_24px),
                contentDescription = "View details",
                tint = Color(0xFF637478)
            )
        }
    }
}

@Composable
private fun SecurityNoteCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFFFF4ED)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = null,
                tint = Color(0xFFB25E02)
            )
            Column {
                Text(
                    "Secure Integration",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB25E02)
                )
                Text(
                    "All digital wallet connections are protected with industry-standard encryption and security measures.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB25E02)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletDetailsSheet(
    wallet: WalletOption,
    onDismiss: () -> Unit,
    onSetup: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF6F5F3)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = wallet.icon),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
                Column {
                    Text(
                        wallet.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        wallet.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF637478)
                    )
                }
            }

            HorizontalDivider()

            // Features
            Text(
                "Features",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            wallet.features.forEach { feature ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription = null,
                        tint = Color(0xFF006D40),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(feature, style = MaterialTheme.typography.bodyMedium)
                }
            }

            HorizontalDivider()

            // Supported Countries
            Text(
                "Availability",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            wallet.supportedCountries.forEach { country ->
                Text(
                    "• $country",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Setup Button
            Button(
                onClick = { onSetup(wallet.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006D40)
                )
            ) {
                Text("Set up ${wallet.name}")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}