package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.ripple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DokanAmber
import com.example.ui.theme.DokanDeepGreen
import com.example.ui.theme.DokanTheme
import com.example.ui.theme.DokanTypographyTokens
import com.example.ui.theme.DokanVividGreen

/**
 * 1. Background: Near-black #0F0F0F with two soft green radial glows:
 *    - Top-left ambient glow
 *    - Center-back aura behind main content area
 *    - Darkened screen edges (vignette preservation)
 */
@Composable
fun DokanBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = DokanTheme.colors
    val isDark = colors.isDark

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .drawBehind {
                val width = size.width
                val height = size.height

                if (isDark) {
                    // Top-Left Soft Green Glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DokanVividGreen.copy(alpha = 0.22f),
                                DokanVividGreen.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            center = Offset(x = width * 0.05f, y = height * 0.08f),
                            radius = width * 0.70f
                        ),
                        radius = width * 0.70f,
                        center = Offset(x = width * 0.05f, y = height * 0.08f)
                    )

                    // Secondary Mid-Screen Ambient Glow (behind hero cards / main area)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DokanDeepGreen.copy(alpha = 0.16f),
                                DokanVividGreen.copy(alpha = 0.04f),
                                Color.Transparent
                            ),
                            center = Offset(x = width * 0.85f, y = height * 0.35f),
                            radius = width * 0.65f
                        ),
                        radius = width * 0.65f,
                        center = Offset(x = width * 0.85f, y = height * 0.35f)
                    )
                } else {
                    // Light mode subtle mint top-left glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DokanVividGreen.copy(alpha = 0.09f),
                                Color.Transparent
                            ),
                            center = Offset(x = width * 0.10f, y = height * 0.08f),
                            radius = width * 0.60f
                        ),
                        radius = width * 0.60f,
                        center = Offset(x = width * 0.10f, y = height * 0.08f)
                    )
                }
            }
    ) {
        content()
    }
}

/**
 * 2. GlassCard: 28px/28dp rounded corners, glass surface (~65% #202020 in dark),
 *    thin gradient border (brighter at top), soft inner highlight, deep shadow.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 28.dp,
    onClick: (() -> Unit)? = null,
    testTag: String? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = DokanTheme.colors
    val shape = RoundedCornerShape(cornerRadius)

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = DokanVividGreen.copy(alpha = 0.2f)),
            onClick = onClick
        )
    } else Modifier

    val tagModifier = if (testTag != null) Modifier.testTag(testTag) else Modifier

    Surface(
        modifier = modifier
            .then(tagModifier)
            .shadow(
                elevation = if (colors.isDark) 8.dp else 4.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.40f),
                spotColor = Color.Black.copy(alpha = 0.50f)
            )
            .border(
                width = 1.dp,
                brush = colors.borderGradient,
                shape = shape
            )
            .clip(shape)
            .then(clickableModifier),
        shape = shape,
        color = colors.cardGlass
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    // Soft inner top highlight line
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                (if (colors.isDark) Color.White else Color.Black).copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(24.dp.toPx(), 1.dp.toPx()),
                        end = Offset(size.width - 24.dp.toPx(), 1.dp.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(contentPadding)
        ) {
            content()
        }
    }
}

/**
 * 3. HeroGlassCard: Hero card variant with vibrant green gradient fill,
 *    green glowing border, and ambient green aura.
 */
@Composable
fun HeroGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 28.dp,
    onClick: (() -> Unit)? = null,
    testTag: String? = null,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = DokanTheme.colors
    val shape = RoundedCornerShape(cornerRadius)

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = DokanVividGreen.copy(alpha = 0.25f)),
            onClick = onClick
        )
    } else Modifier

    val tagModifier = if (testTag != null) Modifier.testTag(testTag) else Modifier

    Box(
        modifier = modifier
            .then(tagModifier)
            // Outer radiant green aura
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            DokanVividGreen.copy(alpha = 0.20f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = size.width * 0.70f
                    ),
                    radius = size.width * 0.70f,
                    center = center
                )
            }
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = DokanVividGreen.copy(alpha = 0.35f),
                spotColor = DokanVividGreen.copy(alpha = 0.45f)
            )
            .border(
                width = 1.5.dp,
                brush = colors.heroBorderGradient,
                shape = shape
            )
            .clip(shape)
            .background(brush = colors.heroCardGradient)
            .then(clickableModifier)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            content()
        }
    }
}

/**
 * 4. GlowingPrimaryButton: Vivid green #5DD62C with dark text (#0A1805),
 *    single-line, pill shape (28dp/CircleShape), and ambient green glow.
 */
@Composable
fun GlowingPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    testTag: String = "primary_button"
) {
    val shape = RoundedCornerShape(28.dp)

    Box(
        modifier = modifier
            .testTag(testTag)
            .heightIn(min = 52.dp)
            .drawBehind {
                if (enabled) {
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DokanVividGreen.copy(alpha = 0.45f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width * 0.65f
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
                    )
                }
            }
            .shadow(
                elevation = if (enabled) 10.dp else 0.dp,
                shape = shape,
                ambientColor = DokanVividGreen.copy(alpha = 0.5f),
                spotColor = DokanVividGreen.copy(alpha = 0.6f)
            )
            .clip(shape)
            .background(
                if (enabled) DokanVividGreen else Color(0xFF283820)
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.2f)),
                onClick = onClick,
                role = Role.Button
            )
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (enabled) Color(0xFF0A1805) else Color(0x66FFFFFF),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = if (enabled) Color(0xFF0A1805) else Color(0x66FFFFFF),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = if (enabled) Color(0xFF0A1805) else Color(0x66FFFFFF),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 5. GlassButton: Translucent glass pill button with subtle top border and crisp white text.
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    testTag: String = "glass_button"
) {
    val colors = DokanTheme.colors
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .testTag(testTag)
            .heightIn(min = 44.dp)
            .border(1.dp, colors.borderGradient, shape)
            .clip(shape)
            .background(colors.cardGlass)
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = DokanVividGreen.copy(alpha = 0.2f)),
                onClick = onClick,
                role = Role.Button
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = colors.textPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = colors.textPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = colors.textPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * 6. PillChips: Compact pill filter/tag chip with active/inactive states.
 */
@Composable
fun PillChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int? = null,
    icon: ImageVector? = null,
    testTag: String = "pill_chip"
) {
    val colors = DokanTheme.colors
    val shape = CircleShape

    val backgroundBrush = if (selected) {
        SolidColor(DokanVividGreen)
    } else {
        SolidColor(colors.cardGlass)
    }

    val borderModifier = if (selected) {
        Modifier.border(1.dp, DokanVividGreen, shape)
    } else {
        Modifier.border(1.dp, colors.borderGradient, shape)
    }

    val textColor = if (selected) Color(0xFF0A1805) else colors.textSecondary

    Box(
        modifier = modifier
            .testTag(testTag)
            .then(borderModifier)
            .clip(shape)
            .background(backgroundBrush)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = DokanVividGreen.copy(alpha = 0.2f)),
                onClick = onClick,
                role = Role.Tab
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.5.sp,
                maxLines = 1
            )
            if (badgeCount != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (selected) Color(0xFF0A1805) else colors.deepGreen)
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = if (selected) DokanVividGreen else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 7. SegmentedControl: Sleek pill track with rounded sliding active pill.
 */
@Composable
fun <T> DokanSegmentedControl(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelProvider: (T) -> String = { it.toString() }
) {
    val colors = DokanTheme.colors
    val trackShape = RoundedCornerShape(24.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(1.dp, colors.borderGradient, trackShape),
        shape = trackShape,
        color = Color(0x99181818)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item == selectedItem
                val itemShape = RoundedCornerShape(20.dp)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(itemShape)
                        .background(
                            if (isSelected) Color(0xFF2C2C2C) else Color.Transparent
                        )
                        .border(
                            width = if (isSelected) 1.dp else 0.dp,
                            color = if (isSelected) Color(0x40FFFFFF) else Color.Transparent,
                            shape = itemShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = DokanVividGreen.copy(0.2f)),
                            onClick = { onItemSelected(item) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = labelProvider(item),
                        color = if (isSelected) Color.White else colors.textSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * 8. DokanToggle: Modern switch with glowing green track & thumb when enabled.
 */
@Composable
fun DokanToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFF0A1805),
            checkedTrackColor = DokanVividGreen,
            uncheckedThumbColor = Color(0xFF888888),
            uncheckedTrackColor = Color(0xFF242424),
            uncheckedBorderColor = Color(0x33FFFFFF),
            checkedBorderColor = DokanVividGreen
        )
    )
}

/**
 * 9. SearchPill: Glass search pill with magnifying icon, placeholder, and optional end action.
 */
@Composable
fun SearchPill(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onSearch: (() -> Unit)? = null,
    trailingAction: (@Composable () -> Unit)? = null
) {
    val colors = DokanTheme.colors
    val shape = RoundedCornerShape(26.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, colors.borderGradient, shape)
            .clip(shape)
            .background(colors.cardGlass)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = colors.textSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = colors.textMuted,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = colors.textPrimary,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(DokanVividGreen),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (value.isNotEmpty()) {
            IconButton(
                onClick = { onValueChange("") },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        if (trailingAction != null) {
            Spacer(modifier = Modifier.width(4.dp))
            trailingAction()
        }
    }
}

/**
 * 10. ProgressRing: Circular gauge with background track and animated glowing green arc.
 */
@Composable
fun ProgressRing(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    trackColor: Color = Color(0x33FFFFFF),
    progressColor: Color = DokanVividGreen,
    centerContent: @Composable (BoxScope.() -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "progress_ring"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val diameter = size.minDimension - strokePx
            val radius = diameter / 2f
            val centerOffset = Offset(size.width / 2f, size.height / 2f)

            // Background ring
            drawCircle(
                color = trackColor,
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokePx)
            )

            // Animated progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        if (centerContent != null) {
            centerContent()
        }
    }
}

/**
 * 11. SlimProgressBar: 4-6dp slim rounded progress bar with glowing green fill.
 */
@Composable
fun SlimProgressBar(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    height: Dp = 5.dp,
    trackColor: Color = Color(0xFF262626),
    progressColor: Color = DokanVividGreen
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500),
        label = "slim_progress"
    )
    val shape = RoundedCornerShape(height / 2)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height)
                .clip(shape)
                .background(
                    Brush.horizontalGradient(
                        listOf(DokanDeepGreen, progressColor)
                    )
                )
        )
    }
}

/**
 * 12. StatusTag: Compact badge for states (Active, Paid, Due, Task, Low Stock, etc.).
 */
enum class StatusTagType {
    SUCCESS, // Green (Active, Paid)
    WARNING, // Amber (Due, Low Stock)
    DANGER,  // Red (Overdue, Danger)
    INFO,    // Blue (Info, Sync)
    NEUTRAL  // Dark glass (Task, Default)
}

@Composable
fun StatusTag(
    text: String,
    modifier: Modifier = Modifier,
    type: StatusTagType = StatusTagType.SUCCESS
) {
    val (bg, contentColor) = when (type) {
        StatusTagType.SUCCESS -> DokanDeepGreen.copy(alpha = 0.35f) to DokanVividGreen
        StatusTagType.WARNING -> Color(0x33FFB020) to Color(0xFFFFB020)
        StatusTagType.DANGER -> Color(0x33FF453A) to Color(0xFFFF453A)
        StatusTagType.INFO -> Color(0x332997FF) to Color(0xFF2997FF)
        StatusTagType.NEUTRAL -> Color(0x33FFFFFF) to Color(0xCCFFFFFF)
    }

    Box(
        modifier = modifier
            .border(0.75.dp, contentColor.copy(alpha = 0.45f), CircleShape)
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = 9.dp, vertical = 2.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

/**
 * 13. LetterInitialAvatar: Elegant circular dark glass avatar with initial letter (no photos).
 */
@Composable
fun LetterInitialAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    textColor: Color = Color.White
) {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "D"
    val colors = DokanTheme.colors

    Box(
        modifier = modifier
            .size(size)
            .shadow(4.dp, CircleShape, ambientColor = Color.Black.copy(0.3f))
            .border(1.dp, colors.borderGradient, CircleShape)
            .clip(CircleShape)
            .background(Color(0xFF242424)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = textColor,
            fontSize = (size.value * 0.42f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 14. DokanBottomSheetContainer: Sleek glass bottom sheet modal wrapper.
 */
@Composable
fun DokanBottomSheetContainer(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = DokanTheme.colors
    val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, sheetShape)
            .border(1.dp, colors.borderGradient, sheetShape)
            .clip(sheetShape),
        shape = sheetShape,
        color = Color(0xF0181818) // ~94% dark glass
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Drag grab handle
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0x55FFFFFF))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

/**
 * 15. DokanToast: Floating glass pill notification banner.
 */
@Composable
fun DokanToast(
    message: String,
    visible: Boolean,
    modifier: Modifier = Modifier,
    isSuccess: Boolean = true
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
        modifier = modifier
    ) {
        val shape = RoundedCornerShape(24.dp)
        val accentColor = if (isSuccess) DokanVividGreen else DokanAmber

        Surface(
            modifier = Modifier
                .shadow(12.dp, shape, ambientColor = accentColor.copy(alpha = 0.3f))
                .border(1.dp, accentColor.copy(alpha = 0.6f), shape)
                .clip(shape),
            shape = shape,
            color = Color(0xF21C1C1C)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF0A1805),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}
