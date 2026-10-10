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
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun AttendanceScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.members.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val attendanceMap = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(members) {
        if (attendanceMap.isEmpty()) {
            members.forEachIndexed { idx, m ->
                attendanceMap[m.memberId] = (idx < 130) // 130 present
            }
        }
    }

    val presentCount = attendanceMap.values.count { it }
    val absentCount = members.size - presentCount

    val filteredMembers = members.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.memberId.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Meeting & Attendance",
            subtitle = "October General Body Meeting (12-10-2026)",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Meeting Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("October General Body Meeting", fontWeight = FontWeight.Bold, color = DeepGreenPrimary, fontSize = 14.sp)
                        Surface(shape = RoundedCornerShape(8.dp), color = StatusActiveBg) {
                            Text("Scheduled", color = StatusActiveGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Date: Sunday, 12-10-2026 at 10:00 AM • Devangar Community Hall", fontSize = 11.sp, color = OnSurfaceVariantText)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Present: $presentCount", fontWeight = FontWeight.Bold, color = StatusActiveGreen, fontSize = 13.sp)
                        Text("Absent: $absentCount", fontWeight = FontWeight.Bold, color = StatusInactiveRed, fontSize = 13.sp)
                        TextButton(
                            onClick = {
                                members.forEach { m -> attendanceMap[m.memberId] = true }
                            }
                        ) {
                            Text("Mark All Present", fontSize = 12.sp, color = DeepGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search member...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Attendance Member List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredMembers, key = { it.memberId }) { m ->
                    val isPresent = attendanceMap[m.memberId] ?: false

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(m.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${m.memberId} • ${m.mobile}", fontSize = 11.sp, color = OnSurfaceVariantText)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { attendanceMap[m.memberId] = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isPresent) DeepGreenPrimary else SurfaceContainerHigh
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Present", fontSize = 11.sp, color = if (isPresent) Color.White else OnSurfaceVariantText)
                                }

                                Button(
                                    onClick = { attendanceMap[m.memberId] = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (!isPresent) StatusInactiveRed else SurfaceContainerHigh
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Absent", fontSize = 11.sp, color = if (!isPresent) Color.White else OnSurfaceVariantText)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.userNotification.value = "Attendance for $presentCount members saved"
                    viewModel.navigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_attendance_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Text("Save Attendance Register", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
