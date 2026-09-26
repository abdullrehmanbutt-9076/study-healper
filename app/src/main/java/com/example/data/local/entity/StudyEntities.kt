package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val topic: String,
    val subject: String,
    val language: String, // "English" or "Urdu"
    val summary: String,
    val keyPointsJson: String, // List of key points as simple JSON or delimited
    val formulasJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val syncHash: String = ""
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val subject: String,
    val scorePercentage: Int,
    val totalQuestions: Int,
    val correctCount: Int,
    val weakConcepts: String, // Comma separated weak points
    val completedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val topic: String,
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val completed: Boolean = true,
    val isSynced: Boolean = false
)

@Entity(tableName = "concept_weaknesses")
data class WeaknessEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val conceptName: String,
    val severityLevel: Int, // 1: Low, 2: Moderate, 3: Critical
    val detectedCount: Int = 1,
    val lastPracticedAt: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

@Entity(tableName = "sync_logs")
data class SyncLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val status: String, // "SUCCESS", "PENDING", "FAILED"
    val recordsPushed: Int,
    val recordsPulled: Int,
    val encryptionFingerprint: String,
    val timestamp: Long = System.currentTimeMillis()
)
