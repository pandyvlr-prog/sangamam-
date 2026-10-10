package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.CommunityRepository
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMemberScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.members.collectAsState()
    val nextMemberNumber = members.size + 1
    val nextMemberId = "M" + nextMemberNumber.toString().padStart(3, '0')

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("15-05-1990") }
    var weddingDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var bloodGroup by remember { mutableStateOf("B+ Positive") }
    var education by remember { mutableStateOf("B.Com") }
    var occupation by remember { mutableStateOf("Textile Business") }
    var streetAddress by remember { mutableStateOf("No. 24, Sannathi Street") }
    var area by remember { mutableStateOf("Thorapadi, Vellore - 632002") }
    var googleMapsLink by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isOfficeBearer by remember { mutableStateOf(false) }
    var designation by remember { mutableStateOf("Committee Member") }
    var termPeriod by remember { mutableStateOf("2024-2027") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val calculatedAge = remember(dob) {
        CommunityRepository.calculateAge(dob)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "New Member Registration",
            subtitle = "உறுப்பினர் சேர்க்கை பதிவு",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Next Member ID Hero Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepGreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (name.isNotBlank()) name.take(2).uppercase() else "M#",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ASSIGNED MEMBER ID",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = LightMintAccent,
                                    letterSpacing = 1.sp,
                                    fontSize = 10.sp
                                )
                            )
                            Text(
                                text = nextMemberId,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldLightContainer.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = "Auto-allocated",
                            color = LightMintAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (errorMessage != null) {
                Surface(
                    color = StatusInactiveBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = StatusInactiveRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(errorMessage!!, color = StatusInactiveRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Section 1: Personal Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "1. Personal Information (தனிநபர் விவரங்கள்)",
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            errorMessage = null
                        },
                        label = { Text("Full Name * (முழு பெயர்)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = DeepGreenPrimary) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = {
                            mobile = it
                            errorMessage = null
                        },
                        label = { Text("Mobile Number * (கைபேசி எண்)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = DeepGreenPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("reg_mobile_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address (மின்னஞ்சல் முகவரி - Optional)") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = OutlineBorder) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Gender Selector
                    Text("Gender (பாலினம்):", fontSize = 12.sp, color = OnSurfaceVariantText, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Male", "Female", "Other").forEach { g ->
                            FilterChip(
                                selected = gender == g,
                                onClick = { gender = g },
                                label = { Text(g, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepGreenPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Date of Birth & Calculated Age
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            label = { Text("Date of Birth") },
                            placeholder = { Text("DD-MM-YYYY") },
                            leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null, tint = DeepGreenPrimary) },
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Surface(
                            modifier = Modifier.weight(0.9f).height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerHigh
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Age (வயது)", fontSize = 10.sp, color = OnSurfaceVariantText)
                                Text("$calculatedAge Years", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepGreenPrimary)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = weddingDate,
                        onValueChange = { weddingDate = it },
                        label = { Text("Wedding Date / திருமண நாள் (Optional)") },
                        placeholder = { Text("DD-MM-YYYY") },
                        leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null, tint = OutlineBorder) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Blood Group
                    Text("Blood Group (இரத்த வகை):", fontSize = 12.sp, color = OnSurfaceVariantText, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("B+", "O+", "A+", "AB+").forEach { bg ->
                            FilterChip(
                                selected = bloodGroup.startsWith(bg),
                                onClick = { bloodGroup = "$bg Positive" },
                                label = { Text(bg, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepGreenPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Section 2: Education & Occupation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "2. Education & Occupation (கல்வி & தொழில்)",
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        value = education,
                        onValueChange = { education = it },
                        label = { Text("Education / Qualification") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = DeepGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = occupation,
                        onValueChange = { occupation = it },
                        label = { Text("Occupation / தொழில்") },
                        leadingIcon = { Icon(Icons.Default.Work, contentDescription = null, tint = DeepGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            // Section 3: Residential Address
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "3. Residential Address (இருப்பிட முகவரி)",
                        fontWeight = FontWeight.Bold,
                        color = DeepGreenPrimary,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        value = streetAddress,
                        onValueChange = { streetAddress = it },
                        label = { Text("Door No & Street") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = DeepGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = area,
                        onValueChange = { area = it },
                        label = { Text("Area / Landmark & PIN") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = DeepGreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = googleMapsLink,
                        onValueChange = { googleMapsLink = it },
                        label = { Text("Google Maps Link (Optional)") },
                        leadingIcon = { Icon(Icons.Default.Map, contentDescription = null, tint = OutlineBorder) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            // Section 4: Committee / Office Bearer Role
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Office Bearer / நிர்வாகி",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DeepGreenPrimary
                            )
                            Text(
                                text = "Is this member part of the executive committee?",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantText
                            )
                        }
                        Switch(
                            checked = isOfficeBearer,
                            onCheckedChange = { isOfficeBearer = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = DeepGreenPrimary
                            )
                        )
                    }

                    if (isOfficeBearer) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = designation,
                            onValueChange = { designation = it },
                            label = { Text("Designation (பதவி)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = termPeriod,
                            onValueChange = { termPeriod = it },
                            label = { Text("Term Period (பதவிக்காலம்)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Section 5: Notes & Remarks
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Remarks / Notes (குறிப்புகள்)",
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceText,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Enter family native village, reference or special notes...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Primary Registration Submit Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter Member Full Name"
                        return@Button
                    }
                    if (mobile.isBlank() || mobile.length < 5) {
                        errorMessage = "Please enter a valid Mobile Number"
                        return@Button
                    }

                    val fullAddress = if (streetAddress.isNotBlank()) "$streetAddress, $area" else area
                    viewModel.addMember(
                        name = name,
                        mobile = mobile,
                        email = email,
                        dob = dob,
                        weddingDate = weddingDate,
                        bloodGroup = bloodGroup,
                        education = education,
                        occupation = occupation,
                        address = fullAddress,
                        gender = gender,
                        isOfficeBearer = isOfficeBearer,
                        designation = if (isOfficeBearer) designation else "",
                        termPeriod = if (isOfficeBearer) termPeriod else "",
                        notes = notes
                    )
                    viewModel.navigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_registration_button"),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Register Member / உறுப்பினர் சேர்",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
