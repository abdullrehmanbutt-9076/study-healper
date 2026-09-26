package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.QuizResultEntity
import com.example.data.local.entity.StudyNoteEntity
import com.example.data.local.entity.StudySessionEntity
import com.example.data.local.entity.SyncLogEntity
import com.example.data.local.entity.WeaknessEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyNoteDao {
    @Query("SELECT * FROM study_notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE isSynced = 0")
    suspend fun getUnsyncedNotes(): List<StudyNoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity): Long

    @Update
    suspend fun updateNote(note: StudyNoteEntity)

    @Query("DELETE FROM study_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("UPDATE study_notes SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAllAsSynced()
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY completedAt DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE isSynced = 0")
    suspend fun getUnsyncedResults(): List<QuizResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity): Long

    @Query("UPDATE quiz_results SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAllAsSynced()
}

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE isSynced = 0")
    suspend fun getUnsyncedSessions(): List<StudySessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Query("UPDATE study_sessions SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAllAsSynced()
}

@Dao
interface WeaknessDao {
    @Query("SELECT * FROM concept_weaknesses WHERE isResolved = 0 ORDER BY severityLevel DESC, detectedCount DESC")
    fun getActiveWeaknesses(): Flow<List<WeaknessEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeakness(weakness: WeaknessEntity): Long

    @Query("UPDATE concept_weaknesses SET isResolved = 1 WHERE id = :id")
    suspend fun resolveWeakness(id: Long)
}

@Dao
interface SyncLogDao {
    @Query("SELECT * FROM sync_logs ORDER BY timestamp DESC LIMIT 20")
    fun getRecentLogs(): Flow<List<SyncLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SyncLogEntity): Long
}
