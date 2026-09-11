package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    subjects: List<Subject>,
    isGeneralEducation: Boolean,
    onToggleCurriculum: (Boolean) -> Unit,
    onSubjectClick: (String) -> Unit,
    onAiTeacherClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD4AF37)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Color(0xFF1B4332),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isGeneralEducation) "مذاكرة الثاني الإعدادي العام" else "المذاكرة الأزهرية 2027",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text(
                                text = if (isGeneralEducation) "الصف الثاني الإعدادي (عام)" else "الصف الثاني الإعدادي الأزهري",
                                fontSize = 12.sp,
                                color = Color(0xFFE9D8A6)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B4332)
                ),
                actions = {
                    IconButton(onClick = onAiTeacherClick) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "المعلم الذكي",
                            tint = Color(0xFFD4AF37)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1B4332),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                    label = { Text("المواد الدراسية", color = Color.White) },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                    label = { Text("المعلم الذكي", color = Color(0xFFE9D8A6)) },
                    selected = false,
                    onClick = onAiTeacherClick
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("التقييم والتقدم", color = Color(0xFFE9D8A6)) },
                    selected = false,
                    onClick = onProgressClick
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F9F6))
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Curriculum Toggle Switcher
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !isGeneralEducation,
                            onClick = { onToggleCurriculum(false) },
                            label = { Text("📚 المناهج الأزهرية", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2D6A4F),
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color(0xFF1B4332)
                            )
                        )
                        FilterChip(
                            selected = isGeneralEducation,
                            onClick = { onToggleCurriculum(true) },
                            label = { Text("🏫 المناهج العامة (عام)", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2D6A4F),
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color(0xFF1B4332)
                            )
                        )
                    }
                }

                // Banner Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D6A4F)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = if (isGeneralEducation) "أهلاً بك في قسم المناهج العامة للصف الثاني الإعدادي 🌟" else "أهلاً بك يا بطل في رحلة العلم والنجاح! 🌟",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isGeneralEducation) "تصفح المواد العامة (عربي، رياضيات، علوم، دراسات، إنجليزي)، مع الشرح المفصل واختبارات لكل وحدة." else "اختر المادة الدراسية لتتصفح الأبواب، والشرح المفصل، وتختبر معلوماتك لكل وحدة طبقاً لمنهج الأزهر الشريف 2027.",
                                fontSize = 13.sp,
                                color = Color(0xFFE9D8A6),
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = onAiTeacherClick,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFF1B4332)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "اسأل المعلم الذكي",
                                    color = Color(0xFF1B4332),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = if (isGeneralEducation) "المواد المقررة (الصف الثاني الإعدادي العام)" else "المواد المقررة (الصف الثاني الإعدادي الأزهري)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B4332),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(subjects) { subject ->
                    SubjectCard(subject = subject, onClick = { onSubjectClick(subject.id) })
                }
            }
        }
    }
}

@Composable
fun SubjectCard(subject: Subject, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color(0xFF2D6A4F),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = subject.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4332)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subject.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Badge(containerColor = Color(0xFFD4AF37)) {
                        Text(
                            text = "${subject.units.size} وحدات وبواب",
                            fontSize = 10.sp,
                            color = Color(0xFF1B4332),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
