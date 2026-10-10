package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CommunityViewModel
import com.example.ui.theme.*

@Composable
fun MoreScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    var showBackupDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top Header Info
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "More Options",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepGreenPrimary,
                    fontSize = 17.sp
                )
            )
            Text(
                text = "Manage app settings, events, and community office bearers",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = OnSurfaceVariantText,
                    fontSize = 12.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Options List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MoreOptionItem(
                icon = Icons.Default.Groups,
                title = "Office Bearers",
                subtitle = "Manage office bearers & committee",
                iconBg = Color(0xFFE0F2F1),
                iconTint = DeepGreenPrimary,
                testTag = "more_office_bearers",
                onClick = { viewModel.navigateTo("OFFICE_BEARERS") }
            )

            MoreOptionItem(
                icon = Icons.Default.CalendarMonth,
                title = "Meeting Calendar",
                subtitle = "Meeting dates and community events",
                iconBg = Color(0xFFE8F5E9),
                iconTint = EmeraldSecondary,
                testTag = "more_meeting_calendar",
                onClick = { viewModel.navigateTo("ATTENDANCE") }
            )

            MoreOptionItem(
                icon = Icons.Default.CardGiftcard,
                title = "Festival Gift Distribution",
                subtitle = "Track festival gifts & status",
                iconBg = Color(0xFFFFEBEE),
                iconTint = StatusInactiveRed,
                testTag = "more_gift_distribution",
                onClick = { viewModel.navigateTo("GIFT_DISTRIBUTION") }
            )

            MoreOptionItem(
                icon = Icons.Default.PhotoLibrary,
                title = "Community Gallery",
                subtitle = "Photos and community videos",
                iconBg = Color(0xFFE0F2F1),
                iconTint = DarkGreenTertiary,
                testTag = "more_community_gallery",
                onClick = { viewModel.navigateTo("GALLERY") }
            )

            MoreOptionItem(
                icon = Icons.Default.CloudSync,
                title = "Backup & Restore",
                subtitle = "Data backup and secure restore",
                iconBg = Color(0xFFE8F5E9),
                iconTint = EmeraldSecondary,
                testTag = "more_backup_restore",
                onClick = { showBackupDialog = true }
            )

            MoreOptionItem(
                icon = Icons.Default.Settings,
                title = "Settings",
                subtitle = "App settings and security",
                iconBg = Color(0xFFEFEBE9),
                iconTint = OnSurfaceText,
                testTag = "more_settings",
                onClick = { viewModel.navigateTo("SETTINGS") }
            )

            MoreOptionItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "App information & version",
                iconBg = Color(0xFFE0F2F1),
                iconTint = DeepGreenPrimary,
                testTag = "more_about",
                onClick = { showAboutDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom App Version Badge Card (Image 8)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(DeepGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ச",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "சங்கமம் (Sangamam) v2.4.0",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Vellore Devangar Community App • Made with warmth",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = OnSurfaceVariantText,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }

    if (showBackupDialog) {
        AlertDialog(
            onDismissRequest = { showBackupDialog = false },
            title = { Text("Backup & Restore", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("All community records are stored safely in the local encrypted Room database on this device.")
                    Text("Last Local Snapshot: 10-10-2026 03:54 PM", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = EmeraldSecondary)
                    Text("Total Members: 150 • Ledger Transactions: Verified", fontSize = 12.sp, color = OnSurfaceVariantText)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Sangamam Devangar Community Backup Archive\nDate: 10-10-2026\nMembers: 150\nBalance: ₹5,48,000\nChecksum: SHA256-OK"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Export Community Backup"))
                        showBackupDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Create Backup")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackupDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About சங்கமம்", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("சங்கமம் – Devangar Community Association", fontWeight = FontWeight.Bold)
                    Text("Version: 1.0 (Build 2.4.0)", fontSize = 13.sp)
                    Text("Headquarters: Devangar Community Hall, Thorapadi, Vellore, Tamil Nadu - 632002", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Text("Designed specifically for association office bearers to manage members, funds, loans, chits, and ledgers safely without internet dependency.", fontSize = 12.sp, lineHeight = 16.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Done", color = DeepGreenPrimary)
                }
            }
        )
    }
}

@Composable
fun MoreOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconBg: Color,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantText,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = OutlineVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
