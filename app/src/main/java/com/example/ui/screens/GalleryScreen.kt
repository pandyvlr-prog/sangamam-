package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.GalleryItemEntity
import com.example.ui.CommunityViewModel
import com.example.ui.components.SubScreenHeader
import com.example.ui.theme.*

@Composable
fun GalleryScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.galleryItems.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedPhoto by remember { mutableStateOf<GalleryItemEntity?>(null) }
    val context = LocalContext.current

    val filteredPhotos = if (selectedCategory == "All") items else items.filter { it.category == selectedCategory }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        SubScreenHeader(
            title = "Community Gallery",
            subtitle = "Memories & celebrations album",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = { viewModel.userNotification.value = "Camera & Photo Picker available" }) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = "Add Photo", tint = DeepGreenPrimary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .padding(bottom = 76.dp)
        ) {
            // Category Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Festivals", "Tours", "Meetings").forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
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
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid of photos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredPhotos, key = { it.id }) { photo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPhoto = photo },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = photo.photoUrl,
                                contentDescription = photo.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = photo.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    color = OnSurfaceText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = photo.date,
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariantText
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedPhoto != null) {
        val p = selectedPhoto!!
        AlertDialog(
            onDismissRequest = { selectedPhoto = null },
            title = { Text(p.title, fontWeight = FontWeight.Bold, color = DeepGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AsyncImage(
                        model = p.photoUrl,
                        contentDescription = p.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(p.caption, fontSize = 13.sp, color = OnSurfaceText)
                    Text("Date: ${p.date} • Category: ${p.category}", fontSize = 11.sp, color = OnSurfaceVariantText)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${p.title}\n${p.caption}\nSangamam Devangar Community")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Photo"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreenPrimary)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPhoto = null }) { Text("Close") }
            }
        )
    }
}
