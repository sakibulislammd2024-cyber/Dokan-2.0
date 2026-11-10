package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.AppLanguage
import com.example.ui.navigation.NavTab
import com.example.ui.theme.DokanTheme
import com.example.ui.theme.DokanVividGreen

/**
 * Floating glass pill dock navigation (not full width):
 * - Home, Stock, big raised glowing green SELL button in center, History, Business.
 * - Active item shows a green dot underneath.
 * - Glass surface with rounded corners, subtle top gradient border, and ambient shadow.
 */
@Composable
fun AppBottomBar(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val colors = DokanTheme.colors
    val dockShape = RoundedCornerShape(38.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = dockShape,
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.6f)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            (if (colors.isDark) Color.White else Color.Black).copy(alpha = 0.22f),
                            (if (colors.isDark) Color.White else Color.Black).copy(alpha = 0.05f)
                        )
                    ),
                    shape = dockShape
                )
                .clip(dockShape),
            shape = dockShape,
            color = if (colors.isDark) Color(0xF2161616) else Color(0xF5FFFFFF) // ~95% glass
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // 1. HOME
                DockTabItem(
                    title = if (currentLanguage == AppLanguage.BANGLA) "হোম" else "Home",
                    icon = if (currentTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    isSelected = currentTab == NavTab.HOME,
                    onClick = { onTabSelected(NavTab.HOME) },
                    testTag = "nav_tab_home"
                )

                // 2. STOCK
                DockTabItem(
                    title = if (currentLanguage == AppLanguage.BANGLA) "স্টক" else "Stock",
                    icon = if (currentTab == NavTab.STOCK) Icons.Filled.ShowChart else Icons.Outlined.ShowChart,
                    isSelected = currentTab == NavTab.STOCK,
                    onClick = { onTabSelected(NavTab.STOCK) },
                    testTag = "nav_tab_stock"
                )

                // 3. CENTER RAISED GLOWING SELL BUTTON
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .testTag("nav_tab_sell")
                        // Radiant green outer glow
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        DokanVividGreen.copy(alpha = 0.55f),
                                        DokanVividGreen.copy(alpha = 0.20f),
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = size.width * 0.72f
                                ),
                                radius = size.width * 0.72f,
                                center = center
                            )
                        }
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape,
                            ambientColor = DokanVividGreen.copy(alpha = 0.6f),
                            spotColor = DokanVividGreen.copy(alpha = 0.7f)
                        )
                        .clip(CircleShape)
                        .background(DokanVividGreen)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.Black.copy(0.25f)),
                            onClick = { onTabSelected(NavTab.SELL) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SELL",
                        color = Color(0xFF0A1805),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.5.sp,
                        letterSpacing = 0.6.sp
                    )
                }

                // 4. HISTORY
                DockTabItem(
                    title = if (currentLanguage == AppLanguage.BANGLA) "ইতিহাস" else "History",
                    icon = if (currentTab == NavTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                    isSelected = currentTab == NavTab.HISTORY,
                    onClick = { onTabSelected(NavTab.HISTORY) },
                    testTag = "nav_tab_history"
                )

                // 5. BUSINESS
                DockTabItem(
                    title = if (currentLanguage == AppLanguage.BANGLA) "ব্যবসা" else "Business",
                    icon = if (currentTab == NavTab.BUSINESS) Icons.Filled.BusinessCenter else Icons.Outlined.BusinessCenter,
                    isSelected = currentTab == NavTab.BUSINESS,
                    onClick = { onTabSelected(NavTab.BUSINESS) },
                    testTag = "nav_tab_business"
                )
            }
        }
    }
}

@Composable
private fun DockTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val colors = DokanTheme.colors

    Column(
        modifier = Modifier
            .testTag(testTag)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 24.dp),
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) colors.textPrimary else colors.textMuted,
            modifier = Modifier.size(21.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = title,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) colors.textPrimary else colors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Active Item Green Dot
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (isSelected) DokanVividGreen else Color.Transparent)
        )
    }
}
