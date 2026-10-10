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
import com.example.data.local.entity.BusinessFundEntity
import com.example.data.local.entity.MemberLoanEntity
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun MemberLoansScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val loans by viewModel.memberLoans.collectAsState()
    val members by viewModel.members.collectAsState()
    var showRepaymentDialog by remember { mutableStateOf(false) }
    var selectedLoan by remember { mutableStateOf<MemberLoanEntity?>(null) }
    var showIssueLoanDialog by remember { mutableStateOf(false) }

    val totalOutstanding = loans.sumOf { it.outstandingPrincipal }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Member Loans",
            subtitle = "Interest @ 1.5% per month on outstanding principal",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = { showIssueLoanDialog = true }) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Give Loan", tint = DeepGreenPrimary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepGreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL OUTSTANDING PRINCIPAL", fontSize = 10.sp, color = LightMintAccent, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(CommunityRepository.formatRupee(totalOutstanding), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Active Borrowers: ${loans.count { it.status == "Active" }}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                    Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(alpha = 0.15f)) {
                        Text("Rate: 1.5% / mo", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(loans, key = { it.id }) { loan ->
                    val m = members.find { it.memberId == loan.memberId }
                    val name = m?.name ?: loan.memberId
                    val interestDue = loan.outstandingPrincipal * (loan.monthlyInterestRate / 100.0)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("${loan.memberId} • Loan Date: ${loan.loanDate}", fontSize = 11.sp, color = OnSurfaceVariantText)
                                }
                                Surface(shape = RoundedCornerShape(8.dp), color = if (loan.status == "Active") StatusActiveBg else SurfaceContainerHigh) {
                                    Text(loan.status, color = if (loan.status == "Active") StatusActiveGreen else OnSurfaceVariantText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(loan.purpose, fontSize = 12.sp, color = OnSurfaceText)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Outstanding Principal", fontSize = 10.sp, color = OnSurfaceVariantText)
                                    Text("₹${loan.outstandingPrincipal.toInt()}", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 14.sp)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Monthly Interest Due", fontSize = 10.sp, color = OnSurfaceVariantText)
                                    Text("₹${interestDue.toInt()}", fontWeight = FontWeight.Bold, color = WarningAmber, fontSize = 14.sp)
                                }
                            }

                            if (loan.status == "Active") {
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        selectedLoan = loan
                                        showRepaymentDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth().height(38.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                                ) {
                                    Text("Record Repayment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showRepaymentDialog && selectedLoan != null) {
        val l = selectedLoan!!
        val intDue = l.outstandingPrincipal * (l.monthlyInterestRate / 100.0)
        var paymentText by remember { mutableStateOf((intDue + 2000).toInt().toString()) }

        AlertDialog(
            onDismissRequest = { showRepaymentDialog = false },
            title = { Text("Record Loan Repayment", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Borrower: ${l.memberId}", fontWeight = FontWeight.Bold)
                    Text("Outstanding: ₹${l.outstandingPrincipal.toInt()} | Interest: ₹${intDue.toInt()} (1.5%)")
                    OutlinedTextField(
                        value = paymentText,
                        onValueChange = { paymentText = it },
                        label = { Text("Total Payment (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pVal = paymentText.toDoubleOrNull() ?: 0.0
                        if (pVal > 0) {
                            viewModel.recordLoanRepayment(l.id, pVal, "Monthly Repayment")
                            showRepaymentDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Save Repayment")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRepaymentDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showIssueLoanDialog) {
        var mId by remember { mutableStateOf("M001") }
        var mName by remember { mutableStateOf("Suresh M") }
        var amtText by remember { mutableStateOf("20000") }
        var purp by remember { mutableStateOf("Business expansion") }

        AlertDialog(
            onDismissRequest = { showIssueLoanDialog = false },
            title = { Text("Issue Member Loan", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = mId, onValueChange = { mId = it }, label = { Text("Member ID") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = mName, onValueChange = { mName = it }, label = { Text("Member Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = amtText, onValueChange = { amtText = it }, label = { Text("Principal Amount (₹)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = purp, onValueChange = { purp = it }, label = { Text("Purpose") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amtVal = amtText.toDoubleOrNull() ?: 20000.0
                        viewModel.issueMemberLoan(mId, mName, amtVal, purp, 1.5)
                        showIssueLoanDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Issue Loan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showIssueLoanDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun BusinessFundsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val bFunds by viewModel.businessFunds.collectAsState()
    val members by viewModel.members.collectAsState()
    var selectedFundForReturn by remember { mutableStateOf<BusinessFundEntity?>(null) }
    var showReturnDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Business Fund",
            subtitle = "Interest-free community enterprise support",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bFunds, key = { it.id }) { bf ->
                    val m = members.find { it.memberId == bf.memberId }
                    val name = m?.name ?: bf.memberId
                    val remaining = (bf.amountGiven - bf.amountReturned).coerceAtLeast(0.0)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("${bf.memberId} • Given: ${bf.dateGiven}", fontSize = 11.sp, color = OnSurfaceVariantText)
                                }
                                Surface(shape = RoundedCornerShape(8.dp), color = if (bf.status == "Active") StatusActiveBg else SurfaceContainerHigh) {
                                    Text(bf.status, color = if (bf.status == "Active") StatusActiveGreen else OnSurfaceVariantText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(bf.purpose, fontSize = 12.sp, color = OnSurfaceText)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Amount Given: ₹${bf.amountGiven.toInt()}", fontSize = 12.sp, color = OnSurfaceVariantText)
                                Text("Returned: ₹${bf.amountReturned.toInt()}", fontSize = 12.sp, color = LedgerCreditGreen, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Remaining: ₹${remaining.toInt()}", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 13.sp)

                            if (bf.status == "Active") {
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        selectedFundForReturn = bf
                                        showReturnDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth().height(38.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                                ) {
                                    Text("Record Return", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showReturnDialog && selectedFundForReturn != null) {
        val bf = selectedFundForReturn!!
        val remaining = bf.amountGiven - bf.amountReturned
        var returnAmtText by remember { mutableStateOf(remaining.toInt().toString()) }

        AlertDialog(
            onDismissRequest = { showReturnDialog = false },
            title = { Text("Record Business Fund Return", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Member: ${bf.memberId} | Remaining: ₹${remaining.toInt()}")
                    OutlinedTextField(
                        value = returnAmtText,
                        onValueChange = { returnAmtText = it },
                        label = { Text("Return Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val rVal = returnAmtText.toDoubleOrNull() ?: 0.0
                        if (rVal > 0) {
                            viewModel.returnBusinessFund(bf.id, rVal, "Installment returned")
                            showReturnDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Text("Save Return")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDialog = false }) { Text("Cancel") }
            }
        )
    }
}
