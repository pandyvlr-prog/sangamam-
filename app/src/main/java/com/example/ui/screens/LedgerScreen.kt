package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ConsolidatedLedgerEntity
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun LedgerScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val ledgerEntries by viewModel.ledgerEntries.collectAsState()
    val context = LocalContext.current
    var showStatementDialog by remember { mutableStateOf(false) }

    val totalDebit = ledgerEntries.sumOf { it.debit }
    val totalCredit = ledgerEntries.sumOf { it.credit }
    val currentBalance = ledgerEntries.firstOrNull()?.balance ?: 548000.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "My Ledger",
            subtitle = "Ruled community accounts book",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = { showStatementDialog = true }) {
                    Icon(Icons.Default.Share, contentDescription = "Share Statement", tint = DeepGreenPrimary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Ruled Ledger Book Table
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Ruled Ledger Columns: Date | Details | Debit | Credit | Balance
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Details",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = "Debit",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1.2f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "Credit",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1.2f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "Balance",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1.4f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                    }

                    HorizontalDivider(color = LedgerBorder)

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(ledgerEntries, key = { it.id }) { entry ->
                            var showTxDetails by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showTxDetails = true }
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = entry.date.take(5), // Short date e.g. "08-10"
                                    fontSize = 11.sp,
                                    color = OnSurfaceVariantText,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = entry.details,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnSurfaceText,
                                    maxLines = 2,
                                    modifier = Modifier.weight(2f)
                                )
                                Text(
                                    text = if (entry.debit > 0) CommunityRepository.formatRupee(entry.debit) else "-",
                                    fontSize = 12.sp,
                                    color = if (entry.debit > 0) LedgerDebitRed else OnSurfaceVariantText,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Text(
                                    text = if (entry.credit > 0) CommunityRepository.formatRupee(entry.credit) else "-",
                                    fontSize = 12.sp,
                                    fontWeight = if (entry.credit > 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (entry.credit > 0) LedgerCreditGreen else OnSurfaceVariantText,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Text(
                                    text = CommunityRepository.formatRupee(entry.balance),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepGreenPrimary,
                                    modifier = Modifier.weight(1.4f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                            }
                            HorizontalDivider(color = SurfaceContainerHigh)

                            if (showTxDetails) {
                                AlertDialog(
                                    onDismissRequest = { showTxDetails = false },
                                    title = { Text("Transaction Details", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text("Date: ${entry.date}")
                                            Text("Details: ${entry.details}", fontWeight = FontWeight.Bold)
                                            if (entry.debit > 0) Text("Debit Outflow: ₹${entry.debit.toInt()}", color = LedgerDebitRed, fontWeight = FontWeight.Bold)
                                            if (entry.credit > 0) Text("Credit Inflow: ₹${entry.credit.toInt()}", color = LedgerCreditGreen, fontWeight = FontWeight.Bold)
                                            Text("Running Balance: ₹${entry.balance.toInt()}", fontWeight = FontWeight.Bold)
                                            Text("Reference: ${entry.referenceType} (${entry.referenceId})", fontSize = 11.sp, color = OnSurfaceVariantText)
                                        }
                                    },
                                    confirmButton = {
                                        TextButton(onClick = { showTxDetails = false }) {
                                            Text("Done")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Totals Bar (Image 11)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Debit: -${CommunityRepository.formatRupee(totalDebit)}",
                            fontWeight = FontWeight.Bold,
                            color = LedgerDebitRed,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Credit: +${CommunityRepository.formatRupee(totalCredit)}",
                            fontWeight = FontWeight.Bold,
                            color = LedgerCreditGreen,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Current Balance Bottom Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = DeepGreenPrimary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Balance",
                        color = LightMintAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = CommunityRepository.formatRupee(currentBalance),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    if (showStatementDialog) {
        var selectedPeriod by remember { mutableStateOf("1 Month") }

        AlertDialog(
            onDismissRequest = { showStatementDialog = false },
            title = { Text("Generate Account Statement", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select Statement Period:", fontSize = 12.sp, color = OnSurfaceVariantText)
                    listOf("1 Month", "3 Months", "6 Months", "1 Year").forEach { p ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { selectedPeriod = p }
                        ) {
                            RadioButton(selected = selectedPeriod == p, onClick = { selectedPeriod = p })
                            Text(p, fontSize = 13.sp)
                        }
                    }
                    Text("Current Closing Balance: ${CommunityRepository.formatRupee(currentBalance)}", fontWeight = FontWeight.Bold, color = DeepGreenPrimary)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareText = "Sangamam Devangar Community Financial Statement ($selectedPeriod)\nTotal Debit: ₹${totalDebit.toInt()}\nTotal Credit: ₹${totalCredit.toInt()}\nClosing Balance: ₹${currentBalance.toInt()}\nAudited by Central Committee."
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Account Statement"))
                        showStatementDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Share Statement")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStatementDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
