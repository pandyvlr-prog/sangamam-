package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppHeader(
    title: String = "சங்கமம் / Devangar Community",
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        color = WarmCreamBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Sangamam Avatar Badge with Tamil 'ச'
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DeepGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ச",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepGreenPrimary,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "Vellore Mahajan Welfare",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantText,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Notification Bell with Badge
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = StatusInactiveRed,
                            contentColor = Color.White
                        ) {
                            Text("3", fontSize = 10.sp)
                        }
                    },
                    modifier = Modifier.clickable { onNotificationClick() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = DeepGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Profile Avatar Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ForestGreenPrimaryContainer.copy(alpha = 0.15f))
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = DeepGreenPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SubScreenHeader(
    title: String,
    subtitle: String? = null,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = WarmCreamBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DeepGreenPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary,
                        fontSize = 17.sp
                    )
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantText,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                actions()
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(68.dp)
    ) {
        val activeColor = DeepGreenPrimary
        val inactiveColor = OnSurfaceVariantText

        NavigationBarItem(
            selected = currentScreen == "HOME",
            onClick = { onNavigate("HOME") },
            icon = {
                Icon(
                    imageVector = if (currentScreen == "HOME") Icons.Default.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = if (currentScreen == "HOME") FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                indicatorColor = EmeraldLightContainer.copy(alpha = 0.5f),
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == "MEMBERS",
            onClick = { onNavigate("MEMBERS") },
            icon = {
                Icon(
                    imageVector = if (currentScreen == "MEMBERS") Icons.Default.People else Icons.Outlined.People,
                    contentDescription = "Members"
                )
            },
            label = { Text("Members", fontSize = 11.sp, fontWeight = if (currentScreen == "MEMBERS") FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                indicatorColor = EmeraldLightContainer.copy(alpha = 0.5f),
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_members")
        )

        // Center prominent + Add action button
        NavigationBarItem(
            selected = currentScreen == "ADD",
            onClick = { onNavigate("ADD") },
            icon = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(DeepGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            label = { Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = activeColor) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = activeColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Color.White,
                unselectedTextColor = activeColor
            ),
            modifier = Modifier.testTag("nav_add")
        )

        NavigationBarItem(
            selected = currentScreen == "FUNDS",
            onClick = { onNavigate("FUNDS") },
            icon = {
                Icon(
                    imageVector = if (currentScreen == "FUNDS") Icons.Default.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "Funds"
                )
            },
            label = { Text("Funds", fontSize = 11.sp, fontWeight = if (currentScreen == "FUNDS") FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                indicatorColor = EmeraldLightContainer.copy(alpha = 0.5f),
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_funds")
        )

        NavigationBarItem(
            selected = currentScreen == "MORE",
            onClick = { onNavigate("MORE") },
            icon = {
                Icon(
                    imageVector = if (currentScreen == "MORE") Icons.Default.Menu else Icons.Outlined.Menu,
                    contentDescription = "More"
                )
            },
            label = { Text("More", fontSize = 11.sp, fontWeight = if (currentScreen == "MORE") FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                indicatorColor = EmeraldLightContainer.copy(alpha = 0.5f),
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_more")
        )
    }
}
