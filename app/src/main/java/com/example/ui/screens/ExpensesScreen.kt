package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ExpenseEntity
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun ExpensesScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val expenses by viewModel.expenses.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredExpenses = expenses.filter {
        it.category.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.date.contains(searchQuery)
    }

    val totalExpenseSum = expenses.sumOf { it.amount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Expense List",
            subtitle = "Master expense register & ledger debits",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = { showAddDialog = true }, modifier = Modifier.testTag("add_expense_header_btn")) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add Expense", tint = DeepGreenPrimary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search expense...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OutlineBorder) },
                modifier = Modifier.fillMaxWidth().testTag("expense_search_input"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Ruled Expense Table
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Date", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
                        Text("Details", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(2f))
                        Text("Amount", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.2f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Spacer(modifier = Modifier.width(24.dp))
                    }

                    HorizontalDivider(color = LedgerBorder)

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredExpenses, key = { it.id }) { exp ->
                            var showDetailDialog by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDetailDialog = true }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(exp.date, fontSize = 12.sp, color = OnSurfaceVariantText, modifier = Modifier.weight(1f))
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(exp.category, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = OnSurfaceText)
                                    if (exp.description.isNotBlank()) {
                                        Text(exp.description, fontSize = 11.sp, color = OnSurfaceVariantText, maxLines = 1)
                                    }
                                }
                                Text(
                                    text = CommunityRepository.formatRupee(exp.amount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = LedgerDebitRed,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = OutlineVariant,
                                    modifier = Modifier.size(20.dp).padding(start = 4.dp)
                                )
                            }
                            HorizontalDivider(color = SurfaceContainerHigh)

                            if (showDetailDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDetailDialog = false },
                                    title = { Text(exp.category, fontWeight = FontWeight.Bold) },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text("Date: ${exp.date}", fontSize = 13.sp)
                                            Text("Amount: ₹${exp.amount.toInt()}", fontWeight = FontWeight.Bold, color = LedgerDebitRed)
                                            Text("Description: ${exp.description.ifEmpty { "N/A" }}", fontSize = 13.sp)
                                            Text("Payment Method: ${exp.paymentMethod}", fontSize = 13.sp)
                                            Text("Notes: ${exp.notes.ifEmpty { "N/A" }}", fontSize = 13.sp)
                                        }
                                    },
                                    confirmButton = {
                                        TextButton(onClick = { showDetailDialog = false }) {
                                            Text("Close")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                viewModel.deleteExpense(exp.id)
                                                showDetailDialog = false
                                            }
                                        ) {
                                            Text("Delete Expense", color = StatusInactiveRed)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Total Summary Card (matching Image 10: Total ₹9,050)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText
                        )
                    )
                    Text(
                        text = CommunityRepository.formatRupee(totalExpenseSum),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepGreenPrimary
                        )
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddExpenseDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { date, cat, amt, desc, method, notes ->
                viewModel.recordExpense(date, cat, amt, desc, method, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, String, String, String) -> Unit
) {
    var date by remember { mutableStateOf("07-10-2026") }
    var category by remember { mutableStateOf("Tea & Snacks") }
    var amountText by remember { mutableStateOf("1500") }
    var notes by remember { mutableStateOf("Monthly Meeting") }
    var paymentMethod by remember { mutableStateOf("Cash") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense", fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date *") },
                    trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("expense_date_input")
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Expense Name / Category *") },
                    modifier = Modifier.fillMaxWidth().testTag("expense_name_input")
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount * (₹)") },
                    modifier = Modifier.fillMaxWidth().testTag("expense_amount_input")
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amtVal = amountText.toDoubleOrNull() ?: 0.0
                    if (category.isNotBlank() && amtVal > 0) {
                        onConfirm(date, category, amtVal, notes, paymentMethod, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary),
                modifier = Modifier.testTag("save_expense_button")
            ) {
                Text("Save Expense")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
