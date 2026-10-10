package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

data class MemberTxRow(
    val title: String,
    val date: String,
    val ref: String,
    val debit: String,
    val credit: String,
    val balance: String
)

@Composable
fun MemberTransactionsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val member by viewModel.selectedMember.collectAsState()
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All Transactions") }

    val transactions = listOf(
        MemberTxRow("Monthly Contribution", "12 Oct 2023", "Ref #TR-8812", "—", "₹100", "₹100"),
        MemberTxRow("Savings Fund", "05 Nov 2023", "Ref #TR-9041", "—", "₹500", "₹600"),
        MemberTxRow("Festival Fund", "18 Nov 2023", "Ref #TR-9320", "—", "₹1,000", "₹1,600"),
        MemberTxRow("Extra Contribution", "01 Dec 2023", "Ref #TR-9512", "—", "₹200", "₹1,800"),
        MemberTxRow("Monthly Contribution", "10 Jan 2024", "Ref #TR-9801", "—", "₹100", "₹1,900")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Member Transactions",
            subtitle = "Ledger & Contribution History",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(
                    onClick = {
                        val shareText = "Devangar Community Statement\nMember: ${member?.name ?: "Suresh M"} (${member?.memberId ?: "M001"})\nTotal Contribution: ₹1,900\nTransactions: 5 verified entries."
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Member Statement"))
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = DeepGreenPrimary)
                }
            }
        )

        // Member Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(DeepGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member?.avatarInitials ?: "SM",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = member?.name ?: "Suresh M",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceText
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = StatusActiveBg
                            ) {
                                Text(
                                    text = "Active",
                                    color = StatusActiveGreen,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Member ID: ${member?.memberId ?: "M001"} • Devangar Community",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantText)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLow
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Net Balance",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariantText,
                                fontSize = 10.sp
                            )
                        )
                        Text(
                            text = "₹1,900",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSecondary
                            )
                        )
                    }
                }
            }
        }

        // Filter Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All Transactions", "Contribution", "Loans").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
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

        // Ruled Ledger Table
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerHigh)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DATE / DETAILS",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = "DEBIT",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        text = "CREDIT",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1.2f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        text = "BALANCE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1.2f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }

                HorizontalDivider(color = LedgerBorder)

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(transactions.size) { index ->
                        val tx = transactions[index]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(
                                        text = tx.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Text(
                                        text = "${tx.date} • ${tx.ref}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = OnSurfaceVariantText,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Text(
                                    text = tx.debit,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = LedgerDebitRed,
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier.weight(1f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Text(
                                    text = tx.credit,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = LedgerCreditGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Text(
                                    text = tx.balance,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepGreenPrimary,
                                        fontSize = 13.sp
                                    ),
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                            }
                        }
                        HorizontalDivider(color = LedgerBorder.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Bottom Summary Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .padding(bottom = 70.dp),
            color = DeepGreenPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = LightMintAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TOTAL CONTRIBUTION SUMMARY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = LightMintAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "₹1,900",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Button(
                    onClick = {
                        val shareText = "Devangar Community Official Receipt\nMember: Suresh M (M001)\nAmount Received: ₹1,900\nStatus: Paid & Verified."
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Receipt"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("View Receipt", color = DeepGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
