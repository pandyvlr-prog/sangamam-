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
fun SettingsScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    var tapSoundEnabled by remember { mutableStateOf(true) }
    var selectedSound by remember { mutableStateOf("Soft") }
    var pinLockEnabled by remember { mutableStateOf(false) }
    var loanRate by remember { mutableStateOf("1.5") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Settings",
            subtitle = "Preferences, loan configuration & security",
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
            // Community Profile Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Community Profile", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Name: சங்கமம் – DEVANGAR COMMUNITY", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Address: Thorapadi, Vellore, Tamil Nadu - 632002", color = OnSurfaceVariantText, fontSize = 12.sp)
                    Text("Contact: +91 98765 43210", color = OnSurfaceVariantText, fontSize = 12.sp)
                }
            }

            // Loan Interest Rate Configuration
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Loan Settings", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Configured default interest rate for all issued loans.", fontSize = 12.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = loanRate,
                        onValueChange = { loanRate = it },
                        label = { Text("Monthly Interest Rate (% per month)") },
                        modifier = Modifier.fillMaxWidth(),
                        suffix = { Text("% / mo (₹1.50 per ₹100)") }
                    )
                }
            }

            // Tap Sound Configuration (Spec 22.G)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Tap Sound", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 15.sp)
                            Text("Sound feedback on interactive controls", fontSize = 12.sp, color = OnSurfaceVariantText)
                        }
                        Switch(
                            checked = tapSoundEnabled,
                            onCheckedChange = { tapSoundEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = DeepGreenPrimary)
                        )
                    }

                    if (tapSoundEnabled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Select Sound Profile:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Soft", "Click", "Bell", "Pop").forEach { sound ->
                                FilterChip(
                                    selected = selectedSound == sound,
                                    onClick = { selectedSound = sound },
                                    label = { Text(sound, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepGreenPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Security PIN Lock
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Security PIN Lock", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 15.sp)
                        Text("Optional 4-digit PIN for treasurer operations", fontSize = 12.sp, color = OnSurfaceVariantText)
                    }
                    Switch(
                        checked = pinLockEnabled,
                        onCheckedChange = { pinLockEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = DeepGreenPrimary)
                    )
                }
            }

            // Notifications
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Community Reminders", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 15.sp)
                        Text("Pending collection and meeting alerts", fontSize = 12.sp, color = OnSurfaceVariantText)
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = DeepGreenPrimary)
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.userNotification.value = "Settings saved successfully"
                    viewModel.navigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_settings_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Text("Save Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun OfficeBearersScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val bearers by viewModel.officeBearers.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Office Bearers",
            subtitle = "Executive committee panel (2024–2027)",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 76.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            bearers.forEach { b ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(DeepGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = b.avatarInitials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(b.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OnSurfaceText)
                                Surface(shape = RoundedCornerShape(10.dp), color = EmeraldLightContainer.copy(alpha = 0.5f)) {
                                    Text(b.designation, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepGreenPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Phone: ${b.phone}", fontSize = 12.sp, color = OnSurfaceVariantText)
                            Text("Term: ${b.term}", fontSize = 11.sp, color = OnSurfaceVariantText)
                        }
                    }
                }
            }
        }
    }
}
