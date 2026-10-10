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
import com.example.data.local.entity.CollectionRecordEntity
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyCollectionScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.members.collectAsState()
    val funds by viewModel.funds.collectAsState()

    var selectedFundCode by remember { mutableStateOf("COMMUNITY_FUND") }
    var selectedMonthYear by remember { mutableStateOf("10-2026") }
    var searchQuery by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf("ALL") } // ALL, PAID, PENDING

    val currentFund = funds.find { it.fundCode == selectedFundCode } ?: funds.firstOrNull()
    val isFixed = currentFund?.isFixed ?: true
    val defaultAmount = currentFund?.fixedAmount ?: 100.0

    // Member payment selection state: map of memberId to Pair(isPaid, amount)
    val memberPaymentMap = remember {
        mutableStateMapOf<String, Pair<Boolean, Double>>()
    }

    // Initialize map for 150 members if empty
    LaunchedEffect(members, selectedFundCode) {
        if (memberPaymentMap.isEmpty()) {
            members.forEachIndexed { index, m ->
                val isPaid = (index < 90) // Initially 90 members paid as per screenshot!
                memberPaymentMap[m.memberId] = Pair(isPaid, if (isPaid) defaultAmount else 0.0)
            }
        }
    }

    val paidCount = memberPaymentMap.values.count { it.first }
    val totalPaidAmount = memberPaymentMap.values.filter { it.first }.sumOf { it.second }

    val filteredMembers = members.filter { m ->
        val matchesSearch = m.name.contains(searchQuery, ignoreCase = true) || m.memberId.contains(searchQuery, ignoreCase = true)
        val isPaid = memberPaymentMap[m.memberId]?.first ?: false
        val matchesFilter = when (filterStatus) {
            "PAID" -> isPaid
            "PENDING" -> !isPaid
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
            title = "Monthly Collection",
            subtitle = "Single consolidated collection register",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Fund & Month Selector Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Select Fund Scheme", fontSize = 11.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(4.dp))

                    var expandedFundDropdown by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedFundDropdown,
                        onExpandedChange = { expandedFundDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = currentFund?.title ?: "Monthly Community Contribution",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFundDropdown) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedFundDropdown,
                            onDismissRequest = { expandedFundDropdown = false }
                        ) {
                            funds.forEach { f ->
                                DropdownMenuItem(
                                    text = { Text(f.title) },
                                    onClick = {
                                        selectedFundCode = f.fundCode
                                        expandedFundDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Month / Year", fontSize = 11.sp, color = OnSurfaceVariantText)
                            Text("October 2026", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepGreenPrimary)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceContainerHigh
                        ) {
                            Text(
                                text = if (isFixed) "Rate: ₹${defaultAmount.toInt()} / member" else "Variable Rate",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceVariantText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Summary Totals Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = DeepGreenPrimary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("COLLECTED TOTAL", fontSize = 10.sp, color = LightMintAccent, fontWeight = FontWeight.Bold)
                        Text(
                            text = CommunityRepository.formatRupee(totalPaidAmount),
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.15f)) {
                            Text(
                                text = "$paidCount / ${members.size} Paid",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                members.forEach { m ->
                                    memberPaymentMap[m.memberId] = Pair(true, defaultAmount)
                                }
                            }
                        ) {
                            Text("Mark All", color = LightMintAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search and Status Filters
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
                    selected = filterStatus == "PAID",
                    onClick = { filterStatus = if (filterStatus == "PAID") "ALL" else "PAID" },
                    label = { Text("Paid", fontSize = 11.sp) },
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

            Spacer(modifier = Modifier.height(6.dp))

            // Member Collection Checkbox List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredMembers, key = { it.memberId }) { member ->
                    val paymentState = memberPaymentMap[member.memberId] ?: Pair(false, 0.0)
                    val isChecked = paymentState.first

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        memberPaymentMap[member.memberId] = Pair(checked, if (checked) defaultAmount else 0.0)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = DeepGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(member.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("${member.memberId} • ${member.mobile}", color = OnSurfaceVariantText, fontSize = 11.sp)
                                }
                            }

                            if (isChecked) {
                                Surface(shape = RoundedCornerShape(8.dp), color = StatusActiveBg) {
                                    Text(
                                        text = "₹${paymentState.second.toInt()}",
                                        color = StatusActiveGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Surface(shape = RoundedCornerShape(8.dp), color = StatusInactiveBg) {
                                    Text(
                                        text = "Pending",
                                        color = StatusInactiveRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Save Collection Button
            Button(
                onClick = {
                    val records = members.map { m ->
                        val p = memberPaymentMap[m.memberId] ?: Pair(false, 0.0)
                        CollectionRecordEntity(
                            fundCode = selectedFundCode,
                            memberId = m.memberId,
                            monthYear = selectedMonthYear,
                            amount = p.second,
                            paymentDate = if (p.first) "10-10-2026" else "",
                            isPaid = p.first
                        )
                    }
                    viewModel.saveMonthlyCollection(
                        fundCode = selectedFundCode,
                        fundTitle = currentFund?.title ?: "Monthly Collection",
                        monthYear = selectedMonthYear,
                        records = records
                    )
                    viewModel.navigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_collection_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Collection & Consolidate Ledger", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
