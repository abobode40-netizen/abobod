package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UnitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    unit: UnitItem,
    subjectTitle: String,
    onBackClick: () -> Unit,
    onQuizFinished: (Int, Int) -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val questions = unit.quizQuestions

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "اختبار: ${unit.title}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = subjectTitle,
                            color = Color(0xFFE9D8A6),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F9F6))
                .padding(paddingValues)
        ) {
            if (isQuizCompleted) {
                // Quiz Finished Card
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎉 انتهى الاختبار بنجاح!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B4332)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "نتيجتك هي: $score من ${questions.size}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD4AF37)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val percentage = if (questions.isNotEmpty()) (score * 100) / questions.size else 0
                            val praise = when {
                                percentage >= 80 -> "أحسنت يا بطل الأزهر! أداء ممتاز ومبهر 🌟"
                                percentage >= 50 -> "جيد جداً، واصل المذاكرة والتحصيل 💪"
                                else -> "تحتاج لمراجعة الشرح المفصل جيداً ثم إعادة الاختبار 📚"
                            }
                            Text(
                                text = praise,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    currentQuestionIndex = 0
                                    score = 0
                                    selectedOptionIndex = null
                                    isAnswerSubmitted = false
                                    isQuizCompleted = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "إعادة الاختبار", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = onBackClick,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "العودة للوحدات", color = Color(0xFF2D6A4F))
                            }
                        }
                    }
                }
            } else if (questions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "لا توجد أسئلة متاحة لهذه الوحدة حالياً.", color = Color.Gray)
                }
            } else {
                val q = questions[currentQuestionIndex]
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Badge(containerColor = Color(0xFFE8F5E9)) {
                                Text(
                                    text = "السؤال ${currentQuestionIndex + 1} من ${questions.size}",
                                    color = Color(0xFF2D6A4F),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            LinearProgressIndicator(
                                progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF2D6A4F),
                                trackColor = Color(0xFFDCDCDC)
                            )
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = q.questionText,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B4332),
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }

                    items(q.options.size) { index ->
                        val isSelected = selectedOptionIndex == index
                        val isCorrect = index == q.correctAnswerIndex
                        
                        val backgroundColor = when {
                            isAnswerSubmitted && isCorrect -> Color(0xFFD4EDDA)
                            isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFF8D7DA)
                            isSelected -> Color(0xFFE8F5E9)
                            else -> Color.White
                        }
                        
                        val borderColor = when {
                            isAnswerSubmitted && isCorrect -> Color(0xFF28A745)
                            isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFDC3545)
                            isSelected -> Color(0xFF2D6A4F)
                            else -> Color(0xFFE0E0E0)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable(enabled = !isAnswerSubmitted) {
                                    selectedOptionIndex = index
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = backgroundColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { if (!isAnswerSubmitted) selectedOptionIndex = index },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2D6A4F))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = q.options[index],
                                    fontSize = 15.sp,
                                    color = Color(0xFF212529),
                                    modifier = Modifier.weight(1f)
                                )
                                if (isAnswerSubmitted) {
                                    if (isCorrect) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF28A745))
                                    } else if (isSelected) {
                                        Icon(imageVector = Icons.Default.HighlightOff, contentDescription = null, tint = Color(0xFFDC3545))
                                    }
                                }
                            }
                        }
                    }

                    if (isAnswerSubmitted) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "التوضيح والشرح:",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB78103),
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = q.explanation,
                                        fontSize = 13.sp,
                                        color = Color(0xFF5A4A00),
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!isAnswerSubmitted) {
                            Button(
                                onClick = {
                                    if (selectedOptionIndex != null) {
                                        isAnswerSubmitted = true
                                        if (selectedOptionIndex == q.correctAnswerIndex) {
                                            score++
                                        }
                                    }
                                },
                                enabled = selectedOptionIndex != null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "تحقق من الإجابة", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (currentQuestionIndex < questions.size - 1) {
                                        currentQuestionIndex++
                                        selectedOptionIndex = null
                                        isAnswerSubmitted = false
                                    } else {
                                        isQuizCompleted = true
                                        onQuizFinished(score, questions.size)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (currentQuestionIndex < questions.size - 1) "السؤال التالي" else "عرض النتيجة النهائية",
                                    color = Color(0xFF1B4332),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
