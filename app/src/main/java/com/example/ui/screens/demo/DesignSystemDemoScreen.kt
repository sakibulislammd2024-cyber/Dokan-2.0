package com.example.ui.screens.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DokanBackground
import com.example.ui.components.DokanBottomSheetContainer
import com.example.ui.components.DokanSegmentedControl
import com.example.ui.components.DokanToast
import com.example.ui.components.DokanToggle
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingPrimaryButton
import com.example.ui.components.HeroGlassCard
import com.example.ui.components.LetterInitialAvatar
import com.example.ui.components.PillChip
import com.example.ui.components.ProgressRing
import com.example.ui.components.SearchPill
import com.example.ui.components.SlimProgressBar
import com.example.ui.components.StatusTag
import com.example.ui.components.StatusTagType
import com.example.ui.theme.DokanTheme
import com.example.ui.theme.DokanTypographyTokens
import com.example.ui.theme.DokanVividGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesignSystemDemoScreen(
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    onExitDemo: () -> Unit
) {
    val colors = DokanTheme.colors

    var searchQuery by remember { mutableStateOf("") }
    var selectedPeriod by remember { mutableStateOf("Today") }
    var selectedFilterChip by remember { mutableStateOf("All") }
    var toggleState by remember { mutableStateOf(true) }
    var progressVal by remember { mutableFloatStateOf(0.68f) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("Action completed successfully") }

    DokanBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header item
                item {
                    Spacer(modifier = Modifier.height(36.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onExitDemo) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = colors.textPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = "DESIGN SYSTEM",
                                    style = DokanTypographyTokens.SectionLabel,
                                    color = DokanVividGreen
                                )
                                Text(
                                    text = "Dokan Tokens & UI",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = colors.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Theme Mode Toggle (Dark / Light)
                        IconButton(
                            onClick = { onToggleDarkTheme(!isDarkTheme) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(colors.cardGlass)
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = if (isDarkTheme) DokanVividGreen else colors.textPrimary
                            )
                        }
                    }
                }

                // 1. Hero Card Showcase (Green glowing gradient card with tabular numbers)
                item {
                    Text(
                        text = "1. HERO GLASS CARD (VIVID GREEN GLOW & TABULAR NUMBERS)",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    HeroGlassCard(
                        onClick = {
                            toastMessage = "Tapped Hero Card"
                            showToast = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatusTag(text = "TODAY", type = StatusTagType.SUCCESS)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = DokanVividGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+12% vs yesterday",
                                    fontSize = 11.sp,
                                    color = DokanVividGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "৳25,000",
                            style = DokanTypographyTokens.StatNumberLarge,
                            color = DokanVividGreen
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "BOUGHT",
                                    style = DokanTypographyTokens.SectionLabel,
                                    color = colors.textSecondary
                                )
                                Text(
                                    text = "৳25,000",
                                    style = DokanTypographyTokens.StatNumberSmall,
                                    color = colors.amber
                                )
                            }
                            Column {
                                Text(
                                    text = "SOLD",
                                    style = DokanTypographyTokens.SectionLabel,
                                    color = colors.textSecondary
                                )
                                Text(
                                    text = "৳1,050",
                                    style = DokanTypographyTokens.StatNumberSmall,
                                    color = colors.textPrimary
                                )
                            }
                            Column {
                                Text(
                                    text = "PROFIT",
                                    style = DokanTypographyTokens.SectionLabel,
                                    color = colors.textSecondary
                                )
                                Text(
                                    text = "৳250",
                                    style = DokanTypographyTokens.StatNumberSmall,
                                    color = DokanVividGreen
                                )
                            }
                        }
                    }
                }

                // 2. Search Pill
                item {
                    Text(
                        text = "2. SEARCH PILL",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SearchPill(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search invoice, customer, product...",
                        trailingAction = {
                            IconButton(onClick = {
                                toastMessage = "Scanner opened"
                                showToast = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Scan Barcode",
                                    tint = DokanVividGreen
                                )
                            }
                        }
                    )
                }

                // 3. Segmented Control
                item {
                    Text(
                        text = "3. SEGMENTED CONTROL",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DokanSegmentedControl(
                        items = listOf("Today", "Week", "Month", "Year"),
                        selectedItem = selectedPeriod,
                        onItemSelected = { selectedPeriod = it }
                    )
                }

                // 4. Pill Chips
                item {
                    Text(
                        text = "4. PILL CHIPS & STATUS TAGS",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("All", "Paid", "Baki", "Low Stock", "Wholesale").forEach { tag ->
                            PillChip(
                                text = tag,
                                selected = selectedFilterChip == tag,
                                badgeCount = if (tag == "Baki") 3 else if (tag == "Low Stock") 2 else null,
                                onClick = { selectedFilterChip = tag }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusTag(text = "Active", type = StatusTagType.SUCCESS)
                        StatusTag(text = "Due ৳3,500", type = StatusTagType.WARNING)
                        StatusTag(text = "Overdue", type = StatusTagType.DANGER)
                        StatusTag(text = "Syncing", type = StatusTagType.INFO)
                        StatusTag(text = "Task", type = StatusTagType.NEUTRAL)
                    }
                }

                // 5. Standard Glass Card with Letter Initial Avatar & Info
                item {
                    Text(
                        text = "5. STANDARD GLASS CARD & INITIAL AVATAR (NO PHOTOS)",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    GlassCard(
                        onClick = {
                            toastMessage = "Selected Karim Builders"
                            showToast = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LetterInitialAvatar(name = "Karim Builders")
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Karim Builders",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colors.textPrimary,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "01711-223344 · Regular Customer",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary,
                                    maxLines = 1
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "৳3,500",
                                    style = DokanTypographyTokens.StatNumberSmall,
                                    color = colors.amber
                                )
                                StatusTag(text = "Due", type = StatusTagType.WARNING)
                            }
                        }
                    }
                }

                // 6. Progress Ring & Slim Progress Bar
                item {
                    Text(
                        text = "6. PROGRESS GAUGES (RING & SLIM BAR)",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ProgressRing(
                                progress = progressVal,
                                modifier = Modifier.size(86.dp),
                                strokeWidth = 8.dp
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${(progressVal * 100).toInt()}%",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = DokanVividGreen
                                    )
                                    Text(
                                        text = "MARGIN",
                                        style = DokanTypographyTokens.SectionLabel,
                                        color = colors.textSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Target Completion",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = colors.textPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                SlimProgressBar(progress = progressVal)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Current: ৳34,000",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                    Text(
                                        text = "Goal: ৳50,000",
                                        fontSize = 11.sp,
                                        color = colors.textSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // 7. Toggle Control
                item {
                    Text(
                        text = "7. DOKAN TOGGLE",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Realtime Cloud Sync",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = colors.textPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Automatically synchronizes transactions across devices",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.textSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            DokanToggle(
                                checked = toggleState,
                                onCheckedChange = { toggleState = it }
                            )
                        }
                    }
                }

                // 8. Buttons Showcase
                item {
                    Text(
                        text = "8. GLOWING PRIMARY & GLASS BUTTONS",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GlowingPrimaryButton(
                        text = "+ Add Reminder",
                        onClick = {
                            toastMessage = "Glowing Primary Button Tapped"
                            showToast = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GlassButton(
                            text = "Open Sheet",
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.weight(1f)
                        )
                        GlassButton(
                            text = "Show Toast",
                            onClick = {
                                toastMessage = "Verified design tokens & components!"
                                showToast = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Bottom spacer
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // Floating Toast Notification
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 44.dp)
            ) {
                DokanToast(
                    message = toastMessage,
                    visible = showToast
                )
            }

            // Bottom Sheet Modal
            if (showBottomSheet) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.65f))
                        .clickable { showBottomSheet = false },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    DokanBottomSheetContainer(
                        title = "Glass Bottom Sheet",
                        onClose = { showBottomSheet = false }
                    ) {
                        Text(
                            text = "This demonstrates the reusable glass bottom sheet with 28px top rounded corners, grab handle, and gradient borders.",
                            color = colors.textSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        GlowingPrimaryButton(
                            text = "Confirm & Close",
                            onClick = { showBottomSheet = false },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}
