package com.hikmet.imperium.data.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {
    @Test
    fun migration4To5PreservesAttemptsAndAddsResponseHistory() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.deleteDatabase(TEST_DATABASE)
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(TEST_DATABASE)
                .callback(
                    object : SupportSQLiteOpenHelper.Callback(4) {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            db.execSQL(CREATE_VERSION_FOUR_ATTEMPTS)
                        }

                        override fun onUpgrade(
                            db: SupportSQLiteDatabase,
                            oldVersion: Int,
                            newVersion: Int,
                        ) = Unit
                    },
                )
                .build(),
        )
        val migrated = helper.writableDatabase.apply {
            execSQL(
                """
                INSERT INTO quiz_attempts (
                    id, categoryId, levelNumber, score, stars, correctAnswers,
                    totalQuestions, durationMs, completedAtEpochMs
                ) VALUES (1, 'ancient', 1, 75, 2, 3, 4, 12000, 1000)
                """.trimIndent(),
            )
            DatabaseMigrations.MIGRATION_4_5.migrate(this)
        }
        migrated.query("SELECT score, sessionSeed, addedStars FROM quiz_attempts WHERE id = 1").use { cursor ->
            cursor.moveToFirst()
            assertEquals(75, cursor.getInt(0))
            assertEquals(0L, cursor.getLong(1))
            assertEquals(0, cursor.getInt(2))
        }
        migrated.execSQL(
            """
            INSERT INTO question_responses (
                attemptId, questionId, selectedAnswerIndex, correctAnswerIndex,
                isCorrect, responseTimeMs, position
            ) VALUES (1, 'stable-question-id', 2, 2, 1, 2500, 0)
            """.trimIndent(),
        )
        assertEquals(1, migrated.count("question_responses"))
        helper.close()
        context.deleteDatabase(TEST_DATABASE)
    }

    private fun SupportSQLiteDatabase.count(table: String): Int =
        query("SELECT COUNT(*) FROM $table").use { cursor ->
            cursor.moveToFirst()
            cursor.getInt(0)
        }

    private companion object {
        const val TEST_DATABASE = "migration-test"
        val CREATE_VERSION_FOUR_ATTEMPTS =
            """
            CREATE TABLE IF NOT EXISTS quiz_attempts (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                categoryId TEXT NOT NULL,
                levelNumber INTEGER NOT NULL,
                score INTEGER NOT NULL,
                stars INTEGER NOT NULL,
                correctAnswers INTEGER NOT NULL,
                totalQuestions INTEGER NOT NULL,
                durationMs INTEGER NOT NULL,
                completedAtEpochMs INTEGER NOT NULL
            )
            """.trimIndent()
    }
}
