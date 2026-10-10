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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun CommunityFundDetailsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val ledger by viewModel.ledgerEntries.collectAsState()
    val currentBalance = ledger.firstOrNull()?.balance ?: 548000.0
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Community Fund Details",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 76.dp)
        ) {
            // Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fund_overview_card"),
                colors = CardDefaults.cardColors(containerColor = DeepGreenPrimary),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL COMMUNITY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LightMintAccent,
                                    letterSpacing = 1.sp,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "FUND BALANCE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LightMintAccent,
                                    letterSpacing = 1.sp,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = CommunityRepository.formatRupee(currentBalance),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = ForestGreenPrimaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = LightMintAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Fully\nAudited",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2x2 Breakdown Metric Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FundMetricSubCard(
                            title = "Contributions",
                            amount = "₹6,20,000",
                            subtitle = "Credit Inflows",
                            icon = Icons.Default.ArrowDownward,
                            modifier = Modifier.weight(1f)
                        )
                        FundMetricSubCard(
                            title = "Connected Funds",
                            amount = "₹1,40,000",
                            subtitle = "Savings & Festivals",
                            icon = Icons.Default.AccountBalance,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FundMetricSubCard(
                            title = "Expenses",
                            amount = "₹2,12,000",
                            subtitle = "Debit Outflows",
                            icon = Icons.Default.ArrowOutward,
                            modifier = Modifier.weight(1f)
                        )
                        FundMetricSubCard(
                            title = "Pending",
                            amount = "₹34,500",
                            subtitle = "To Be Collected",
                            icon = Icons.Default.Schedule,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.navigateTo("MONTHLY_COLLECTION") },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f).height(46.dp)
                ) {
                    Text("Collect Contribution", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                FilledTonalButton(
                    onClick = { viewModel.navigateTo("EXPENSES") },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = SurfaceContainerHigh),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.1f).height(46.dp)
                ) {
                    Text("Record Expense", fontSize = 12.sp, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val statementText = "Sangamam Devangar Community Fund Audit\nBalance: ₹5,48,000\nContributions: ₹6,20,000\nConnected Funds: ₹1,40,000\nExpenses: ₹2,12,000\nPending: ₹34,500"
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, statementText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Community Statement"))
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(0.9f).height(46.dp)
                ) {
                    Text("Statement", fontSize = 11.sp, color = DeepGreenPrimary, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Recent Consolidated Ledger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Consolidated Ledger",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSecondary
                    ),
                    modifier = Modifier.clickable { viewModel.navigateTo("LEDGER") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Recent Ledger Items
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ledger.take(4).forEachIndexed { index, entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entry.details,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = entry.date,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OnSurfaceVariantText,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            if (entry.credit > 0) {
                                Text(
                                    text = "+${CommunityRepository.formatRupee(entry.credit)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = LedgerCreditGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                            } else if (entry.debit > 0) {
                                Text(
                                    text = "-${CommunityRepository.formatRupee(entry.debit)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = LedgerDebitRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }
                        if (index < 3) {
                            HorizontalDivider(color = SurfaceContainerHigh)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Synchronized Audit Notice
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.7f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = DeepGreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "All community fund transactions are verified and synchronized with central committee records.",
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
}

@Composable
fun FundMetricSubCard(
    title: String,
    amount: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LightMintAccent,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LightMintAccent.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            )
        }
    }
}
