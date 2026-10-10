package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChitFundEntity
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun ChitFundsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val chits by viewModel.chitFunds.collectAsState()
    var selectedFilter by remember { mutableStateOf("All Groups") }
    var showAuctionBidDialog by remember { mutableStateOf(false) }
    var selectedChitForBid by remember { mutableStateOf<ChitFundEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Chit Funds",
            subtitle = "Chit scheme management & monthly auctions",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Hero Corpus Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chit_corpus_card"),
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
                                text = "TOTAL CHIT CORPUS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LightMintAccent,
                                    letterSpacing = 1.sp,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹3,00,000",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
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
                                imageVector = Icons.Default.Savings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
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
                            text = "Active Groups: ${chits.size} Groups",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Participants: 60 Members",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LightMintAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Create New Chit Fund Button
            Button(
                onClick = { viewModel.navigateTo("CREATE_CHIT") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("create_new_chit_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create New Chit Fund", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All Groups (${chits.size})", "Active", "Completed").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter.startsWith(filter.take(5)),
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = OnSurfaceVariantText
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chit Cards
            chits.forEach { chit ->
                ChitCardItem(
                    chit = chit,
                    onPlaceBid = {
                        selectedChitForBid = chit
                        showAuctionBidDialog = true
                    },
                    onViewLedger = { viewModel.navigateTo("LEDGER") }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Bottom Regulations Notice
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = DeepGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Chit schemes are fully audited and synchronized with community central committee regulations. For grievance redressal, contact community elders.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantText,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }
    }

    if (showAuctionBidDialog && selectedChitForBid != null) {
        val chit = selectedChitForBid!!
        AuctionConductDialog(
            chit = chit,
            onDismiss = { showAuctionBidDialog = false },
            onConfirm = { winningBid, mId, mName ->
                viewModel.recordChitAuction(chit.id, chit.currentCycleMonth, winningBid, mId, mName)
                showAuctionBidDialog = false
            }
        )
    }
}

@Composable
fun ChitCardItem(
    chit: ChitFundEntity,
    onPlaceBid: () -> Unit,
    onViewLedger: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (chit.status == "Bidding Open") StatusActiveBg else SurfaceContainerHigh
                ) {
                    Text(
                        text = chit.status,
                        color = if (chit.status == "Bidding Open") StatusActiveGreen else OnSurfaceVariantText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Monthly", fontSize = 10.sp, color = OnSurfaceVariantText)
                    Text(
                        text = "₹${chit.monthlyDue.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = chit.chitName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepGreenPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Pot Amount", fontSize = 11.sp, color = OnSurfaceVariantText)
                        Text(
                            text = "₹${chit.chitValue.toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OnSurfaceText
                        )
                    }

                    Column {
                        Text("Cycle", fontSize = 11.sp, color = OnSurfaceVariantText)
                        Text(
                            text = "Month ${chit.currentCycleMonth} / ${chit.durationMonths}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OnSurfaceText
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Status", fontSize = 11.sp, color = OnSurfaceVariantText)
                        Text(
                            text = if (chit.status == "Bidding Open") "Live Bid" else "Confirmed",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (chit.status == "Bidding Open") EmeraldSecondary else OnSurfaceVariantText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val progress = (chit.currentCycleMonth.toFloat() / chit.durationMonths).coerceIn(0f, 1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Duration Progress", fontSize = 11.sp, color = OnSurfaceVariantText)
                Text("${(progress * 100).toInt()}% Completed", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = DeepGreenPrimary,
                trackColor = SurfaceContainerHigh
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewLedger,
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Auctions", fontSize = 11.sp, color = OnSurfaceText)
                }

                OutlinedButton(
                    onClick = onViewLedger,
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ledger", fontSize = 11.sp, color = OnSurfaceText)
                }

                if (chit.status == "Bidding Open") {
                    Button(
                        onClick = onPlaceBid,
                        modifier = Modifier.weight(1.3f).height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Place Bid", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1.3f).height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        enabled = false
                    ) {
                        Text("Bidding Closed", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AuctionConductDialog(
    chit: ChitFundEntity,
    onDismiss: () -> Unit,
    onConfirm: (Double, String, String) -> Unit
) {
    var bidInput by remember { mutableStateOf("15000") }
    var winnerMemberId by remember { mutableStateOf("M005") }
    var winnerMemberName by remember { mutableStateOf("Balaji V") }

    val bidAmount = bidInput.toDoubleOrNull() ?: 15000.0
    val communityCommission = chit.chitValue * (chit.commissionPercent / 100.0)
    val memberDistribution = (bidAmount - communityCommission).coerceAtLeast(0.0)
    val perMemberBenefit = memberDistribution / chit.memberCount
    val finalMonthlyPayable = chit.monthlyDue - perMemberBenefit
    val totalExpectedCollection = finalMonthlyPayable * chit.memberCount
    val winnerPayout = chit.chitValue - bidAmount

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Conduct Auction - Month ${chit.currentCycleMonth}", fontWeight = FontWeight.Bold, color = DeepGreenPrimary)
                Text(chit.chitName, fontSize = 12.sp, color = OnSurfaceVariantText)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = bidInput,
                    onValueChange = { bidInput = it },
                    label = { Text("Winning Bid / Thallu (₹) *") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = winnerMemberId,
                    onValueChange = { winnerMemberId = it },
                    label = { Text("Winner Member ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = winnerMemberName,
                    onValueChange = { winnerMemberName = it },
                    label = { Text("Winner Member Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("AUCTION CALCULATION BREAKDOWN:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepGreenPrimary)
                        Text("• Pot Value: ₹${chit.chitValue.toInt()}", fontSize = 11.sp)
                        Text("• Community Commission (5%): ₹${communityCommission.toInt()}", fontSize = 11.sp)
                        Text("• Member Distribution: ₹${memberDistribution.toInt()}", fontSize = 11.sp)
                        Text("• Per-Member Benefit: ₹${perMemberBenefit.toInt()}", fontSize = 11.sp, color = EmeraldSecondary, fontWeight = FontWeight.Bold)
                        Text("• Final Payable / Member: ₹${finalMonthlyPayable.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("• Winner Payout: ₹${winnerPayout.toInt()}", fontSize = 12.sp, color = DeepGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(bidAmount, winnerMemberId, winnerMemberName) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Text("Confirm Auction")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
