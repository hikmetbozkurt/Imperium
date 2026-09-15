package com.hikmet.imperium.data.content

import com.hikmet.imperium.data.Question

internal fun supplementalQuestion(
    text: String,
    correct: String,
    distractor1: String,
    distractor2: String,
    distractor3: String,
): Question = Question(
    text = text,
    options = listOf(correct, distractor1, distractor2, distractor3),
    correctAnswerIndex = 0,
)
