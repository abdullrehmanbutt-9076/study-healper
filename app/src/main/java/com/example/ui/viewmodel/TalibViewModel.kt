package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.StudyNoteEntity
import com.example.data.repository.TalibRepository
import com.example.domain.model.AnalyticsData
import com.example.domain.model.CloudSyncInfo
import com.example.domain.model.ExplainPipelineResult
import com.example.domain.model.PhotoProblem
import com.example.domain.model.RoomChatMessage
import com.example.domain.model.StudyRoom
import com.example.domain.model.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    AUTH,
    HOME,
    PHOTO_SOLVER,
    EXPLAIN_PIPELINE,
    ANALYTICS,
    COLLABORATION,
    CLOUD_SYNC,
    STUDY_NOTES,
    POMODORO
}

class TalibViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TalibRepository(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val cloudSyncInfo: StateFlow<CloudSyncInfo> = repository.cloudSyncInfo
    val studyRooms: StateFlow<List<StudyRoom>> = repository.studyRooms
    val activeRoomMessages: StateFlow<Map<String, List<RoomChatMessage>>> = repository.activeRoomMessages

    val allNotes: StateFlow<List<StudyNoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(Screen.AUTH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _analyticsData = MutableStateFlow(AnalyticsData())
    val analyticsData: StateFlow<AnalyticsData> = _analyticsData.asStateFlow()

    // Photo solver state
    private val _currentPhotoProblem = MutableStateFlow<PhotoProblem?>(null)
    val currentPhotoProblem: StateFlow<PhotoProblem?> = _currentPhotoProblem.asStateFlow()

    private val _hintRevealLevel = MutableStateFlow(0) // 0: Question only, 1: Core Concept hint, 2: Guided Steps, 3: Full solution
    val hintRevealLevel: StateFlow<Int> = _hintRevealLevel.asStateFlow()

    private val _isPhotoSolving = MutableStateFlow(false)
    val isPhotoSolving: StateFlow<Boolean> = _isPhotoSolving.asStateFlow()

    private val _isAudioSpeaking = MutableStateFlow(false)
    val isAudioSpeaking: StateFlow<Boolean> = _isAudioSpeaking.asStateFlow()

    // "Explain in X Minutes" pipeline state
    private val _pipelineResult = MutableStateFlow<ExplainPipelineResult?>(null)
    val pipelineResult: StateFlow<ExplainPipelineResult?> = _pipelineResult.asStateFlow()

    private val _isPipelineLoading = MutableStateFlow(false)
    val isPipelineLoading: StateFlow<Boolean> = _isPipelineLoading.asStateFlow()

    private val _quizSelectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val quizSelectedAnswers: StateFlow<Map<Int, Int>> = _quizSelectedAnswers.asStateFlow()

    private val _quizSubmitted = MutableStateFlow(false)
    val quizSubmitted: StateFlow<Boolean> = _quizSubmitted.asStateFlow()

    // Collaboration room state
    private val _selectedRoom = MutableStateFlow<StudyRoom?>(null)
    val selectedRoom: StateFlow<StudyRoom?> = _selectedRoom.asStateFlow()

    // Pomodoro Timer state
    private val _pomodoroSecondsRemaining = MutableStateFlow(25 * 60)
    val pomodoroSecondsRemaining: StateFlow<Int> = _pomodoroSecondsRemaining.asStateFlow()

    private val _isPomodoroRunning = MutableStateFlow(false)
    val isPomodoroRunning: StateFlow<Boolean> = _isPomodoroRunning.asStateFlow()

    private val _pomodoroMode = MutableStateFlow("Focus") // "Focus", "Short Break", "Deep Study"
    val pomodoroMode: StateFlow<String> = _pomodoroMode.asStateFlow()

    private var pomodoroJob: Job? = null

    // Navigation helper
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun handleBack() {
        if (_currentScreen.value != Screen.HOME && _currentScreen.value != Screen.AUTH) {
            _currentScreen.value = Screen.HOME
        }
    }

    // Auth actions
    fun login(phone: String, grade: String, language: String) {
        repository.updateProfile(phone, grade, language)
        _currentScreen.value = Screen.HOME
    }

    fun logout() {
        repository.updateProfile("", "FSc", "English")
        _currentScreen.value = Screen.AUTH
    }

    fun setLanguage(language: String) {
        repository.setLanguagePreference(language)
    }

    // Photo solver
    fun solveProblem(bitmap: Bitmap?, text: String, subject: String) {
        viewModelScope.launch {
            _isPhotoSolving.value = true
            _hintRevealLevel.value = 1 // Start at beat 1: Hint, not full answer
            val problem = repository.solvePhotoProblem(bitmap, text, subject)
            _currentPhotoProblem.value = problem
            _isPhotoSolving.value = false
        }
    }

    fun revealNextHint() {
        if (_hintRevealLevel.value < 3) {
            _hintRevealLevel.value += 1
        }
    }

    fun toggleAudioExplanation() {
        _isAudioSpeaking.value = !_isAudioSpeaking.value
    }

    fun saveCurrentProblemToNotes() {
        val problem = _currentPhotoProblem.value ?: return
        viewModelScope.launch {
            repository.saveNote(
                title = "${problem.subject}: ${problem.topic}",
                topic = problem.topic,
                subject = problem.subject,
                summary = problem.coreConceptHint,
                keyPoints = "${problem.guidedStep1} | ${problem.guidedStep2}",
                formulas = problem.formulasUsed.joinToString(", ")
            )
        }
    }

    // "Explain in X Minutes" pipeline
    fun runExplainPipeline(topic: String, minutes: Int) {
        viewModelScope.launch {
            _isPipelineLoading.value = true
            _quizSelectedAnswers.value = emptyMap()
            _quizSubmitted.value = false
            val result = repository.runExplainPipeline(topic, minutes)
            _pipelineResult.value = result
            _isPipelineLoading.value = false
        }
    }

    fun selectQuizOption(questionIndex: Int, optionIndex: Int) {
        if (!_quizSubmitted.value) {
            val current = _quizSelectedAnswers.value.toMutableMap()
            current[questionIndex] = optionIndex
            _quizSelectedAnswers.value = current
        }
    }

    fun submitQuiz() {
        _quizSubmitted.value = true
        val result = _pipelineResult.value ?: return
        var correct = 0
        val weakConcepts = mutableListOf<String>()

        result.interactiveQuiz.forEachIndexed { index, question ->
            val chosen = _quizSelectedAnswers.value[index]
            if (chosen == question.correctIndex) {
                correct++
            } else {
                weakConcepts.add(question.conceptTag)
            }
        }

        viewModelScope.launch {
            repository.recordQuizScore(
                topic = result.topic,
                subject = "General Science",
                correct = correct,
                total = result.interactiveQuiz.size,
                weakConcepts = weakConcepts.joinToString(", ")
            )
            // Update live analytics
            _analyticsData.value = _analyticsData.value.copy(
                totalQuestionsAnswered = _analyticsData.value.totalQuestionsAnswered + result.interactiveQuiz.size,
                examReadinessScore = minOf(98, _analyticsData.value.examReadinessScore + (if (correct >= 2) 1 else -1))
            )
        }
    }

    // Collaboration room actions
    fun joinRoom(room: StudyRoom) {
        _selectedRoom.value = room
        _currentScreen.value = Screen.COLLABORATION
    }

    fun sendRoomMessage(text: String) {
        val room = _selectedRoom.value ?: return
        val sender = userProfile.value.studentName
        repository.sendRoomMessage(room.id, sender, text)
    }

    // Cloud sync
    fun triggerCloudSync() {
        viewModelScope.launch {
            repository.performCloudSync()
        }
    }

    fun saveCustomNote(title: String, topic: String, subject: String, summary: String, keyPoints: String, formulas: String) {
        viewModelScope.launch {
            repository.saveNote(title, topic, subject, summary, keyPoints, formulas)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun resolveWeakness(id: Long) {
        viewModelScope.launch {
            repository.resolveWeakness(id)
            val updated = _analyticsData.value.aiDetectedWeaknesses.filterNot { it.id == id }
            _analyticsData.value = _analyticsData.value.copy(aiDetectedWeaknesses = updated)
        }
    }

    // Pomodoro logic
    fun togglePomodoro() {
        if (_isPomodoroRunning.value) {
            pomodoroJob?.cancel()
            _isPomodoroRunning.value = false
        } else {
            _isPomodoroRunning.value = true
            pomodoroJob = viewModelScope.launch {
                while (_pomodoroSecondsRemaining.value > 0) {
                    delay(1000)
                    _pomodoroSecondsRemaining.value -= 1
                }
                _isPomodoroRunning.value = false
                repository.recordStudySession("General Study", "Pomodoro Focus Block", 25)
            }
        }
    }

    fun resetPomodoro(minutes: Int = 25, mode: String = "Focus") {
        pomodoroJob?.cancel()
        _isPomodoroRunning.value = false
        _pomodoroMode.value = mode
        _pomodoroSecondsRemaining.value = minutes * 60
    }
}
