package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.local.entity.MemberEntity
import com.example.ui.CommunityViewModel
import com.example.ui.theme.*

@Composable
fun MembersScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.members.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, ACTIVE, INACTIVE

    val activeCount = members.count { it.isActive }
    val inactiveCount = members.count { !it.isActive }

    val filteredMembers = members.filter { member ->
        val matchesSearch = member.name.contains(searchQuery, ignoreCase = true) ||
                member.memberId.contains(searchQuery, ignoreCase = true) ||
                member.mobile.contains(searchQuery)
        val matchesFilter = when (selectedFilter) {
            "ACTIVE" -> member.isActive
            "INACTIVE" -> !member.isActive
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Box(modifier = modifier.fillMaxSize().background(WarmCreamBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 76.dp)
        ) {
            // Search Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                tonalElevation = 1.dp
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search member name, ID or mobile...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = OutlineBorder
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("member_search_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" },
                    label = { Text("All (${members.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeepGreenPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = OnSurfaceVariantText
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = null
                )

                FilterChip(
                    selected = selectedFilter == "ACTIVE",
                    onClick = { selectedFilter = "ACTIVE" },
                    label = { Text("Active ($activeCount)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeepGreenPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = OnSurfaceVariantText
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = null
                )

                FilterChip(
                    selected = selectedFilter == "INACTIVE",
                    onClick = { selectedFilter = "INACTIVE" },
                    label = { Text("Inactive ($inactiveCount)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
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

            // Member Cards List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredMembers, key = { it.memberId }) { member ->
                    MemberListItemCard(
                        member = member,
                        onClick = { viewModel.openMemberDetails(member.memberId) }
                    )
                }
            }
        }

        // Floating Action Button: + Add Member
        ExtendedFloatingActionButton(
            onClick = { viewModel.navigateTo("REGISTER_MEMBER") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 86.dp)
                .testTag("add_member_fab"),
            containerColor = DeepGreenPrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Member", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
fun MemberListItemCard(
    member: MemberEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("member_card_${member.memberId}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar with initials
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            try {
                                Color(android.graphics.Color.parseColor(member.avatarColorHex))
                            } catch (e: Exception) {
                                ForestGreenPrimaryContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member.avatarInitials.ifEmpty { member.name.take(2).uppercase() },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${member.memberId} • ${member.mobile}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantText,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Active/Inactive Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (member.isActive) StatusActiveBg else StatusInactiveBg,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = if (member.isActive) "Active" else "Inactive",
                        color = if (member.isActive) StatusActiveGreen else StatusInactiveRed,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View Details",
                    tint = OutlineVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("15-05-1990") }
    var weddingDate by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("B+ Positive") }
    var education by remember { mutableStateOf("Graduate") }
    var occupation by remember { mutableStateOf("Business") }
    var address by remember { mutableStateOf("Thorapadi, Vellore") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Community Member", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_name_input")
                )
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("Mobile Number *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_phone_input")
                )
                OutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = { Text("Date of Birth (DD-MM-YYYY)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = occupation,
                    onValueChange = { occupation = it },
                    label = { Text("Occupation") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Residential Address") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && mobile.isNotBlank()) {
                        onConfirm(name, mobile, email, dob, weddingDate, bloodGroup, education, occupation, address)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary),
                modifier = Modifier.testTag("confirm_add_member_button")
            ) {
                Text("Add Member")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = OnSurfaceVariantText)
            }
        }
    )
}
