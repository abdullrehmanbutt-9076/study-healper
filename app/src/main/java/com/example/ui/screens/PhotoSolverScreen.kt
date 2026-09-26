package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.PhotoProblem
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

@Composable
fun PhotoSolverScreen(
    currentProblem: PhotoProblem?,
    hintLevel: Int,
    isLoading: Boolean,
    isAudioPlaying: Boolean,
    onSolve: (bitmap: Bitmap?, text: String, subject: String) -> Unit,
    onRevealNextHint: () -> Unit,
    onToggleAudio: () -> Unit,
    onSaveToNotes: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var selectedSubject by remember { mutableStateOf("Physics") }
    var questionInput by remember {
        mutableStateOf("A cricket ball is struck at 20 m/s at 30° angle. Find maximum height.")
    }
    var selectedLanguageTab by remember { mutableStateOf("English") } // "English", "Roman Urdu", "اردو"
    var saveSuccessMessage by remember { mutableStateOf(false) }

    val subjects = listOf("Physics", "Mathematics", "Chemistry", "Biology")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                onSolve(bitmap, questionInput, selectedSubject)
            } catch (e: Exception) {
                onSolve(null, questionInput, selectedSubject)
            }
        }
    }

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
                    modifier = Modifier.testTag("photo_back_button")
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
                        text = "Photo Problem Solver",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Tutor mode: Progressive hints first",
                        fontSize = 12.sp,
                        color = Cyan40
                    )
                }
            }
        }

        // Subject selector chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subject ->
                    val isSelected = selectedSubject == subject
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Indigo60 else DarkSurface1)
                            .border(1.dp, if (isSelected) Indigo60 else DarkBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                selectedSubject = subject
                                when (subject) {
                                    "Physics" -> questionInput = "A cricket ball is struck at 20 m/s at 30° angle. Find maximum height."
                                    "Mathematics" -> questionInput = "Evaluate the definite integral: ∫ (2x · e^(x²)) dx from 0 to 1."
                                    "Chemistry" -> questionInput = "What is the electrophile in the nitration of benzene, and what catalyst generates it?"
                                    "Biology" -> questionInput = "Explain the difference between homozygous and heterozygous genotypes with a Punnett square."
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("subject_chip_$subject"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subject.take(4),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else DarkTextSecondary
                        )
                    }
                }
            }
        }

        // Question Input Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter Question or Pick Textbook Image",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = questionInput,
                        onValueChange = { questionInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("photo_question_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Cyan40,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary
                        ),
                        placeholder = { Text("Type question or snap a photo...", color = DarkTextMuted) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pick Photo
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("pick_photo_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Cyan40)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Image", fontSize = 13.sp)
                        }

                        // Solve Now Button
                        Button(
                            onClick = {
                                onSolve(null, questionInput, selectedSubject)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("solve_problem_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Cyan40)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analyze", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Result & Progressive Revelation Ladder
        if (currentProblem != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("progressive_hint_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Cyan40.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Step Progress Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tutor Ladder: Step $hintLevel of 3",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Cyan40
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurface2)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentProblem.difficulty,
                                    fontSize = 11.sp,
                                    color = Amber40,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { hintLevel / 3f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Cyan40,
                            trackColor = DarkSurface2,
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Language Tabs for explanation
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurface2)
                                .padding(2.dp)
                        ) {
                            listOf("English", "Roman Urdu", "اردو").forEach { lang ->
                                val isSelected = selectedLanguageTab == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Indigo60 else Color.Transparent)
                                        .clickable { selectedLanguageTab = lang }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = lang,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else DarkTextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Beat 1: Core Concept Recall Hint (Always visible once analyzed)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Indigo60.copy(alpha = 0.12f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Indigo60.copy(alpha = 0.3f))
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                                Icon(
                                    Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = Amber40,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Beat 1: Concept Recall Hint",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Amber40
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentProblem.coreConceptHint,
                                        fontSize = 13.sp,
                                        color = DarkTextPrimary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        // Beat 2: Guided Steps & Formulas
                        AnimatedVisibility(visible = hintLevel >= 2) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface2)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Beat 2: Guided Steps & Equations",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Cyan40
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = currentProblem.guidedStep1,
                                            fontSize = 13.sp,
                                            color = DarkTextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentProblem.guidedStep2,
                                            fontSize = 13.sp,
                                            color = DarkTextPrimary
                                        )

                                        if (currentProblem.formulasUsed.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                currentProblem.formulasUsed.forEach { formula ->
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(Cyan40.copy(alpha = 0.15f))
                                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                                    ) {
                                                        Text(
                                                            text = formula,
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
                            }
                        }

                        // Beat 3: Complete Solution & Final Answer
                        AnimatedVisibility(visible = hintLevel >= 3) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Emerald40.copy(alpha = 0.12f)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Emerald40.copy(alpha = 0.3f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Beat 3: Verified Solution & Final Answer",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Emerald40
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))

                                        val contentText = when (selectedLanguageTab) {
                                            "Roman Urdu" -> currentProblem.romanUrduExplanation
                                            "اردو" -> currentProblem.nativeUrduExplanation.ifBlank { currentProblem.romanUrduExplanation }
                                            else -> currentProblem.fullSolution
                                        }

                                        Text(
                                            text = contentText,
                                            fontSize = 13.sp,
                                            color = DarkTextPrimary,
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Emerald40.copy(alpha = 0.2f))
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "Final: ${currentProblem.finalAnswer}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Emerald40
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: Next Hint vs Save Notes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (hintLevel < 3) {
                                Button(
                                    onClick = onRevealNextHint,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("reveal_next_hint_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Indigo60)
                                ) {
                                    Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (hintLevel == 1) "Show Step 2 Hint" else "Show Full Solution",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        onSaveToNotes()
                                        saveSuccessMessage = true
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("save_to_notes_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Emerald40)
                                ) {
                                    Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save to Notes", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }

                            // Voice Audio toggle
                            OutlinedButton(
                                onClick = onToggleAudio,
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("audio_toggle_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isAudioPlaying) Cyan40 else DarkBorder)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Read Audio",
                                    tint = if (isAudioPlaying) Cyan40 else DarkTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        if (saveSuccessMessage) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald40, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Saved to your offline notes & queued for cloud sync!", fontSize = 11.sp, color = Emerald40)
                            }
                        }
                    }
                }
            }
        }
    }
}
