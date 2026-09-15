package com.hikmet.imperium.domain.repository

import com.hikmet.imperium.domain.model.CategoryId
import com.hikmet.imperium.domain.model.HistoryCategory
import com.hikmet.imperium.domain.model.QuizQuestion

interface HistoryContentRepository {
    fun categories(): List<HistoryCategory>
    fun category(id: CategoryId): HistoryCategory?
    fun questions(categoryId: CategoryId, levelNumber: Int): List<QuizQuestion>
}
