package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CloudSyncScreen
import com.example.ui.screens.CollaborationScreen
import com.example.ui.screens.ExplainPipelineScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PhotoSolverScreen
import com.example.ui.screens.PomodoroScreen
import com.example.ui.screens.StudyNotesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.TalibViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                TalibApp()
            }
        }
    }
}

@Composable
fun TalibApp(
    viewModel: TalibViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val cloudSyncInfo by viewModel.cloudSyncInfo.collectAsStateWithLifecycle()
    val currentPhotoProblem by viewModel.currentPhotoProblem.collectAsStateWithLifecycle()
    val hintLevel by viewModel.hintRevealLevel.collectAsStateWithLifecycle()
    val isPhotoSolving by viewModel.isPhotoSolving.collectAsStateWithLifecycle()
    val isAudioSpeaking by viewModel.isAudioSpeaking.collectAsStateWithLifecycle()
    val pipelineResult by viewModel.pipelineResult.collectAsStateWithLifecycle()
    val isPipelineLoading by viewModel.isPipelineLoading.collectAsStateWithLifecycle()
    val quizSelectedAnswers by viewModel.quizSelectedAnswers.collectAsStateWithLifecycle()
    val quizSubmitted by viewModel.quizSubmitted.collectAsStateWithLifecycle()
    val analyticsData by viewModel.analyticsData.collectAsStateWithLifecycle()
    val studyRooms by viewModel.studyRooms.collectAsStateWithLifecycle()
    val selectedRoom by viewModel.selectedRoom.collectAsStateWithLifecycle()
    val roomMessages by viewModel.activeRoomMessages.collectAsStateWithLifecycle()
    val notes by viewModel.allNotes.collectAsStateWithLifecycle()
    val pomodoroSeconds by viewModel.pomodoroSecondsRemaining.collectAsStateWithLifecycle()
    val isPomodoroRunning by viewModel.isPomodoroRunning.collectAsStateWithLifecycle()
    val pomodoroMode by viewModel.pomodoroMode.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        when (currentScreen) {
            Screen.AUTH -> {
                AuthScreen(
                    onLoginSuccess = { phone, grade, language ->
                        viewModel.login(phone, grade, language)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.HOME -> {
                HomeScreen(
                    userProfile = userProfile,
                    cloudSyncInfo = cloudSyncInfo,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onQuickExplain = { topic, minutes ->
                        viewModel.runExplainPipeline(topic, minutes)
                        viewModel.navigateTo(Screen.EXPLAIN_PIPELINE)
                    },
                    onToggleLanguage = { lang ->
                        viewModel.setLanguage(lang)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.PHOTO_SOLVER -> {
                PhotoSolverScreen(
                    currentProblem = currentPhotoProblem,
                    hintLevel = hintLevel,
                    isLoading = isPhotoSolving,
                    isAudioPlaying = isAudioSpeaking,
                    onSolve = { bitmap, text, subject ->
                        viewModel.solveProblem(bitmap, text, subject)
                    },
                    onRevealNextHint = {
                        viewModel.revealNextHint()
                    },
                    onToggleAudio = {
                        viewModel.toggleAudioExplanation()
                    },
                    onSaveToNotes = {
                        viewModel.saveCurrentProblemToNotes()
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.EXPLAIN_PIPELINE -> {
                ExplainPipelineScreen(
                    pipelineResult = pipelineResult,
                    isLoading = isPipelineLoading,
                    selectedAnswers = quizSelectedAnswers,
                    isQuizSubmitted = quizSubmitted,
                    onRunPipeline = { topic, mins ->
                        viewModel.runExplainPipeline(topic, mins)
                    },
                    onSelectOption = { qIdx, optIdx ->
                        viewModel.selectQuizOption(qIdx, optIdx)
                    },
                    onSubmitQuiz = {
                        viewModel.submitQuiz()
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.ANALYTICS -> {
                AnalyticsScreen(
                    analyticsData = analyticsData,
                    onResolveWeakness = { id ->
                        viewModel.resolveWeakness(id)
                    },
                    onDrillWeakness = { concept ->
                        viewModel.runExplainPipeline(concept, 5)
                        viewModel.navigateTo(Screen.EXPLAIN_PIPELINE)
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.COLLABORATION -> {
                CollaborationScreen(
                    studyRooms = studyRooms,
                    selectedRoom = selectedRoom,
                    roomMessages = roomMessages,
                    onJoinRoom = { room ->
                        viewModel.joinRoom(room)
                    },
                    onSendMessage = { text ->
                        viewModel.sendRoomMessage(text)
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.CLOUD_SYNC -> {
                CloudSyncScreen(
                    syncInfo = cloudSyncInfo,
                    onTriggerSync = {
                        viewModel.triggerCloudSync()
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.STUDY_NOTES -> {
                StudyNotesScreen(
                    notes = notes,
                    onSaveNote = { title, topic, subject, summary, keyPoints, formulas ->
                        viewModel.saveCustomNote(title, topic, subject, summary, keyPoints, formulas)
                    },
                    onDeleteNote = { id ->
                        viewModel.deleteNote(id)
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            Screen.POMODORO -> {
                PomodoroScreen(
                    secondsRemaining = pomodoroSeconds,
                    isRunning = isPomodoroRunning,
                    currentMode = pomodoroMode,
                    onToggle = {
                        viewModel.togglePomodoro()
                    },
                    onReset = { minutes, mode ->
                        viewModel.resetPomodoro(minutes, mode)
                    },
                    onBack = {
                        viewModel.handleBack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
