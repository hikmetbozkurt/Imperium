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

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `quiz_attempts` ADD COLUMN `sessionSeed` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `quiz_attempts` ADD COLUMN `isNewBest` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `quiz_attempts` ADD COLUMN `addedStars` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `quiz_attempts` ADD COLUMN `unlockedLevels` INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE `quiz_attempts` ADD COLUMN `newlyUnlockedLevel` INTEGER DEFAULT NULL")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `question_responses` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `attemptId` INTEGER NOT NULL,
                    `questionId` TEXT NOT NULL,
                    `selectedAnswerIndex` INTEGER,
                    `correctAnswerIndex` INTEGER NOT NULL,
                    `isCorrect` INTEGER NOT NULL,
                    `responseTimeMs` INTEGER NOT NULL,
                    `position` INTEGER NOT NULL,
                    FOREIGN KEY(`attemptId`) REFERENCES `quiz_attempts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """.trimIndent(),
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_question_responses_attemptId` " +
                    "ON `question_responses` (`attemptId`)",
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_question_responses_questionId` " +
                    "ON `question_responses` (`questionId`)",
            )
        }
    }
}
