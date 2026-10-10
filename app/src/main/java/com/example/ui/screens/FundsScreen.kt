package com.example.ui.screens

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
fun FundsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val ledger by viewModel.ledgerEntries.collectAsState()
    val currentBalance = ledger.firstOrNull()?.balance ?: 548000.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top Title
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            Text(
                text = "Funds & Loans",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepGreenPrimary,
                    fontSize = 17.sp
                )
            )
            Text(
                text = "Manage community accounts, collections, and ledgers",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = OnSurfaceVariantText,
                    fontSize = 12.sp
                )
            )
        }

        // Hero Total Community Corpus Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("corpus_summary_card"),
            colors = CardDefaults.cardColors(containerColor = DeepGreenPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Community Corpus",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = LightMintAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CommunityRepository.formatRupee(currentBalance),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Active Collections: 12",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Pending: ₹34,500",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LightMintAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 7 Sections
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FundNavigationCard(
                icon = Icons.Default.AccountBalance,
                title = "Community Fund",
                subtitle = "Main community fund details",
                iconBg = Color(0xFFE0F2F1),
                iconTint = DeepGreenPrimary,
                testTag = "nav_community_fund",
                onClick = { viewModel.navigateTo("COMMUNITY_FUND_DETAILS") }
            )

            FundNavigationCard(
                icon = Icons.Default.Savings,
                title = "Savings Funds",
                subtitle = "All savings schemes",
                iconBg = Color(0xFFE8F5E9),
                iconTint = DarkGreenTertiary,
                testTag = "nav_savings_funds",
                onClick = { viewModel.navigateTo("SAVINGS_FUNDS") }
            )

            FundNavigationCard(
                icon = Icons.Default.CardGiftcard,
                title = "Festival & Tour Funds",
                subtitle = "Festival and tour collections",
                iconBg = Color(0xFFFFEBEE),
                iconTint = StatusInactiveRed,
                testTag = "nav_festival_tour_funds",
                onClick = { viewModel.navigateTo("MONTHLY_COLLECTION") }
            )

            FundNavigationCard(
                icon = Icons.Default.Storefront,
                title = "Business Fund",
                subtitle = "Business support to members",
                iconBg = Color(0xFFE0F2F1),
                iconTint = DeepGreenPrimary,
                testTag = "nav_business_fund",
                onClick = { viewModel.navigateTo("BUSINESS_FUNDS") }
            )

            FundNavigationCard(
                icon = Icons.Default.Payments,
                title = "Member Loans",
                subtitle = "Loans given to members",
                iconBg = Color(0xFFE8F5E9),
                iconTint = EmeraldSecondary,
                testTag = "nav_member_loans",
                onClick = { viewModel.navigateTo("MEMBER_LOANS") }
            )

            FundNavigationCard(
                icon = Icons.Default.PriceCheck,
                title = "Chit Funds",
                subtitle = "Chit scheme management",
                iconBg = Color(0xFFE0F2F1),
                iconTint = DarkGreenTertiary,
                testTag = "nav_chit_funds",
                onClick = { viewModel.navigateTo("CHIT_FUNDS") }
            )

            FundNavigationCard(
                icon = Icons.Default.MenuBook,
                title = "My Ledger",
                subtitle = "All income and expenses",
                iconBg = Color(0xFFEFEBE9),
                iconTint = OnSurfaceText,
                testTag = "nav_my_ledger",
                onClick = { viewModel.navigateTo("LEDGER") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Audited Bottom Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.8f)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = DeepGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "All accounts are fully audited and synchronized with the Devangar community central committee.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DeepGreenPrimary,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun FundNavigationCard(
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
