package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.SiteHygieneViewModel
import com.example.ui.components.WorkforceHealthBottomBar
import com.example.ui.components.WorkforceHealthTopBar
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AssessmentFormScreen
import com.example.ui.screens.AssessmentsListScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EquipmentScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NewAssessmentScreen
import com.example.ui.screens.RemedialActionsScreen
import com.example.ui.screens.ReportPreviewScreen
import com.example.ui.screens.SamplesLabScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SiteAnalyticsDashboardScreen
import com.example.ui.screens.AdminBackendScreen
import com.example.sync.SyncManager
import com.example.ui.theme.WorkforceHealthTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SiteHygieneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SyncManager.schedulePeriodicSync(applicationContext)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            WorkforceHealthTheme(
                selectedTheme = uiState.selectedTheme,
                darkTheme = uiState.isDarkMode
            ) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "splash"

                val isFullScreen = currentRoute == "splash" ||
                        currentRoute == "login" ||
                        currentRoute.startsWith("assessment_form") ||
                        currentRoute.startsWith("reports")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!isFullScreen) {
                            WorkforceHealthTopBar(
                                currentRoute = currentRoute,
                                currentRole = uiState.currentRole,
                                isDarkMode = uiState.isDarkMode,
                                selectedCompany = uiState.selectedCompany,
                                currentUser = uiState.currentUser,
                                onToggleDarkMode = { viewModel.toggleDarkMode() },
                                onRoleChange = { viewModel.setRole(it) },
                                onSelectCompanyClick = {
                                    if (currentRoute != "dashboard") {
                                        navController.navigate("dashboard") {
                                            popUpTo("dashboard") { saveState = true }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onLogout = {
                                    viewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (!isFullScreen) {
                            val openActions = uiState.actions.count {
                                it.status != com.example.data.model.ActionStatus.CLOSED
                            }
                            WorkforceHealthBottomBar(
                                currentRoute = currentRoute,
                                openActionsCount = openActions,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo("dashboard") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 0. Animated Splash Screen
                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    if (uiState.isLoggedIn) {
                                        navController.navigate("dashboard") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("login") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        // 0.1 Login Screen
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { email, _, user ->
                                    viewModel.login(email = email, user = user)
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 1. Dashboard (With Company Selector at top)
                        composable("dashboard") {
                            DashboardScreen(
                                assessments = uiState.assessments,
                                actions = uiState.actions,
                                siteVisits = uiState.siteVisits,
                                siteInfo = uiState.siteInfo,
                                currentRole = uiState.currentRole,
                                selectedCompany = uiState.selectedCompany,
                                availableCompanies = uiState.availableCompanies,
                                onSelectCompany = { viewModel.selectCompany(it) },
                                onAddNewCompany = { viewModel.addCompany(it) },
                                onNavigateToNewAssessment = { navController.navigate("new_assessment") },
                                onNavigateToAssessment = { id -> navController.navigate("assessment_form/$id") },
                                onNavigateToActions = { navController.navigate("remedial_actions") },
                                onNavigateToReports = {
                                    val firstId = uiState.assessments.firstOrNull()?.id ?: 0L
                                    navController.navigate("reports/$firstId")
                                },
                                onNavigateToSamples = { navController.navigate("samples_lab") },
                                onNavigateToAllAssessments = { navController.navigate("assessments_list") },
                                onNavigateToAnalytics = { navController.navigate("site_analytics") },
                                onNavigateToAdminBackend = { navController.navigate("admin_backend") },
                                onSelectModuleType = { module ->
                                    viewModel.startNewAssessment(module) { newId ->
                                        navController.navigate("assessment_form/$newId")
                                    }
                                },
                                selectedTheme = uiState.selectedTheme,
                                onSelectTheme = { viewModel.setTheme(it) },
                                onCreateEnvironmentalTask = { type, title, heg, notes, onCreated ->
                                    viewModel.createEnvironmentalFieldAssessmentTask(type, title, heg, notes, onCreated)
                                },
                                equipmentList = uiState.equipment
                            )
                        }

                        // 2. New Assessment Selection (10 statutory modules)
                        composable("new_assessment") {
                            NewAssessmentScreen(
                                siteInfo = uiState.siteInfo,
                                onSelectAssessmentType = { moduleType ->
                                    viewModel.startNewAssessment(moduleType) { newId ->
                                        navController.navigate("assessment_form/$newId")
                                    }
                                }
                            )
                        }


                        // 3. Assessment Form (Individual Module Workflow)
                        composable(
                            route = "assessment_form/{assessmentId}",
                            arguments = listOf(navArgument("assessmentId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getLong("assessmentId") ?: 0L
                            val assessment = uiState.assessments.find { it.id == id }
                                ?: uiState.selectedAssessment

                            if (assessment != null) {
                                AssessmentFormScreen(
                                    initialAssessment = assessment,
                                    onSaveAssessment = { viewModel.saveAssessment(it) },
                                    onNavigateBack = { navController.popBackStack() },
                                    onPreviewReport = { target ->
                                        navController.navigate("reports/${target.id}")
                                    }
                                )
                            }
                        }

                        // 4. All Assessments Records List
                        composable("assessments_list") {
                            AssessmentsListScreen(
                                assessments = uiState.assessments,
                                onSelectAssessment = { id -> navController.navigate("assessment_form/$id") }
                            )
                        }

                        // 5. Remedial Action Plans
                        composable("remedial_actions") {
                            RemedialActionsScreen(
                                actions = uiState.actions,
                                onAddAction = { viewModel.addAction(it) },
                                onUpdateAction = { viewModel.updateAction(it) }
                            )
                        }

                        // 6. Samples & Laboratory Chain of Custody
                        composable("samples_lab") {
                            SamplesLabScreen(
                                assessments = uiState.assessments,
                                onSelectAssessment = { id -> navController.navigate("assessment_form/$id") }
                            )
                        }

                        // 7. Equipment Calibration Register
                        composable("equipment") {
                            EquipmentScreen(
                                equipmentList = uiState.equipment,
                                onAddEquipment = { viewModel.addEquipment(it) }
                            )
                        }

                        // 8. Part 1 & Part 2 Statutory Reports
                        composable(
                            route = "reports/{assessmentId}",
                            arguments = listOf(navArgument("assessmentId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getLong("assessmentId") ?: 0L
                            val targetAssessment = uiState.assessments.find { it.id == id }
                                ?: uiState.assessments.firstOrNull()

                            if (targetAssessment != null) {
                                ReportPreviewScreen(
                                    assessment = targetAssessment,
                                    siteInfo = uiState.siteInfo,
                                    actions = uiState.actions,
                                    equipmentList = uiState.equipment,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                        }

                        // 9. Field AI Assistant
                        composable("ai_assistant") {
                            AiAssistantScreen(
                                assessments = uiState.assessments
                            )
                        }

                        // 10. Settings & Mine Site Configuration
                        composable("settings") {
                            SettingsScreen(
                                currentRole = uiState.currentRole,
                                onRoleSelected = { viewModel.setRole(it) },
                                siteInfo = uiState.siteInfo,
                                isDarkMode = uiState.isDarkMode,
                                onToggleDarkMode = { viewModel.setDarkMode(it) },
                                selectedTheme = uiState.selectedTheme,
                                onThemeSelected = { viewModel.setTheme(it) }
                            )
                        }

                        // 11. Interactive D3 / Recharts Site Analytics & Visualizations Dashboard
                        composable("site_analytics") {
                            SiteAnalyticsDashboardScreen(
                                assessments = uiState.assessments,
                                actions = uiState.actions,
                                availableCompanies = uiState.availableCompanies,
                                selectedCompany = uiState.selectedCompany,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 12. Admin Functions & Backend Module Portal
                        composable("admin_backend") {
                            AdminBackendScreen(
                                currentRole = uiState.currentRole,
                                currentUser = uiState.currentUser,
                                assessments = uiState.assessments,
                                actions = uiState.actions,
                                equipmentList = uiState.equipment,
                                siteVisits = uiState.siteVisits,
                                availableCompanies = uiState.availableCompanies,
                                selectedCompany = uiState.selectedCompany,
                                onRoleSelected = { viewModel.setRole(it) },
                                onAddNewCompany = { viewModel.addCompany(it) },
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToAssessment = { id -> navController.navigate("assessment_form/$id") }
                            )
                        }
                    }
                }
            }
        }
    }
}
