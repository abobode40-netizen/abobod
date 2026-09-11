package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.AzharCurriculumRepository
import com.example.ui.screens.*
import com.example.ui.viewmodel.AzharViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SubjectDetail : Screen("subject_detail/{subjectId}") {
        fun createRoute(subjectId: String) = "subject_detail/$subjectId"
    }
    object UnitDetail : Screen("unit_detail/{unitId}/{subjectId}") {
        fun createRoute(unitId: String, subjectId: String) = "unit_detail/$unitId/$subjectId"
    }
    object Quiz : Screen("quiz/{unitId}/{subjectId}") {
        fun createRoute(unitId: String, subjectId: String) = "quiz/$unitId/$subjectId"
    }
    object AiTeacher : Screen("ai_teacher")
    object Progress : Screen("progress")
}

@Composable
fun AzharNavGraph(viewModel: AzharViewModel = viewModel()) {
    val navController = rememberNavController()
    val subjects by viewModel.subjects.collectAsState()
    val isGeneralEducation by viewModel.isGeneralEducation.collectAsState()
    val quizScores by viewModel.quizScores.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                subjects = subjects,
                isGeneralEducation = isGeneralEducation,
                onToggleCurriculum = { isGen ->
                    viewModel.setCurriculumMode(isGen)
                },
                onSubjectClick = { subjectId ->
                    navController.navigate(Screen.SubjectDetail.createRoute(subjectId))
                },
                onAiTeacherClick = {
                    navController.navigate(Screen.AiTeacher.route)
                },
                onProgressClick = {
                    navController.navigate(Screen.Progress.route)
                }
            )
        }

        composable(
            route = Screen.SubjectDetail.route,
            arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            val subject = subjects.find { it.id == subjectId } ?: subjects.first()
            SubjectDetailScreen(
                subject = subject,
                onBackClick = { navController.popBackStack() },
                onUnitClick = { unitId, subjId ->
                    navController.navigate(Screen.UnitDetail.createRoute(unitId, subjId))
                },
                onQuizClick = { unitId, subjId ->
                    navController.navigate(Screen.Quiz.createRoute(unitId, subjId))
                }
            )
        }

        composable(
            route = Screen.UnitDetail.route,
            arguments = listOf(
                navArgument("unitId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unitId = backStackEntry.arguments?.getString("unitId") ?: ""
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            val subject = subjects.find { it.id == subjectId }
            val unit = subject?.units?.find { it.id == unitId } ?: subject?.units?.first()

            if (unit != null && subject != null) {
                UnitDetailScreen(
                    unit = unit,
                    subjectTitle = subject.title,
                    onBackClick = { navController.popBackStack() },
                    onStartQuizClick = {
                        navController.navigate(Screen.Quiz.createRoute(unitId, subjectId))
                    }
                )
            }
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("unitId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unitId = backStackEntry.arguments?.getString("unitId") ?: ""
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            val subject = subjects.find { it.id == subjectId }
            val unit = subject?.units?.find { it.id == unitId } ?: subject?.units?.first()

            if (unit != null && subject != null) {
                QuizScreen(
                    unit = unit,
                    subjectTitle = subject.title,
                    onBackClick = { navController.popBackStack() },
                    onQuizFinished = { score, total ->
                        viewModel.recordQuizScore(
                            unitId = unit.id,
                            unitTitle = unit.title,
                            subjectTitle = subject.title,
                            score = score,
                            total = total
                        )
                    }
                )
            }
        }

        composable(Screen.AiTeacher.route) {
            AiTeacherScreen(
                messages = chatMessages,
                isLoading = isAiLoading,
                onBackClick = { navController.popBackStack() },
                onSendMessage = { question ->
                    viewModel.askAiTeacher(question)
                }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                quizScores = quizScores,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
