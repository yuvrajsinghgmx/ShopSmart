package com.yuvrajsinghgmx.shopsmart.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.screens.onboarding.components.UserTypeCard

enum class UserType {
    CUSTOMER,
    SHOP_OWNER,
    NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypeSelectionScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedUserType by remember { mutableStateOf(UserType.NONE) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Welcome to ShopSmart",
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Continue Button
            if (selectedUserType != UserType.NONE) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = {
                            // Navigate to profile details screen
                            navController.navigate("onboarding_profile/${selectedUserType.name}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Continue",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Welcome Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Choose Your Account Type",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Select the option that best describes you to get started with ShopSmart",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User Type Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UserTypeCard(
                    title = "Customer",
                    description = "Browse and shop from various stores",
                    icon = Icons.Default.Person,
                    isSelected = selectedUserType == UserType.CUSTOMER,
                    onClick = { selectedUserType = UserType.CUSTOMER }
                )
                
                UserTypeCard(
                    title = "Shop Owner",
                    description = "Manage your store and sell products",
                    icon = Icons.Default.Store,
                    isSelected = selectedUserType == UserType.SHOP_OWNER,
                    onClick = { selectedUserType = UserType.SHOP_OWNER }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
