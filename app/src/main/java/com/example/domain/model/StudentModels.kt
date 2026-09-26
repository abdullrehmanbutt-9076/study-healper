package com.example.domain.model

data class UserProfile(
    val phoneNumber: String = "0300 1234567",
    val gradeBoard: String = "FSc", // Matric, O-Level, FSc, A-Level, University
    val languagePreference: String = "English", // English, Roman Urdu, Urdu Script (اردو)
    val streakDays: Int = 7,
    val solvesRemaining: Int = 10,
    val isPremium: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isCloudSyncEnabled: Boolean = true,
    val cloudToken: String = "enc_sec_993f41a8",
    val studentName: String = "Talib Student"
)

data class PhotoProblem(
    val id: String,
    val subject: String,
    val topic: String,
    val difficulty: String, // Easy, Medium, Hard, Board Exam
    val questionText: String,
    val questionTextUrdu: String = "",
    val coreConceptHint: String,
    val guidedStep1: String,
    val guidedStep2: String,
    val fullSolution: String,
    val finalAnswer: String,
    val formulasUsed: List<String> = emptyList(),
    val romanUrduExplanation: String = "",
    val nativeUrduExplanation: String = ""
)

data class ExplainPipelineResult(
    val topic: String,
    val durationMinutes: Int,
    val gradeLevel: String,
    val language: String,
    val keyConceptSummary: String,
    val timeMilestones: List<TimeMilestone>,
    val realWorldExamples: List<RealWorldExample>,
    val interactiveQuiz: List<QuizQuestion>
)

data class TimeMilestone(
    val timeRange: String, // e.g. "0:00 - 1:30"
    val title: String,
    val description: String,
    val keyEquation: String? = null
)

data class RealWorldExample(
    val title: String,
    val context: String, // e.g. "Cricket fast bowler swing", "Lahore Metro braking system"
    val takeaway: String
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val conceptTag: String
)

data class AnalyticsData(
    val examReadinessScore: Int = 84, // 0 - 100%
    val retentionRetentionRate: Int = 78,
    val totalQuestionsAnswered: Int = 142,
    val overallAccuracy: Int = 82,
    val subjectMastery: List<SubjectMastery> = listOf(
        SubjectMastery("Physics", 88, 45),
        SubjectMastery("Mathematics", 76, 52),
        SubjectMastery("Chemistry", 91, 30),
        SubjectMastery("Biology", 72, 24),
        SubjectMastery("Computer Science", 85, 18)
    ),
    val aiDetectedWeaknesses: List<WeaknessItem> = listOf(
        WeaknessItem(1, "Physics", "Projectile Motion with air resistance", "High", 3),
        WeaknessItem(2, "Mathematics", "Integration by parts constants (C)", "Medium", 2),
        WeaknessItem(3, "Chemistry", "Electrophilic substitution in benzene", "High", 4)
    ),
    val aiRecommendations: List<String> = listOf(
        "Focus on 2D kinematics projectile components — you missed 2 questions in the last quiz.",
        "Your retention for Organic Chemistry is at 91%. Great retention curve!",
        "Recommended: 5-minute drill on Integration by parts before your weekend test."
    ),
    val weeklyStudyHours: List<Pair<String, Float>> = listOf(
        Pair("Mon", 1.8f),
        Pair("Tue", 2.4f),
        Pair("Wed", 3.1f),
        Pair("Thu", 2.0f),
        Pair("Fri", 3.5f),
        Pair("Sat", 4.2f),
        Pair("Sun", 2.8f)
    )
)

data class SubjectMastery(
    val subject: String,
    val percentage: Int,
    val questionsSolved: Int
)

data class WeaknessItem(
    val id: Long,
    val subject: String,
    val concept: String,
    val severity: String,
    val mistakeCount: Int
)

data class StudyRoom(
    val id: String,
    val name: String,
    val subject: String,
    val gradeLevel: String,
    val hostName: String,
    val activeMemberCount: Int,
    val members: List<PeerUser>,
    val currentQuestion: String,
    val isLiveQuizActive: Boolean = false,
    val tags: List<String> = listOf("Active Discussion", "Exam Prep")
)

data class PeerUser(
    val id: String,
    val name: String,
    val status: String, // "Solving", "Viewing Hint", "In Discussion", "Ready"
    val avatarInitial: String,
    val colorHex: Long
)

data class RoomChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isAiAssistant: Boolean = false,
    val isHighlightedFormula: Boolean = false
)

enum class SyncState {
    IDLE,
    SYNCING,
    SUCCESS,
    ERROR
}

data class CloudSyncInfo(
    val status: SyncState = SyncState.IDLE,
    val lastSyncedTime: String = "Just now",
    val pendingChangesCount: Int = 0,
    val encryptionStandard: String = "AES-256 GCM (E2E Encrypted)",
    val cloudToken: String = "SHA256:7a9f...e43c",
    val totalSyncedItems: Int = 48,
    val isCloudConnected: Boolean = true
)
