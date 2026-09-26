package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.QuizResultDao
import com.example.data.local.dao.StudyNoteDao
import com.example.data.local.dao.StudySessionDao
import com.example.data.local.dao.SyncLogDao
import com.example.data.local.dao.WeaknessDao
import com.example.data.local.entity.QuizResultEntity
import com.example.data.local.entity.StudyNoteEntity
import com.example.data.local.entity.StudySessionEntity
import com.example.data.local.entity.SyncLogEntity
import com.example.data.local.entity.WeaknessEntity

@Database(
    entities = [
        StudyNoteEntity::class,
        QuizResultEntity::class,
        StudySessionEntity::class,
        WeaknessEntity::class,
        SyncLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyNoteDao(): StudyNoteDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun weaknessDao(): WeaknessDao
    abstract fun syncLogDao(): SyncLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "talib_ai_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
