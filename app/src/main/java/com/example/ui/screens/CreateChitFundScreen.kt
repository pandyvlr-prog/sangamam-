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
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun CreateChitFundScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    var chitName by remember { mutableStateOf("Devangar Diamond Chit 2027") }
    var monthlyContribution by remember { mutableStateOf("5000") }
    var durationMonths by remember { mutableStateOf("20") }
    var maxParticipants by remember { mutableStateOf("20") }
    var auctionDay by remember { mutableStateOf("10th of every month") }
    var commencementDate by remember { mutableStateOf("10-04-2027") }
    var auditCheckEnabled by remember { mutableStateOf(true) }
    var remarks by remember { mutableStateOf("") }

    val monthlyVal = monthlyContribution.toDoubleOrNull() ?: 5000.0
    val durationVal = durationMonths.toIntOrNull() ?: 20
    val estimatedPot = monthlyVal * durationVal

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Create New Chit Fund",
            subtitle = "Configure new community chit scheme",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Community Initiative Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepGreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightMintAccent
                        ) {
                            Text(
                                text = "Community Initiative",
                                color = DeepGreenPrimary,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Traditional Devangar Savings",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Secure, transparent, and verified community-backed micro-finance.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }

            // Chit Group Name
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Chit Group Name", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Text("Required", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepGreenPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = chitName,
                    onValueChange = { chitName = it },
                    leadingIcon = { Icon(Icons.Default.Groups, contentDescription = null, tint = DeepGreenPrimary) },
                    modifier = Modifier.fillMaxWidth().testTag("chit_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // Monthly Contribution & Duration
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Monthly Contribution (₹)", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = monthlyContribution,
                        onValueChange = { monthlyContribution = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Duration (Months)", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = durationMonths,
                        onValueChange = { durationMonths = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // Estimated Total Pot (Auto-calculated)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ESTIMATED TOTAL POT / CORPUS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariantText,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹${estimatedPot.toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepGreenPrimary
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightMintAccent
                    ) {
                        Text(
                            text = "Auto-calculated",
                            color = DeepGreenPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Max Participants & Auction Day
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Max Participants", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = maxParticipants,
                        onValueChange = { maxParticipants = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Auction Day", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = auctionDay,
                        onValueChange = { auctionDay = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // First Bidding / Commencement Date
            Column {
                Text("First Bidding / Commencement Date", fontSize = 12.sp, color = OnSurfaceVariantText)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = commencementDate,
                    onValueChange = { commencementDate = it },
                    trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // Community Committee Audit Switch
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Community Committee Audit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OnSurfaceText
                        )
                        Text(
                            text = "Mandatory pre-check as per Devangar Mahajan guidelines.",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantText
                        )
                    }
                    Switch(
                        checked = auditCheckEnabled,
                        onCheckedChange = { auditCheckEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = DeepGreenPrimary
                        )
                    )
                }
            }

            // Remarks / Special Rules
            Column {
                Text("Remarks / Special Rules (Optional)", fontSize = 12.sp, color = OnSurfaceVariantText)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    placeholder = { Text("e.g. Dividend distribution rules, emergency draw clauses, or guarantor requirements...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // Encrypted Notice
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = OnSurfaceVariantText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "All entries are encrypted and verified against local community ledgers.",
                    fontSize = 11.sp,
                    color = OnSurfaceVariantText
                )
            }

            // Submit Button
            Button(
                onClick = {
                    if (chitName.isNotBlank()) {
                        viewModel.createChitFund(
                            chitName,
                            estimatedPot,
                            maxParticipants.toIntOrNull() ?: 20,
                            durationVal,
                            5.0
                        )
                        viewModel.navigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_create_chit_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Create Chit Fund Scheme", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
