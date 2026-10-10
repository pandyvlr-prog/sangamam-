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
import com.example.data.local.entity.FundEntity
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun SavingsFundsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val allFunds by viewModel.funds.collectAsState()
    val funds = allFunds.filter { it.category == "SAVINGS" }
    var selectedFilter by remember { mutableStateOf("All Funds") }
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Savings Funds",
            subtitle = "Community asset management & schemes",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Hero Savings Corpus Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("savings_corpus_card"),
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
                                text = "TOTAL SAVINGS CORPUS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LightMintAccent,
                                    letterSpacing = 1.sp,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹1,40,000",
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
                            text = "Active Schemes: 3 Schemes",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Total Enrolled: 135 Members",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LightMintAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Create New Savings Fund Button
            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("create_savings_fund_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create New Savings Fund", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All Funds (${funds.size})", "Active", "Fixed Rate").forEach { filter ->
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

            // Savings Fund Cards
            funds.forEach { fund ->
                SavingsFundCardItem(
                    fund = fund,
                    onCollect = { viewModel.navigateTo("MONTHLY_COLLECTION") },
                    onLedger = { viewModel.navigateTo("LEDGER") }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Bottom Notice
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Savings funds are fully audited and synchronized with community records.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = OnSurfaceVariantText,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(12.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }

    if (showCreateDialog) {
        var title by remember { mutableStateOf("") }
        var isFixed by remember { mutableStateOf(true) }
        var fixedAmount by remember { mutableStateOf("500") }
        var connection by remember { mutableStateOf("COMBINED") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New Savings Scheme", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Scheme Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isFixed, onCheckedChange = { isFixed = it })
                        Text("Fixed Monthly Rate Scheme", fontSize = 13.sp)
                    }
                    if (isFixed) {
                        OutlinedTextField(
                            value = fixedAmount,
                            onValueChange = { fixedAmount = it },
                            label = { Text("Fixed Amount (₹ / month)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text("Connection Status:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = connection == "COMBINED", onClick = { connection = "COMBINED" })
                        Text("Combine with Community Fund", fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = connection == "SEPARATE", onClick = { connection = "SEPARATE" })
                        Text("Keep Separate", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Create Scheme")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SavingsFundCardItem(
    fund: FundEntity,
    onCollect: () -> Unit,
    onLedger: () -> Unit
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldLightContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = DeepGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = fund.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceText,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = if (fund.isFixed) "Fixed (₹${fund.fixedAmount.toInt()} / month)" else "Variable (Amount entered per member)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OnSurfaceVariantText,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = StatusActiveBg
                ) {
                    Text(
                        text = "Active",
                        color = StatusActiveGreen,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Collected",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantText)
                        )
                        Text(
                            text = "₹${fund.totalCollected.toInt()}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepGreenPrimary
                            )
                        )
                    }
                    if (fund.isFixed) {
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { (fund.collectionRate / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = DeepGreenPrimary,
                            trackColor = SurfaceContainerHigh,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Collection Rate", fontSize = 10.sp, color = OnSurfaceVariantText)
                            Text("${fund.collectionRate}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepGreenPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Connection Badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceContainerHigh
            ) {
                Text(
                    text = if (fund.connectionType == "COMBINED") "∞ Combine with Community Fund" else "⊘ Keep Separate",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceVariantText,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onLedger,
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("View", fontSize = 11.sp, color = OnSurfaceText)
                }

                OutlinedButton(
                    onClick = onLedger,
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ledger", fontSize = 11.sp, color = OnSurfaceText)
                }

                Button(
                    onClick = onCollect,
                    modifier = Modifier.weight(1.2f).height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldLightContainer)
                ) {
                    Text("Collect", fontSize = 11.sp, color = DeepGreenPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
