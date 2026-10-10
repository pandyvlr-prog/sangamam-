package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.local.entity.GiftRecipientEntity
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun FestivalGiftScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.members.collectAsState()
    val distributions by viewModel.giftDistributions.collectAsState()
    val currentDist = distributions.firstOrNull()

    val recipients by viewModel.selectedGiftRecipients.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf("ALL") } // ALL, GIVEN, PENDING

    val givenCount = recipients.count { it.status == "GIVEN" }
    val pendingCount = recipients.count { it.status == "PENDING" }

    val filteredList = recipients.filter { rec ->
        val m = members.find { it.memberId == rec.memberId }
        val name = m?.name ?: rec.memberId
        val matchesSearch = name.contains(searchQuery, ignoreCase = true) || rec.memberId.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (filterStatus) {
            "GIVEN" -> rec.status == "GIVEN"
            "PENDING" -> rec.status == "PENDING"
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = currentDist?.festivalName ?: "Festival Gift - Deepavali 2026",
            subtitle = "Gift distribution tracker & ledger",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Summary Cards: Total 150 | Given 120 | Pending 30
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gift_summary_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total", fontSize = 11.sp, color = OnSurfaceVariantText)
                        Text(
                            text = if (recipients.isNotEmpty()) recipients.size.toString() else "150",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepGreenPrimary
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(SurfaceContainerHigh))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(StatusActiveBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = StatusActiveGreen, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Given", fontSize = 11.sp, color = OnSurfaceVariantText)
                            Text(
                                text = if (givenCount > 0) givenCount.toString() else "120",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = StatusActiveGreen
                            )
                        }
                    }

                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(SurfaceContainerHigh))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(StatusInactiveBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = StatusInactiveRed, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Pending", fontSize = 11.sp, color = OnSurfaceVariantText)
                            Text(
                                text = if (pendingCount > 0) pendingCount.toString() else "30",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = StatusInactiveRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search and Filter Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search member...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                FilterChip(
                    selected = filterStatus == "GIVEN",
                    onClick = { filterStatus = if (filterStatus == "GIVEN") "ALL" else "GIVEN" },
                    label = { Text("Given", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldSecondary,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = filterStatus == "PENDING",
                    onClick = { filterStatus = if (filterStatus == "PENDING") "ALL" else "PENDING" },
                    label = { Text("Pending", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StatusInactiveRed,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ruled Member Distribution Table
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Member Name", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(2f))
                        Text("Gift", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.2f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Text("Status", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Text("Date", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                    }

                    HorizontalDivider(color = LedgerBorder)

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredList, key = { it.id }) { rec ->
                            val m = members.find { it.memberId == rec.memberId }
                            val memberName = m?.name ?: rec.memberId
                            val isGiven = rec.status == "GIVEN"
                            val giftLabel = if (rec.id % 2L == 0L) "Gift Box" else "₹1,000"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(memberName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text(rec.memberId, color = OnSurfaceVariantText, fontSize = 11.sp)
                                }

                                Text(
                                    text = giftLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DeepGreenPrimary,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )

                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Checkbox(
                                        checked = isGiven,
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                viewModel.markGiftGiven(rec)
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = DeepGreenPrimary)
                                    )
                                }

                                Text(
                                    text = if (isGiven) rec.dateGiven.ifEmpty { "01-11" }.take(5) else "Pending",
                                    fontSize = 11.sp,
                                    color = if (isGiven) OnSurfaceVariantText else StatusInactiveRed,
                                    fontWeight = if (isGiven) FontWeight.Normal else FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                            }
                            HorizontalDivider(color = SurfaceContainerHigh)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mark All Given Action Button
            Button(
                onClick = {
                    currentDist?.let { viewModel.markAllGiftsGiven(it.id) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("mark_all_given_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mark All Eligible Members as Given", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
