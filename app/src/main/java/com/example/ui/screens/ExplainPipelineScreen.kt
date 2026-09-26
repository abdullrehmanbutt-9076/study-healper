package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ExplainPipelineResult
import com.example.ui.theme.Amber40
import com.example.ui.theme.Cyan40
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface1
import com.example.ui.theme.DarkSurface2
import com.example.ui.theme.DarkTextMuted
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Emerald40
import com.example.ui.theme.Indigo60
import com.example.ui.theme.Rose40

@Composable
fun ExplainPipelineScreen(
    pipelineResult: ExplainPipelineResult?,
    isLoading: Boolean,
    selectedAnswers: Map<Int, Int>,
    isQuizSubmitted: Boolean,
    onRunPipeline: (topic: String, minutes: Int) -> Unit,
    onSelectOption: (questionIndex: Int, optionIndex: Int) -> Unit,
    onSubmitQuiz: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var topicInput by remember { mutableStateOf("Newton's laws of motion") }
    var durationMinutes by remember { mutableIntStateOf(5) }

    val suggestedTopics = listOf(
        "Newton's Laws",
        "Bernoulli's Principle",
        "Integration by Parts",
        "Benzene Electrophilic Reactions",
        "Photosynthesis Light Reactions"
    )

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
                    modifier = Modifier.testTag("explain_back_button")
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
                        text = "Explain in X Minutes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Calibrated time breakdown & interactive quiz",
                        fontSize = 12.sp,
                        color = Amber40
                    )
                }
            }
        }

        // Topic & Duration Configuration Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "What concept do you want explained?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pipeline_topic_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Amber40,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary
                        ),
                        placeholder = { Text("e.g. Newton's laws of motion", color = DarkTextMuted) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Suggested chips
                    Text(text = "Popular Topics:", fontSize = 11.sp, color = DarkTextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        suggestedTopics.take(3).forEach { topic ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurface2)
                                    .border(1.dp, DarkBorder, RoundedCornerShape(8.dp))
                                    .clickable { topicInput = topic }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = topic, fontSize = 11.sp, color = DarkTextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Duration selector
                    Text(
                        text = "Target Duration:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(2 to "2 Min (Flash)", 5 to "5 Min (Standard)", 10 to "10 Min (Deep)").forEach { (mins, label) ->
                            val isSelected = durationMinutes == mins
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Amber40.copy(alpha = 0.2f) else DarkSurface2)
                                    .border(
                                        1.dp,
                                        if (isSelected) Amber40 else DarkBorder,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { durationMinutes = mins }
                                    .padding(vertical = 10.dp)
                                    .testTag("duration_btn_$mins"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Amber40 else DarkTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onRunPipeline(topicInput, durationMinutes) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("run_pipeline_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber40)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                        } else {
                            Icon(Icons.Default.HourglassTop, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Explain in $durationMinutes Minutes",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Pipeline Results
        if (pipelineResult != null) {
            // Part 1: High-yield summary & Time Milestones
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Amber40, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Part 1: Timed Concept Breakdown",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = pipelineResult.keyConceptSummary,
                            fontSize = 13.sp,
                            color = DarkTextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Milestones list
                        pipelineResult.timeMilestones.forEach { milestone ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurface2)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = milestone.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Cyan40
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Amber40.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = milestone.timeRange,
                                                fontSize = 11.sp,
                                                color = Amber40,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = milestone.description,
                                        fontSize = 12.sp,
                                        color = DarkTextPrimary,
                                        lineHeight = 16.sp
                                    )
                                    if (milestone.keyEquation != null) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Equation: ${milestone.keyEquation}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Emerald40
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Part 2: Concrete Relatable Examples (Pakistani & Universal contexts)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TipsAndUpdates, contentDescription = null, tint = Cyan40, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Part 2: Real-World Relatable Examples",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        pipelineResult.realWorldExamples.forEach { ex ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Indigo60.copy(alpha = 0.1f))
                                    .border(1.dp, Indigo60.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "🏏 ${ex.title}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Indigo60
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = ex.context,
                                        fontSize = 12.sp,
                                        color = DarkTextPrimary,
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Takeaway: ${ex.takeaway}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Cyan40
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Part 3: Interactive Practice Quiz Component
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("interactive_quiz_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Cyan40.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Quiz, contentDescription = null, tint = Emerald40, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Part 3: Interactive Quiz",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                            }
                            if (isQuizSubmitted) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Emerald40.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text("Evaluated", color = Emerald40, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        pipelineResult.interactiveQuiz.forEachIndexed { qIndex, question ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Q${qIndex + 1}. ${question.question}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                question.options.forEachIndexed { optIndex, optionText ->
                                    val isSelected = selectedAnswers[qIndex] == optIndex
                                    val isCorrect = question.correctIndex == optIndex

                                    val optionBorder = when {
                                        isQuizSubmitted && isCorrect -> Emerald40
                                        isQuizSubmitted && isSelected && !isCorrect -> Rose40
                                        isSelected -> Cyan40
                                        else -> DarkBorder
                                    }

                                    val optionBg = when {
                                        isQuizSubmitted && isCorrect -> Emerald40.copy(alpha = 0.15f)
                                        isQuizSubmitted && isSelected && !isCorrect -> Rose40.copy(alpha = 0.15f)
                                        isSelected -> Cyan40.copy(alpha = 0.15f)
                                        else -> DarkSurface2
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(optionBg)
                                            .border(1.dp, optionBorder, RoundedCornerShape(10.dp))
                                            .clickable(enabled = !isQuizSubmitted) {
                                                onSelectOption(qIndex, optIndex)
                                            }
                                            .padding(12.dp)
                                            .testTag("quiz_q${qIndex}_opt$optIndex"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .border(1.dp, optionBorder, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = ('A' + optIndex).toString(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Cyan40 else DarkTextSecondary
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = optionText,
                                            fontSize = 12.sp,
                                            color = DarkTextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isQuizSubmitted) {
                                            if (isCorrect) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Emerald40, modifier = Modifier.size(16.dp))
                                            } else if (isSelected) {
                                                Icon(Icons.Default.Close, contentDescription = "Wrong", tint = Rose40, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }

                                if (isQuizSubmitted) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Explanation: ${question.explanation}",
                                        fontSize = 11.sp,
                                        color = DarkTextSecondary,
                                        lineHeight = 15.sp,
                                        modifier = Modifier.padding(start = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (!isQuizSubmitted) {
                            Button(
                                onClick = onSubmitQuiz,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("submit_quiz_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Cyan40)
                            ) {
                                Text(
                                    text = "Submit Answers & Record Mastery",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald40, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Quiz recorded in local Room DB & AI Analytics updated!",
                                    fontSize = 12.sp,
                                    color = Emerald40,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
