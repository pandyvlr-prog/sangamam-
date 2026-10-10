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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CommunityViewModel
import com.example.ui.theme.*

@Composable
fun CreateAddScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showBusinessFundDialog by remember { mutableStateOf(false) }
    var showLoanDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 84.dp)
    ) {
        // Top Hub Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "QUICK ACTIONS HUB",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepGreenPrimary,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "புதியது உருவாக்கு / Create & Add",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText,
                            fontSize = 15.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(EmeraldLightContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = DeepGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 9 Action Items List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionHubRow(
                icon = Icons.Default.PersonAdd,
                title = "Add Member",
                subtitle = "Add new community member",
                iconBg = Color(0xFFE8F5E9),
                iconTint = DeepGreenPrimary,
                testTag = "action_add_member",
                onClick = { viewModel.navigateTo("REGISTER_MEMBER") }
            )

            ActionHubRow(
                icon = Icons.Default.ReceiptLong,
                title = "Add Expense",
                subtitle = "Record community expense",
                iconBg = Color(0xFFFFEBEE),
                iconTint = StatusInactiveRed,
                testTag = "action_add_expense",
                onClick = { viewModel.navigateTo("EXPENSES") }
            )

            ActionHubRow(
                icon = Icons.Default.Payments,
                title = "Monthly Collection",
                subtitle = "Collect amount from members",
                iconBg = Color(0xFFE0F2F1),
                iconTint = EmeraldSecondary,
                testTag = "action_monthly_collection",
                onClick = { viewModel.navigateTo("MONTHLY_COLLECTION") }
            )

            ActionHubRow(
                icon = Icons.Default.Savings,
                title = "Create Savings Fund",
                subtitle = "Start a new savings scheme",
                iconBg = Color(0xFFE8F5E9),
                iconTint = DeepGreenPrimary,
                testTag = "action_create_savings",
                onClick = { viewModel.navigateTo("SAVINGS_FUNDS") }
            )

            ActionHubRow(
                icon = Icons.Default.Celebration,
                title = "Create Festival / Tour",
                subtitle = "Create festival or tour event",
                iconBg = Color(0xFFE0F2F1),
                iconTint = EmeraldSecondary,
                testTag = "action_create_festival",
                onClick = { viewModel.navigateTo("FUNDS") }
            )

            ActionHubRow(
                icon = Icons.Default.Storefront,
                title = "Give Business Fund",
                subtitle = "Provide business support to member",
                iconBg = Color(0xFFE8F5E9),
                iconTint = DarkGreenTertiary,
                testTag = "action_give_business_fund",
                onClick = { showBusinessFundDialog = true }
            )

            ActionHubRow(
                icon = Icons.Default.AccountBalance,
                title = "Give Member Loan",
                subtitle = "Provide loan to member",
                iconBg = Color(0xFFFFF3E0),
                iconTint = WarningAmber,
                testTag = "action_give_member_loan",
                onClick = { showLoanDialog = true }
            )

            ActionHubRow(
                icon = Icons.Default.PriceCheck,
                title = "Create Chit Fund",
                subtitle = "Start a new chit scheme",
                iconBg = Color(0xFFE0F2F1),
                iconTint = EmeraldSecondary,
                testTag = "action_create_chit",
                onClick = { viewModel.navigateTo("CREATE_CHIT") }
            )

            ActionHubRow(
                icon = Icons.Default.CardGiftcard,
                title = "Create Gift Distribution",
                subtitle = "Create festival gift distribution",
                iconBg = Color(0xFFFFEBEE),
                iconTint = StatusInactiveRed,
                testTag = "action_create_gift",
                onClick = { viewModel.navigateTo("GIFT_DISTRIBUTION") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security and Verification Bottom Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.7f)),
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
                    text = "All records are securely updated in real-time for Vellore Devangar Community welfare.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DeepGreenPrimary,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }

    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onConfirm = { name, mobile, email, dob, wedDate, blood, edu, occ, addr ->
                viewModel.addMember(name, mobile, email, dob, wedDate, blood, edu, occ, addr)
                showAddMemberDialog = false
            }
        )
    }

    if (showBusinessFundDialog) {
        var mId by remember { mutableStateOf("M001") }
        var mName by remember { mutableStateOf("Suresh M") }
        var amt by remember { mutableStateOf("10000") }
        var purpose by remember { mutableStateOf("Handloom yarn purchase assistance") }

        AlertDialog(
            onDismissRequest = { showBusinessFundDialog = false },
            title = { Text("Give Interest-Free Business Fund", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = mId,
                        onValueChange = { mId = it },
                        label = { Text("Member ID (e.g. M001)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mName,
                        onValueChange = { mName = it },
                        label = { Text("Member Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amt,
                        onValueChange = { amt = it },
                        label = { Text("Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        label = { Text("Purpose / Business details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amountVal = amt.toDoubleOrNull() ?: 10000.0
                        viewModel.giveBusinessFund(mId, mName, amountVal, purpose)
                        showBusinessFundDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Issue Fund")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBusinessFundDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showLoanDialog) {
        var mId by remember { mutableStateOf("M003") }
        var mName by remember { mutableStateOf("Ravi K") }
        var amt by remember { mutableStateOf("25000") }
        var purpose by remember { mutableStateOf("Loom machinery upgrade") }

        AlertDialog(
            onDismissRequest = { showLoanDialog = false },
            title = { Text("Give Member Loan (1.5% Interest)", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Interest rate configured in settings: 1.5% per month (₹1.50 per ₹100)", fontSize = 12.sp, color = OnSurfaceVariantText)
                    OutlinedTextField(
                        value = mId,
                        onValueChange = { mId = it },
                        label = { Text("Member ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mName,
                        onValueChange = { mName = it },
                        label = { Text("Member Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amt,
                        onValueChange = { amt = it },
                        label = { Text("Principal Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        label = { Text("Purpose") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amountVal = amt.toDoubleOrNull() ?: 25000.0
                        viewModel.issueMemberLoan(mId, mName, amountVal, purpose, 1.5)
                        showLoanDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Issue Loan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoanDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ActionHubRow(
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
