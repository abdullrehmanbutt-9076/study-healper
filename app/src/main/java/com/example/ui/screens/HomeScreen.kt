package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.CloudSyncInfo
import com.example.domain.model.SyncState
import com.example.domain.model.UserProfile
import com.example.ui.theme.Amber40
import com.example.ui.theme.Cyan40
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface1
import com.example.ui.theme.DarkSurface2
import com.example.ui.theme.DarkTextMuted
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Emerald40
import com.example.ui.theme.Indigo40
import com.example.ui.theme.Indigo60
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    cloudSyncInfo: CloudSyncInfo,
    onNavigate: (Screen) -> Unit,
    onQuickExplain: (topic: String, minutes: Int) -> Unit,
    onToggleLanguage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var quickTopicInput by remember { mutableStateOf("Newton's laws of motion") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar: Profile status, Board chip, Streak, Language & Cloud Sync
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Indigo60.copy(alpha = 0.2f))
                            .border(1.dp, Indigo60, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Talib AI",
                            tint = Cyan40,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Talib AI",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Indigo40.copy(alpha = 0.3f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = userProfile.gradeBoard,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Indigo60
                                )
                            }
                        }
                        Text(
                            text = "Bilingual Study Companion",
                            fontSize = 12.sp,
                            color = DarkTextSecondary
                        )
                    }
                }

                // Badges row: Streak & Cloud Sync Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Streak Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Amber40.copy(alpha = 0.15f))
                            .border(1.dp, Amber40.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔥", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.streakDays}d",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber40
                        )
                    }

                    // Cloud Sync Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                when (cloudSyncInfo.status) {
                                    SyncState.SYNCING -> Cyan40.copy(alpha = 0.2f)
                                    SyncState.ERROR -> Color.Red.copy(alpha = 0.2f)
                                    else -> Emerald40.copy(alpha = 0.15f)
                                }
                            )
                            .border(
                                1.dp,
                                if (cloudSyncInfo.pendingChangesCount > 0) Amber40 else Emerald40,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onNavigate(Screen.CLOUD_SYNC) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("home_cloud_sync_pill"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                cloudSyncInfo.status == SyncState.SYNCING -> Icons.Default.CloudSync
                                cloudSyncInfo.pendingChangesCount > 0 -> Icons.Default.SyncProblem
                                else -> Icons.Default.CloudDone
                            },
                            contentDescription = "Sync status",
                            tint = if (cloudSyncInfo.pendingChangesCount > 0) Amber40 else Emerald40,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (cloudSyncInfo.pendingChangesCount > 0) "${cloudSyncInfo.pendingChangesCount} pend" else "Cloud",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (cloudSyncInfo.pendingChangesCount > 0) Amber40 else Emerald40
                        )
                    }

                    // Language Switch Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface2)
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                val nextLang = if (userProfile.languagePreference == "English") "Urdu" else "English"
                                onToggleLanguage(nextLang)
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("lang_toggle_pill")
                    ) {
                        Text(
                            text = if (userProfile.languagePreference == "English") "EN" else "اردو",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Cyan40
                        )
                    }
                }
            }
        }

        // Hero Study Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_study),
                        contentDescription = "Study Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        DarkSurface1.copy(alpha = 0.85f),
                                        DarkSurface1
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Cyan40,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tutor, not answer-dispenser",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Cyan40
                            )
                        }
                        Text(
                            text = "Learn concepts step-by-step with progressive hints",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }
                }
            }
        }

        // Signature Feature: "Explain X in Y minutes" Quick Launcher
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("explain_signature_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface2),
                border = androidx.compose.foundation.BorderStroke(1.dp, Indigo60.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚡", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Signature: Explain X in 5 Minutes",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                        }
                        Text(
                            text = "Interactive Pipeline",
                            fontSize = 11.sp,
                            color = Cyan40
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "e.g. 'Mujhe Newton's laws 5 minutes mein samjhao'",
                        fontSize = 12.sp,
                        color = DarkTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = quickTopicInput,
                            onValueChange = { quickTopicInput = it },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_explain_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo60,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = DarkTextPrimary,
                                unfocusedTextColor = DarkTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onQuickExplain(quickTopicInput, 5)
                            },
                            modifier = Modifier
                                .height(54.dp)
                                .testTag("quick_explain_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo60)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Explain",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Core Capabilities
        item {
            Text(
                text = "Smart Study Hub",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Feature 1: Photo Problem Solver
        item {
            FeatureActionCard(
                title = "Photo Problem Solver",
                badge = "Tutor Mode",
                badgeColor = Cyan40,
                description = "Snap or select STEM questions. Progressive hints first, full solution only when you're ready.",
                icon = Icons.Default.CameraAlt,
                iconColor = Cyan40,
                testTag = "card_photo_solver",
                onClick = { onNavigate(Screen.PHOTO_SOLVER) }
            )
        }

        // Feature 2: Explain in X Minutes
        item {
            FeatureActionCard(
                title = "Explain X in Y Minutes",
                badge = "Signature",
                badgeColor = Amber40,
                description = "Topic breakdown with calibrated milestones, Pakistani real-world examples & instant quiz.",
                icon = Icons.Default.Timer,
                iconColor = Amber40,
                testTag = "card_explain_pipeline",
                onClick = { onNavigate(Screen.EXPLAIN_PIPELINE) }
            )
        }

        // Feature 3: Real-Time Collaboration (Advanced Feature)
        item {
            FeatureActionCard(
                title = "Live Study Rooms",
                badge = "Real-Time Peers",
                badgeColor = Indigo60,
                description = "Collaborative peer problem solving, live study rooms, synchronized quiz battles & AI co-pilot.",
                icon = Icons.Default.Groups,
                iconColor = Indigo60,
                testTag = "card_collaboration",
                onClick = { onNavigate(Screen.COLLABORATION) }
            )
        }

        // Feature 4: AI-Driven Analytics (Advanced Feature)
        item {
            FeatureActionCard(
                title = "AI-Driven Analytics",
                badge = "84% Readiness",
                badgeColor = Emerald40,
                description = "Concept mastery radar, Ebbinghaus retention curve & automated weakness detection.",
                icon = Icons.Default.Analytics,
                iconColor = Emerald40,
                testTag = "card_analytics",
                onClick = { onNavigate(Screen.ANALYTICS) }
            )
        }

        // Secondary Tools Row
        item {
            Text(
                text = "Study Tools & Sync",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notes Button
                QuickToolPill(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "Saved Notes",
                    iconColor = Cyan40,
                    modifier = Modifier.weight(1f),
                    testTag = "tool_notes",
                    onClick = { onNavigate(Screen.STUDY_NOTES) }
                )

                // Pomodoro Focus
                QuickToolPill(
                    icon = Icons.Default.Timer,
                    label = "Pomodoro",
                    iconColor = Amber40,
                    modifier = Modifier.weight(1f),
                    testTag = "tool_pomodoro",
                    onClick = { onNavigate(Screen.POMODORO) }
                )

                // Cloud Sync
                QuickToolPill(
                    icon = Icons.Default.CloudDone,
                    label = "Cloud Sync",
                    iconColor = Emerald40,
                    modifier = Modifier.weight(1f),
                    testTag = "tool_cloud_sync",
                    onClick = { onNavigate(Screen.CLOUD_SYNC) }
                )
            }
        }
    }
}

@Composable
fun FeatureActionCard(
    title: String,
    badge: String,
    badgeColor: Color,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface1),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f))
                    .border(1.dp, iconColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = DarkTextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = DarkTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun QuickToolPill(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface1),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkTextPrimary
            )
        }
    }
}
