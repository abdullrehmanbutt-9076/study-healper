package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AnalyticsData
import com.example.ui.theme.Amber40
import com.example.ui.theme.Cyan40
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface1
import com.example.ui.theme.DarkSurface2
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Emerald40
import com.example.ui.theme.Indigo60
import com.example.ui.theme.Rose40

@Composable
fun AnalyticsScreen(
    analyticsData: AnalyticsData,
    onResolveWeakness: (Long) -> Unit,
    onDrillWeakness: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("analytics_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "AI-Driven Analytics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Concept mastery radar & predictive readiness",
                        fontSize = 12.sp,
                        color = Emerald40
                    )
                }
            }
        }

        // Predictive Exam Readiness Gauge Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("exam_readiness_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, Emerald40.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gauge circle
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Emerald40.copy(alpha = 0.15f))
                            .border(3.dp, Emerald40, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${analyticsData.examReadinessScore}%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Emerald40
                            )
                            Text(
                                text = "Ready",
                                fontSize = 10.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Predictive Exam Readiness",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Based on Board past paper rubrics, quiz accuracy (${analyticsData.overallAccuracy}%), and syllabus retention.",
                            fontSize = 12.sp,
                            color = DarkTextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // AI Weakness Detection Section (Stumbling Blocks)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_weaknesses_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Amber40, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Detected Weaknesses",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Amber40.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${analyticsData.aiDetectedWeaknesses.size} Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber40
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (analyticsData.aiDetectedWeaknesses.isEmpty()) {
                        Text(
                            text = "Great job! No critical concept weaknesses detected right now.",
                            fontSize = 13.sp,
                            color = Emerald40
                        )
                    } else {
                        analyticsData.aiDetectedWeaknesses.forEach { weakness ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurface2)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = weakness.concept,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DarkTextPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(if (weakness.severity == "High") Rose40.copy(alpha = 0.2f) else Amber40.copy(alpha = 0.2f))
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = weakness.severity,
                                                    fontSize = 10.sp,
                                                    color = if (weakness.severity == "High") Rose40 else Amber40
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${weakness.subject} • Missed ${weakness.mistakeCount} times in quizzes",
                                            fontSize = 11.sp,
                                            color = DarkTextSecondary
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        // Drill Button
                                        Button(
                                            onClick = { onDrillWeakness(weakness.concept) },
                                            modifier = Modifier.height(36.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Cyan40)
                                        ) {
                                            Text("Drill", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        // Mark Resolved
                                        IconButton(
                                            onClick = { onResolveWeakness(weakness.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(Icons.Default.CheckCircleOutline, contentDescription = "Resolved", tint = Emerald40)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Subject Concept Mastery Breakdown
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("subject_mastery_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.QueryStats, contentDescription = null, tint = Cyan40, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Concept Mastery Radar",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    analyticsData.subjectMastery.forEach { subject ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = subject.subject,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkTextPrimary
                                )
                                Text(
                                    text = "${subject.percentage}% (${subject.questionsSolved} solved)",
                                    fontSize = 12.sp,
                                    color = if (subject.percentage >= 85) Emerald40 else Amber40
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { subject.percentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (subject.percentage >= 85) Emerald40 else Indigo60,
                                trackColor = DarkSurface2,
                            )
                        }
                    }
                }
            }
        }

        // Ebbinghaus Retention Curve & Spaced Repetition
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Insights, contentDescription = null, tint = Indigo60, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Memory Retention Decay (Ebbinghaus)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Current retention stability: ${analyticsData.retentionRetentionRate}%. Reviews scheduled automatically before concept decay drops below 70%.",
                        fontSize = 12.sp,
                        color = DarkTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        analyticsData.weeklyStudyHours.forEach { (day, hours) ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(70.dp)
                                        .width(16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DarkSurface2),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height((hours * 15).dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Cyan40)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = day, fontSize = 11.sp, color = DarkTextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // AI Personalized Recommendations
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Amber40, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Personalized AI Action Plan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    analyticsData.aiRecommendations.forEach { rec ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "•", fontSize = 14.sp, color = Amber40, modifier = Modifier.padding(end = 8.dp))
                            Text(text = rec, fontSize = 12.sp, color = DarkTextPrimary, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
