package com.hikmet.imperium.data

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val id: String? = null,
    val conceptId: String? = null,
    val difficulty: Int = 2,
    val explanation: String? = null,
)
