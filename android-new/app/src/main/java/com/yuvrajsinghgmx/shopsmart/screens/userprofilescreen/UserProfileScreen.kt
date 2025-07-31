package com.yuvrajsinghgmx.shopsmart.screens.userprofilescreen

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuvrajsinghgmx.shopsmart.modelclass.User
import com.yuvrajsinghgmx.shopsmart.ui.theme.ShopSmartTypography

@Composable
fun UserProfileScreen(
    user: User
){

    //user type variable for now, will be updated through backend

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Box(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 16.dp),

                ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "User Avatar",
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = user.userName, //can be changed though backend
                        style = ShopSmartTypography.headlineLarge,
                        fontSize = 32.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = user.userPhoneNumber.toString(), //can be changed through backend
                        color = Color.Black,
                        style = ShopSmartTypography.bodyMedium,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { /*reroute to edit profile screen */ },
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .padding(horizontal = 16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Edit Profile",
                            fontSize = 16.sp,
                            style = ShopSmartTypography.headlineLarge
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ){

                //account type section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        "Account Type",
                        color = Color.Black,
                        style = ShopSmartTypography.headlineLarge,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if(user.userType=="Customer"){
                                        Color(0xFF4CAF50)
                                    }else{
                                        Color.Transparent
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Customer",
                                fontSize = 16.sp,
                                style = ShopSmartTypography.bodyMedium,
                                color = if (user.userType=="Customer") Color.White else Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    if(user.userType=="Shopowner"){
                                        Color(0xFF4CAF50)
                                    }else{
                                        Color.Transparent
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Shop Owner",
                                fontSize = 16.sp,
                                style = ShopSmartTypography.bodyMedium,
                                color = if (user.userType=="Shopowner") Color.White else Color.Black
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                //menu items section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    //delivery radius
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable{/*logic to route to the screen*/},
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Delivery Radius",
                                color = Color.Black,
                                style = ShopSmartTypography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "10 km",
                                color = Color.Gray,
                                style = ShopSmartTypography.bodyMedium,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Outlined.KeyboardArrowRight,
                                contentDescription = "Arrow",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    HorizontalDivider(
                        color = Color(0xFFF5F5F5)
                    )

                    //add new product (only for shopowner)
                    if(user.userType=="Shopowner"){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable{/*logic to route to the screen*/},
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Add,
                                    contentDescription = "Location",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "Add New Product",
                                    color = Color.Black,
                                    style = ShopSmartTypography.bodyMedium,
                                    fontSize = 16.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.KeyboardArrowRight,
                                    contentDescription = "Arrow",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        HorizontalDivider(
                            color = Color(0xFFF5F5F5)
                        )
                    }

                    //saved items
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable{/*logic to route to the screen*/},
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Bookmark,
                                contentDescription = "Bookmark",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Saved Items",
                                color = Color.Black,
                                style = ShopSmartTypography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Arrow",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFFF5F5F5)
                    )

                    //my reviews
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable{/*logic to route to the screen*/},
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = "Star",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "My Reviews",
                                color = Color.Black,
                                style = ShopSmartTypography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Arrow",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFFF5F5F5)
                    )

                    //notification settings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable{/*logic to route to the screen*/},
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Notification Settings",
                                color = Color.Black,
                                style = ShopSmartTypography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Arrow",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFFF5F5F5)
                    )

                    //help center
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable{/*logic to route to the screen*/},
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Help,
                                contentDescription = "Help",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Help Center",
                                color = Color.Black,
                                style = ShopSmartTypography.bodyMedium,
                                fontSize = 16.sp
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Arrow",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            //logout button
            Button(
                onClick = { /* handle logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFE53E3E)
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE53E3E))
            ) {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color(0xFFE53E3E),
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