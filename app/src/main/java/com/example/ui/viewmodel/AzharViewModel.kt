package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AzharAiService
import com.example.data.AzharCurriculumRepository
import com.example.data.QuizScore
import com.example.data.Subject
import com.example.data.UnitItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AzharViewModel : ViewModel() {

    private val _isGeneralEducation = MutableStateFlow(false)
    val isGeneralEducation: StateFlow<Boolean> = _isGeneralEducation.asStateFlow()

    private val _subjects = MutableStateFlow(AzharCurriculumRepository.azharSubjects)
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    fun setCurriculumMode(isGeneral: Boolean) {
        _isGeneralEducation.value = isGeneral
        _subjects.value = if (isGeneral) {
            AzharCurriculumRepository.generalSubjects
        } else {
            AzharCurriculumRepository.azharSubjects
        }
    }

    private val _quizScores = MutableStateFlow<List<QuizScore>>(emptyList())
    val quizScores: StateFlow<List<QuizScore>> = _quizScores.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("أهلاً بك يا بطل في تطبيق المذاكرة الأزهرية للصف الثاني الإعدادي الأزهري (2027)! أنا معلمك الأزهري الذكي، تفضل بسؤالى عن أي درس أو قاعدة أو تفسير في المنهج.", false)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    fun recordQuizScore(unitId: String, unitTitle: String, subjectTitle: String, score: Int, total: Int) {
        val newScore = QuizScore(unitId, unitTitle, subjectTitle, score, total)
        _quizScores.update { currentList ->
            // Replace if exists for this unit, or add new
            val filtered = currentList.filterNot { it.unitId == unitId }
            filtered + newScore
        }
    }

    fun askAiTeacher(question: String) {
        if (question.isBlank()) return
        val userMsg = ChatMessage(question, true)
        _chatMessages.update { it + userMsg }

        viewModelScope.launch {
            _isAiLoading.value = true
            val answer = AzharAiService.askAzharTeacher(question)
            val aiMsg = ChatMessage(answer, false)
            _chatMessages.update { it + aiMsg }
            _isAiLoading.value = false
        }
    }
}
