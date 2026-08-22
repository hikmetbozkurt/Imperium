package com.hikmet.imperium.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object DatabaseMigrations {
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `quiz_attempts` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `categoryId` TEXT NOT NULL,
                    `levelNumber` INTEGER NOT NULL,
                    `score` INTEGER NOT NULL,
                    `stars` INTEGER NOT NULL,
                    `correctAnswers` INTEGER NOT NULL,
                    `totalQuestions` INTEGER NOT NULL,
                    `durationMs` INTEGER NOT NULL,
                    `completedAtEpochMs` INTEGER NOT NULL
                )
                """.trimIndent(),
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_quiz_attempts_categoryId` " +
                    "ON `quiz_attempts` (`categoryId`)",
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_quiz_attempts_categoryId_levelNumber` " +
                    "ON `quiz_attempts` (`categoryId`, `levelNumber`)",
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_quiz_attempts_completedAtEpochMs` " +
                    "ON `quiz_attempts` (`completedAtEpochMs`)",
            )
        }
    }
}
