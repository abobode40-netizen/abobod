package com.example.data

data class Subject(
    val id: String,
    val title: String,
    val arabicName: String,
    val description: String,
    val iconName: String, // e.g. "book", "quran", "star", "language"
    val units: List<UnitItem>
)

data class UnitItem(
    val id: String,
    val subjectId: String,
    val unitNumber: Int,
    val title: String,
    val summary: String,
    val detailedExplanation: List<String>,
    val quizQuestions: List<Question>
)

data class Question(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class QuizScore(
    val unitId: String,
    val unitTitle: String,
    val subjectTitle: String,
    val score: Int,
    val total: Int,
    val timestamp: Long = System.currentTimeMillis()
)
