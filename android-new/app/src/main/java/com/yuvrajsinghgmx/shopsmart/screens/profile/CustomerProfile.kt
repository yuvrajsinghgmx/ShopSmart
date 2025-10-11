package com.yuvrajsinghgmx.shopsmart.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.screens.shared.SharedAppViewModel
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTypography

@Composable
fun CustomerProfileScreen(
    viewModel: SharedAppViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.getLogInData()
        viewModel.getOnboardingData()
    }
    val user = viewModel.userState.collectAsState().value
    val onBoarding = viewModel.onBoardingState.collectAsState().value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (onBoarding?.profileImage.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Default Avatar",
                                modifier = Modifier.size(110.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            AsyncImage(
                                model = onBoarding.profileImage,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    onBoarding?.fullName?.let {
                        Text(
                            text = it,
                            style = ShopSmartTypography.headlineLarge,
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    user?.userPhoneNumber?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = ShopSmartTypography.bodyMedium,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
                IconButton(
                    onClick = {
                        navController.navigate("editProfileScreen")
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(50.dp)
                        .padding(end = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Account Type
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {


                Spacer(Modifier.height(16.dp))

                // Menu Items
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    MenuItem(icon = Icons.Outlined.Email, text = onBoarding?.email?:"No email available")
                    if (user?.userType == "Shopowner") {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        MenuItem(icon = Icons.Outlined.Add, text = "Add New Product", onClick = {})
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    MenuItem(icon = Icons.Outlined.LocationOn, text = onBoarding?.currentAddress?:"No address available")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    MenuItem(icon = Icons.Outlined.Star, text = "My Reviews", onClick = {}, true)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    MenuItem(icon = Icons.Outlined.Notifications, text = "Notification Settings", onClick = {}, true)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    MenuItem(icon = Icons.AutoMirrored.Outlined.Help, text = "Customer Support", onClick = {}, true)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    MenuItem(icon = Icons.Outlined.Info, text = "About Us", onClick = {},true)


                }
            }

            Spacer(Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login_route") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    style = ShopSmartTypography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    text: String?,
    onClick: (() -> Unit)? = null,
    trailingArrow:Boolean = false
) {
    val clickModifier = if(onClick != null){
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick)
    }else{
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    }
    Row(
        modifier = clickModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = text ?: "",
                color = MaterialTheme.colorScheme.onSurface,
                style = ShopSmartTypography.bodyMedium,
                fontSize = 16.sp
            )
        }
        if(trailingArrow){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}