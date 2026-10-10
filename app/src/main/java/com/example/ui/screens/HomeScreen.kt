package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.members.collectAsState()
    val totalMembers = members.size
    val activeMembers = members.count { it.isActive }

    val ledger by viewModel.ledgerEntries.collectAsState()
    val currentBalance = ledger.firstOrNull()?.balance ?: 548000.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // A. Combined Community Members Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("members_summary_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Total Members
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Total Members",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = OnSurfaceVariantText,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(EmeraldLightContainer.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                tint = DeepGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = totalMembers.toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepGreenPrimary
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(SurfaceContainerHigh)
                )

                // Active Members
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Active Members",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = OnSurfaceVariantText,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(StatusActiveBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusActiveGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeMembers.toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSecondary
                        )
                    )
                }
            }
        }

        // B. Total Community Fund Balance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clickable { viewModel.navigateTo("COMMUNITY_FUND_DETAILS") }
                .testTag("fund_balance_card"),
            colors = CardDefaults.cardColors(containerColor = DeepGreenPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "COMMUNITY FUND BALANCE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = LightMintAccent,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = CommunityRepository.formatRupee(currentBalance),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View Details",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // C. Community Highlights
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Community Highlights",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepGreenPrimary
                )
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldSecondary
                ),
                modifier = Modifier.clickable { viewModel.navigateTo("FUNDS") }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HighlightCard(
                category = "Festival",
                title = "Deepavali 2026",
                bgColor = Color(0xFF6B4226),
                icon = Icons.Default.Festival,
                onClick = { viewModel.navigateTo("GIFT_DISTRIBUTION") }
            )
            HighlightCard(
                category = "Tour",
                title = "Upcoming Yercaud",
                bgColor = Color(0xFF2C5E3B),
                icon = Icons.Default.DirectionsBus,
                onClick = { viewModel.navigateTo("FUNDS") }
            )
            HighlightCard(
                category = "Gallery",
                title = "Photos & Videos",
                bgColor = Color(0xFF37474F),
                icon = Icons.Default.PhotoLibrary,
                onClick = { viewModel.navigateTo("GALLERY") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // D. Attention Required Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("attention_required_card"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F7)),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFCDD2)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(StatusInactiveRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Attention Required",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusInactiveRed
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = StatusInactiveRed
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                AttentionItem(
                    text = "• 8 Members - Collection Pending",
                    actionText = "View",
                    onAction = { viewModel.navigateTo("MONTHLY_COLLECTION") }
                )
                AttentionItem(
                    text = "• 4 Loans - Payment Pending",
                    actionText = "View",
                    onAction = { viewModel.navigateTo("MEMBER_LOANS") }
                )
                AttentionItem(
                    text = "• 2 Members - Gift Pending",
                    actionText = "View",
                    onAction = { viewModel.navigateTo("GIFT_DISTRIBUTION") }
                )
            }
        }

        // E. Quick Actions
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DeepGreenPrimary
            ),
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionItem(
                icon = Icons.Default.PersonAdd,
                label = "Add\nMember",
                bgColor = Color(0xFFE8F5E9),
                iconColor = DeepGreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo("REGISTER_MEMBER") }
            )
            QuickActionItem(
                icon = Icons.Default.CalendarMonth,
                label = "Monthly\nCollection",
                bgColor = Color(0xFFE0F2F1),
                iconColor = EmeraldSecondary,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo("MONTHLY_COLLECTION") }
            )
            QuickActionItem(
                icon = Icons.Default.FactCheck,
                label = "Attendance\nRegister",
                bgColor = Color(0xFFE8F5E9),
                iconColor = DarkGreenTertiary,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo("ATTENDANCE") }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionItem(
                icon = Icons.Default.ReceiptLong,
                label = "Add\nExpense",
                bgColor = Color(0xFFFFEBEE),
                iconColor = StatusInactiveRed,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo("EXPENSES") }
            )
            QuickActionItem(
                icon = Icons.Default.MenuBook,
                label = "Ledger\nBook",
                bgColor = Color(0xFFE8F5E9),
                iconColor = DeepGreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo("LEDGER") }
            )
            QuickActionItem(
                icon = Icons.Default.AccountBalance,
                label = "Funds &\nLoans",
                bgColor = Color(0xFFE0F2F1),
                iconColor = EmeraldSecondary,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo("FUNDS") }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Community Notice Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = DeepGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Community Notice",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepGreenPrimary
                            )
                        )
                    }
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantText)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Monthly general body meeting is scheduled for coming Sunday at 10:00 AM at the community hall. All members are requested to attend.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OnSurfaceVariantText,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
fun HighlightCard(
    category: String,
    title: String,
    bgColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(135.dp)
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun AttentionItem(
    text: String,
    actionText: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = OnSurfaceText,
                fontSize = 13.sp
            )
        )
        Text(
            text = actionText,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = StatusInactiveRed,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            ),
            modifier = Modifier.clickable { onAction() }
        )
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    bgColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(96.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = OnSurfaceText
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }
}
