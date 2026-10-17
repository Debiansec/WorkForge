package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.EquipmentEntity
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.local.entity.SiteInfoEntity
import com.example.data.local.entity.SiteVisitEntity
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.CompanySite
import com.example.data.model.UserRole
import com.example.sync.SyncManager
import com.example.sync.SyncState
import com.example.ui.theme.AppTheme
import com.example.ui.theme.CorporateNavy800
import com.example.ui.theme.CorporateNavy900
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.SafetyRedPrimary
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.util.PdfReportGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Central Project Dashboard UI
 * Displays a summary of ongoing environmental field assessments, project status cards,
 * quick navigation to active field tasks, an interactive 'New Task' FAB with Room DB persistence,
 * and animated icons with dynamic theme customization (Red theme default).
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    assessments: List<AssessmentEntity>,
    actions: List<RemedialActionEntity>,
    siteVisits: List<SiteVisitEntity>,
    siteInfo: SiteInfoEntity,
    currentRole: UserRole,
    selectedCompany: CompanySite,
    availableCompanies: List<CompanySite>,
    selectedTheme: AppTheme = AppTheme.INDUSTRIAL_RED,
    onSelectTheme: (AppTheme) -> Unit = {},
    onSelectCompany: (CompanySite) -> Unit,
    onAddNewCompany: (CompanySite) -> Unit,
    onNavigateToNewAssessment: () -> Unit,
    onNavigateToAssessment: (Long) -> Unit,
    onNavigateToActions: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToSamples: () -> Unit,
    onNavigateToAllAssessments: () -> Unit,
    onNavigateToAnalytics: () -> Unit = {},
    onNavigateToAdminBackend: () -> Unit = {},
    onSelectModuleType: (AssessmentType) -> Unit = {},
    onCreateEnvironmentalTask: ((type: AssessmentType, title: String, heg: String, notes: String, onCreated: (Long) -> Unit) -> Unit)? = null,
    equipmentList: List<EquipmentEntity> = emptyList(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val activeTheme = selectedTheme

    // Sync State
    val syncState by SyncManager.getSyncWorkState(context).collectAsState(initial = SyncState.IDLE)
    val lastSyncFormatted = remember(syncState) { SyncManager.getLastSyncTimeFormatted(context) }

    // Dialog & Sheet States
    var showNewTaskSheet by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var isExportingPdf by remember { mutableStateOf(false) }
    var exportedFile by remember { mutableStateOf<File?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }
    var activeCategoryFilter by remember { mutableStateOf("All Active") }
    var highlightedBannerPill by remember { mutableStateOf<String?>(null) }

    // Ongoing Environmental Field Assessments
    val ongoingAssessments = remember(assessments) {
        assessments.filter { it.status != AssessmentStatus.COMPLETED }
    }
    val inProgressAssessments = remember(assessments) {
        assessments.filter { it.status == AssessmentStatus.IN_PROGRESS }
    }
    val awaitingReviewAssessments = remember(assessments) {
        assessments.filter { it.status == AssessmentStatus.AWAITING_REVIEW || it.status == AssessmentStatus.AWAITING_RESULTS }
    }
    val completedCount = assessments.count { it.status == AssessmentStatus.COMPLETED }
    val openActionsCount = actions.count { it.status != com.example.data.model.ActionStatus.CLOSED }
    val latestDraft = inProgressAssessments.firstOrNull()

    // Filtered active field tasks
    val filteredActiveTasks = remember(ongoingAssessments, activeCategoryFilter, highlightedBannerPill) {
        var list = ongoingAssessments
        if (highlightedBannerPill != null) {
            list = when (highlightedBannerPill) {
                "CONTROL" -> list.filter {
                    it.type == AssessmentType.PERSONAL_NOISE || it.type == AssessmentType.AREA_SOURCE_NOISE || it.type == AssessmentType.THERMAL_STRESS
                }
                "EVALUATION" -> list.filter {
                    it.type == AssessmentType.PERSONAL_AIRBORNE || it.type == AssessmentType.FALLOUT_DUST || it.type == AssessmentType.POTABLE_WATER_A || it.type == AssessmentType.POTABLE_WATER_B
                }
                "PREVENTION" -> list.filter {
                    it.type == AssessmentType.ILLUMINATION_DAY_NIGHT || it.type == AssessmentType.TMM_ILLUMINATION || it.type == AssessmentType.ABLUTION_FACILITIES
                }
                else -> list
            }
        }
        when (activeCategoryFilter) {
            "Acoustics" -> list.filter { it.type == AssessmentType.PERSONAL_NOISE || it.type == AssessmentType.AREA_SOURCE_NOISE }
            "Air & Dust" -> list.filter { it.type == AssessmentType.PERSONAL_AIRBORNE || it.type == AssessmentType.FALLOUT_DUST }
            "Thermal" -> list.filter { it.type == AssessmentType.THERMAL_STRESS }
            "Lighting" -> list.filter { it.type == AssessmentType.ILLUMINATION_DAY_NIGHT || it.type == AssessmentType.TMM_ILLUMINATION }
            "Water & Hygiene" -> list.filter { it.type == AssessmentType.POTABLE_WATER_A || it.type == AssessmentType.POTABLE_WATER_B || it.type == AssessmentType.ABLUTION_FACILITIES }
            else -> list
        }
    }

    fun generateAuditPdfReport(shareImmediately: Boolean = false) {
        if (isExportingPdf) return
        isExportingPdf = true
        coroutineScope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    PdfReportGenerator.generateSummaryPdf(
                        context = context,
                        siteInfo = siteInfo,
                        assessments = assessments,
                        actions = actions,
                        equipmentList = equipmentList,
                        targetAssessment = assessments.firstOrNull()
                    )
                }
                exportedFile = file
                isExportingPdf = false
                if (shareImmediately) {
                    PdfReportGenerator.openOrSharePdf(context, file, share = true)
                } else {
                    showExportDialog = true
                }
            } catch (e: Exception) {
                isExportingPdf = false
            }
        }
    }

    // Animated pulse for the 'New Task' FAB
    val infiniteTransition = rememberInfiniteTransition(label = "FabPulse")
    val fabPulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FabScale"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewTaskSheet = true },
                icon = {
                    Icon(
                        imageVector = Icons.Default.PostAdd,
                        contentDescription = "New Environmental Task",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                },
                text = {
                    Text(
                        text = "New Task",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                },
                containerColor = activeTheme.primaryColor,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .scale(fabPulseScale)
                    .testTag("dashboard_new_task_fab")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Company / Mine Site Selector & Quick Theme Switcher Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CompanyAssessmentSelectorHeader(
                        selectedCompany = selectedCompany,
                        availableCompanies = availableCompanies,
                        onSelectCompany = onSelectCompany,
                        onAddNewCompany = onAddNewCompany,
                        siteInfo = siteInfo
                    )

                    // Theme selector bar & active theme badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = activeTheme.containerColor,
                            border = BorderStroke(1.dp, activeTheme.primaryColor.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { showThemeDialog = true }
                                .testTag("theme_switcher_pill")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(activeTheme.primaryColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Theme: ${activeTheme.title}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = activeTheme.onContainerColor
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = "Switch Theme",
                                    tint = activeTheme.primaryColor,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        // Room Database Offline Persistence Indicator
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Room DB Offline Ready",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Industrial Safety Hero Banner with CONTROL, EVALUATION, PREVENTION animated badges
            item {
                IndustrialSafetyHeroBanner(
                    activeTheme = activeTheme,
                    highlightedPill = highlightedBannerPill,
                    onSelectPill = { pill ->
                        highlightedBannerPill = if (highlightedBannerPill == pill) null else pill
                    }
                )
            }

            // 3. Central Project Summary KPI Status Cards (Ongoing Environmental Field Assessments)
            item {
                OngoingAssessmentsSummarySection(
                    activeTheme = activeTheme,
                    totalOngoing = ongoingAssessments.size,
                    inProgressCount = inProgressAssessments.size,
                    awaitingReviewCount = awaitingReviewAssessments.size,
                    completedCount = completedCount,
                    totalAssessments = assessments.size
                )
            }

            // 4. D3 / Recharts Site Analytics & Visualizations Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("site_analytics_banner_card")
                        .clickable { onNavigateToAnalytics() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, activeTheme.primaryColor.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = activeTheme.primaryColor,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PieChart,
                                        contentDescription = "Analytics",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Site Analytics & Visuals",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = activeTheme.containerColor,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "D3 / Recharts",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = activeTheme.primaryColor,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Status distributions & pending actions per industrial site",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateToAnalytics,
                            colors = ButtonDefaults.buttonColors(containerColor = CorporateNavy900),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.BarChart, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Charts", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 5. Admin Functions & Backend Control Module Card
            item {
                AdminBackendPortalCard(
                    activeTheme = activeTheme,
                    currentRole = currentRole,
                    assessmentsCount = assessments.size,
                    syncState = syncState,
                    onNavigateToAdminBackend = onNavigateToAdminBackend
                )
            }

            // 6. Statutory Assessment Protocols & Modules Menu with Icons
            item {
                AssessmentModulesMenuSection(
                    activeTheme = activeTheme,
                    assessments = assessments,
                    onSelectModule = { module ->
                        if (onCreateEnvironmentalTask != null) {
                            onCreateEnvironmentalTask(module, module.title, selectedCompany.primaryHegs.firstOrNull() ?: "Operational Plant & Quarry", "") { newId ->
                                onNavigateToAssessment(newId)
                            }
                        } else {
                            onSelectModuleType(module)
                        }
                    }
                )
            }

            // 7. Quick Navigation to Active Field Tasks
            item {
                ActiveFieldTasksSection(
                    activeTheme = activeTheme,
                    tasks = filteredActiveTasks,
                    selectedFilter = activeCategoryFilter,
                    onSelectFilter = { activeCategoryFilter = it },
                    onOpenTask = { taskId -> onNavigateToAssessment(taskId) },
                    onAddNewTaskClick = { showNewTaskSheet = true }
                )
            }

            // 6. Statutory Audit PDF Summary Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("summary_pdf_export_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CorporateNavy800),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                shadowElevation = 2.dp
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.app_logo),
                                        contentDescription = "Workforce Health Logo",
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Audit Summary PDF",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = activeTheme.primaryColor,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "ANNUAL",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Assessments (${assessments.size}) • Actions (${actions.size}) • Tools (${equipmentList.size})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate400,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { generateAuditPdfReport(shareImmediately = false) },
                            colors = ButtonDefaults.buttonColors(containerColor = activeTheme.primaryColor),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            enabled = !isExportingPdf,
                            modifier = Modifier.testTag("dashboard_export_pdf_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isExportingPdf) "Exporting..." else "Export PDF",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 7. Operations Action Tiles (Secondary Actions)
            item {
                Text(
                    text = "Operational Field Tools",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DashboardActionTile(
                        title = "Remedial Actions",
                        subtitle = "$openActionsCount items pending",
                        icon = Icons.Default.Warning,
                        iconBg = if (openActionsCount > 0) StatusCritical else StatusSuccess,
                        onClick = onNavigateToActions,
                        modifier = Modifier.weight(1f),
                        testTag = "follow_up_actions_tile"
                    )

                    DashboardActionTile(
                        title = "Reports & Logs",
                        subtitle = "Statutory forms & records",
                        icon = Icons.Default.Description,
                        iconBg = CorporateNavy900,
                        onClick = onNavigateToReports,
                        modifier = Modifier.weight(1f),
                        testTag = "reports_documents_tile"
                    )
                }
            }

            // 8. Shift Environmental Baseline Card
            item {
                ShiftEnvironmentCard()
            }

            item { Spacer(modifier = Modifier.height(72.dp)) } // Padding for FAB
        }
    }

    // ---------------------------------------------------------------------------------------------
    // MODAL BOTTOM SHEET: CREATE NEW ENVIRONMENTAL FIELD ASSESSMENT (Room Database Persistent)
    // ---------------------------------------------------------------------------------------------
    if (showNewTaskSheet) {
        NewEnvironmentalTaskModalSheet(
            activeTheme = activeTheme,
            selectedCompany = selectedCompany,
            siteInfo = siteInfo,
            onDismiss = { showNewTaskSheet = false },
            onCreateTask = { type, title, targetHeg, notes ->
                showNewTaskSheet = false
                if (onCreateEnvironmentalTask != null) {
                    onCreateEnvironmentalTask(type, title, targetHeg, notes) { newId ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Environmental field task saved locally in Room DB")
                        }
                        onNavigateToAssessment(newId)
                    }
                } else {
                    onNavigateToNewAssessment()
                }
            }
        )
    }

    // ---------------------------------------------------------------------------------------------
    // THEME CHOOSER DIALOG: ("choose different themes")
    // ---------------------------------------------------------------------------------------------
    if (showThemeDialog) {
        ThemeChooserDialog(
            currentTheme = activeTheme,
            onSelectTheme = { theme ->
                onSelectTheme(theme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    // PDF Export Success Dialog
    if (showExportDialog && exportedFile != null) {
        val file = exportedFile!!
        val fileSizeKb = file.length() / 1024

        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = activeTheme.primaryColor,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text("Statutory Audit PDF Report Exported", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "The integrated statutory audit summary report has been compiled and saved to local storage for ${selectedCompany.name}.",
                        fontSize = 13.sp
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("File: ${file.name}", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Size: $fileSizeKb KB (2 Pages, A4)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Data: ${assessments.size} Assessments • ${actions.size} Remedial Actions • ${equipmentList.size} Certified Tools", fontSize = 10.sp, color = activeTheme.primaryColor)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExportDialog = false
                        PdfReportGenerator.openOrSharePdf(context, file, share = false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CorporateNavy800)
                ) {
                    Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open PDF")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showExportDialog = false
                        PdfReportGenerator.openOrSharePdf(context, file, share = true)
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share")
                }
            }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// HERO INDUSTRIAL SAFETY BANNER WITH ANIMATED ICONS (CONTROL, EVALUATION, PREVENTION)
// -------------------------------------------------------------------------------------------------
@Composable
fun IndustrialSafetyHeroBanner(
    activeTheme: AppTheme,
    highlightedPill: String?,
    onSelectPill: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CorporateNavy900),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, activeTheme.primaryColor.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Hero Image Container with Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.industrial_safety_banner),
                    contentDescription = "WorkForce Health SA Industrial Safety Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient scrim for maximum text contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                // Top Badge: Occupational Hygiene & Environmental Safety
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, activeTheme.lightColor.copy(alpha = 0.7f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(activeTheme.primaryColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mines Health & Safety Act (MHSA)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = activeTheme.primaryColor,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "FIELD OPS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Banner Bottom Tagline
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "WorkForce Health SA",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Ensuring a healthy working environment • Zero Harm",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate200
                    )
                }
            }

            // Interactive Bottom Bar with Animated CONTROL, EVALUATION, PREVENTION Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CorporateNavy800)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedPillAction(
                    title = "CONTROL",
                    subtitle = "Acoustics & Heat",
                    isSelected = highlightedPill == "CONTROL",
                    accentColor = activeTheme.primaryColor,
                    animatedContent = { AnimatedEqualizerBars(activeTheme.primaryColor) },
                    onClick = { onSelectPill("CONTROL") },
                    modifier = Modifier.weight(1f)
                )

                AnimatedPillAction(
                    title = "EVALUATION",
                    subtitle = "Air, Dust & Water",
                    isSelected = highlightedPill == "EVALUATION",
                    accentColor = Color(0xFF0EA5E9),
                    animatedContent = { AnimatedScanningMagnifier() },
                    onClick = { onSelectPill("EVALUATION") },
                    modifier = Modifier.weight(1f)
                )

                AnimatedPillAction(
                    title = "PREVENTION",
                    subtitle = "Lighting & Health",
                    isSelected = highlightedPill == "PREVENTION",
                    accentColor = Color(0xFF10B981),
                    animatedContent = { AnimatedBeatingHeart() },
                    onClick = { onSelectPill("PREVENTION") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// ANIMATED ICONS
// -------------------------------------------------------------------------------------------------
@Composable
fun AnimatedPillAction(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    accentColor: Color,
    animatedContent: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.25f) else CorporateNavy900,
        border = BorderStroke(
            1.5.dp,
            if (isSelected) accentColor else Color.White.copy(alpha = 0.15f)
        ),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                animatedContent()
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = if (isSelected) accentColor else Color.White
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Slate400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AnimatedEqualizerBars(tint: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "Equalizer")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(520, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.size(18.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height((18 * bar1).dp)
                .background(tint, RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .width(4.dp)
                .height((18 * bar2).dp)
                .background(tint, RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .width(4.dp)
                .height((18 * bar3).dp)
                .background(tint, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun AnimatedScanningMagnifier() {
    val infiniteTransition = rememberInfiniteTransition(label = "Scanner")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "rot"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(750, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "sc"
    )

    Icon(
        imageVector = Icons.Default.Science,
        contentDescription = null,
        tint = Color(0xFF0EA5E9),
        modifier = Modifier
            .rotate(rotation)
            .scale(scale)
            .size(18.dp)
    )
}

@Composable
fun AnimatedBeatingHeart() {
    val infiniteTransition = rememberInfiniteTransition(label = "Heartbeat")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(650, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = null,
        tint = Color(0xFF10B981),
        modifier = Modifier
            .scale(pulse)
            .size(18.dp)
    )
}

// -------------------------------------------------------------------------------------------------
// ONGOING ASSESSMENTS SUMMARY SECTION (PROJECT STATUS CARDS)
// -------------------------------------------------------------------------------------------------
@Composable
fun OngoingAssessmentsSummarySection(
    activeTheme: AppTheme,
    totalOngoing: Int,
    inProgressCount: Int,
    awaitingReviewCount: Int,
    completedCount: Int,
    totalAssessments: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val infiniteTransition = rememberInfiniteTransition(label = "PulseSensor")
                    val pulseAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
                        label = "SensorPulse"
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(activeTheme.primaryColor.copy(alpha = pulseAlpha), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ongoing Environmental Assessments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = activeTheme.primaryColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "$totalOngoing ACTIVE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = activeTheme.primaryColor,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Project Status Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProjectSummaryStatusTile(
                    title = "In Progress",
                    count = inProgressCount,
                    subtitle = "Field sampling",
                    indicatorColor = activeTheme.primaryColor,
                    icon = Icons.Default.Sensors,
                    modifier = Modifier.weight(1f)
                )

                ProjectSummaryStatusTile(
                    title = "Awaiting Review",
                    count = awaitingReviewCount,
                    subtitle = "Lab / Sign-off",
                    indicatorColor = StatusWarning,
                    icon = Icons.Default.PendingActions,
                    modifier = Modifier.weight(1f)
                )

                ProjectSummaryStatusTile(
                    title = "Approved",
                    count = completedCount,
                    subtitle = "Compliant OEL",
                    indicatorColor = StatusSuccess,
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ProjectSummaryStatusTile(
    title: String,
    count: Int,
    subtitle: String,
    indicatorColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = indicatorColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, indicatorColor.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = indicatorColor
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = indicatorColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// QUICK NAVIGATION TO ACTIVE FIELD TASKS SECTION
// -------------------------------------------------------------------------------------------------
@Composable
fun ActiveFieldTasksSection(
    activeTheme: AppTheme,
    tasks: List<AssessmentEntity>,
    selectedFilter: String,
    onSelectFilter: (String) -> Unit,
    onOpenTask: (Long) -> Unit,
    onAddNewTaskClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Active Field Tasks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "1-Tap navigation to active environmental audits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = onAddNewTaskClick,
                colors = ButtonDefaults.textButtonColors(contentColor = activeTheme.primaryColor)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Task", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Category Filter Chips
        val categories = listOf("All Active", "Acoustics", "Air & Dust", "Thermal", "Lighting", "Water & Hygiene")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(categories) { cat ->
                val isSelected = selectedFilter == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectFilter(cat) },
                    label = { Text(cat, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = activeTheme.primaryColor,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (tasks.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No pending active tasks in this category",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Tap 'New Task' FAB below to create an environmental field assessment",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tasks.forEach { task ->
                    ActiveTaskItemCard(
                        task = task,
                        activeTheme = activeTheme,
                        onClick = { onOpenTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveTaskItemCard(
    task: AssessmentEntity,
    activeTheme: AppTheme,
    onClick: () -> Unit
) {
    val icon = when (task.type) {
        AssessmentType.PERSONAL_NOISE -> Icons.Default.VolumeUp
        AssessmentType.PERSONAL_AIRBORNE -> Icons.Default.Air
        AssessmentType.THERMAL_STRESS -> Icons.Default.Thermostat
        AssessmentType.AREA_SOURCE_NOISE -> Icons.Default.Hearing
        AssessmentType.ILLUMINATION_DAY_NIGHT -> Icons.Default.LightMode
        AssessmentType.TMM_ILLUMINATION -> Icons.Default.LightMode
        AssessmentType.FALLOUT_DUST -> Icons.Default.Air
        AssessmentType.POTABLE_WATER_A -> Icons.Default.Science
        AssessmentType.POTABLE_WATER_B -> Icons.Default.Science
        AssessmentType.ABLUTION_FACILITIES -> Icons.Default.CleanHands
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("active_task_card_${task.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = activeTheme.primaryColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = activeTheme.primaryColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = CorporateNavy900.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = task.documentRef,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CorporateNavy800,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = task.type.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Area: ${task.targetAreaOrHeg}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Assessor: ${task.assessor}",
                            fontSize = 10.sp,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${task.visitDate}",
                            fontSize = 10.sp,
                            color = Slate400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = activeTheme.primaryColor),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("Open", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// MODAL BOTTOM SHEET: CREATE NEW ENVIRONMENTAL ASSESSMENT TASK (ROOM DB PERSISTED)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewEnvironmentalTaskModalSheet(
    activeTheme: AppTheme,
    selectedCompany: CompanySite,
    siteInfo: SiteInfoEntity,
    onDismiss: () -> Unit,
    onCreateTask: (type: AssessmentType, title: String, targetHeg: String, notes: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedType by remember { mutableStateOf(AssessmentType.PERSONAL_AIRBORNE) }
    var targetArea by remember { mutableStateOf(selectedCompany.primaryHegs.firstOrNull() ?: "Primary Crusher / HEG 01") }
    var taskNotes by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "New Environmental Field Task",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Saves immediately to local Room DB (Offline ready)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            // Target Industrial Mine / Client
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = activeTheme.containerColor.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, activeTheme.primaryColor.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = activeTheme.primaryColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = selectedCompany.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = activeTheme.onContainerColor
                        )
                        Text(
                            text = "DME Code: ${selectedCompany.dmeCode} • ${selectedCompany.location}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = "SELECT ENVIRONMENTAL HAZARD MODULE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Assessment Module Selection Grid
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val availableTypes = listOf(
                    AssessmentType.PERSONAL_AIRBORNE,
                    AssessmentType.PERSONAL_NOISE,
                    AssessmentType.THERMAL_STRESS,
                    AssessmentType.FALLOUT_DUST,
                    AssessmentType.AREA_SOURCE_NOISE,
                    AssessmentType.POTABLE_WATER_A,
                    AssessmentType.ILLUMINATION_DAY_NIGHT,
                    AssessmentType.ABLUTION_FACILITIES
                )
                availableTypes.forEach { type ->
                    val isSelected = selectedType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedType = type },
                        label = { Text(type.title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = activeTheme.primaryColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            OutlinedTextField(
                value = targetArea,
                onValueChange = { targetArea = it },
                label = { Text("Target Area / HEG / Sampling Station") },
                placeholder = { Text("e.g. Primary Crusher / HEG 01") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_task_heg_input")
            )

            OutlinedTextField(
                value = taskNotes,
                onValueChange = { taskNotes = it },
                label = { Text("Field Assessment Notes & Instructions (Optional)") },
                placeholder = { Text("e.g. Include 8-hour personal sampling run & acoustic calibration check") },
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_task_notes_input")
            )

            Button(
                onClick = {
                    onCreateTask(selectedType, selectedType.title, targetArea, taskNotes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = activeTheme.primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_new_task_button")
            ) {
                Icon(Icons.Default.PostAdd, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Offline Task to Room DB",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// THEME CHOOSER DIALOG: ("choose different themes")
// -------------------------------------------------------------------------------------------------
@Composable
fun ThemeChooserDialog(
    currentTheme: AppTheme,
    onSelectTheme: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = currentTheme.primaryColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Choose App Theme", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Customize the visual theme across the WorkForce Health SA portal:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AppTheme.values().forEach { theme ->
                    val isSelected = currentTheme == theme
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) theme.containerColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) theme.primaryColor else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectTheme(theme) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(theme.primaryColor, CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = theme.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) theme.onContainerColor else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = theme.description,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = theme.primaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", fontWeight = FontWeight.Bold, color = currentTheme.primaryColor)
            }
        }
    )
}

// -------------------------------------------------------------------------------------------------
// COMPANY ASSESSMENT SELECTOR HEADER
// -------------------------------------------------------------------------------------------------
@Composable
fun CompanyAssessmentSelectorHeader(
    selectedCompany: CompanySite,
    availableCompanies: List<CompanySite>,
    onSelectCompany: (CompanySite) -> Unit,
    onAddNewCompany: (CompanySite) -> Unit,
    siteInfo: SiteInfoEntity
) {
    var showCompanyDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("company_selector_header_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, SafetyRedPrimary.copy(alpha = 0.5f)),
                        shadowElevation = 3.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "Company Emblem",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = selectedCompany.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "DME ${selectedCompany.dmeCode}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedCompany.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Button(
                    onClick = { showCompanyDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CorporateNavy900),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("switch_company_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Switch", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }

    if (showCompanyDialog) {
        AlertDialog(
            onDismissRequest = { showCompanyDialog = false },
            title = { Text("Select Assessment Site", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableCompanies.forEach { company ->
                        val isSelected = company.id == selectedCompany.id
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectCompany(company)
                                    showCompanyDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(company.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("DME ${company.dmeCode} • ${company.location}", fontSize = 11.sp)
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCompanyDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// BASELINE SHIFT ENVIRONMENT CARD
// -------------------------------------------------------------------------------------------------
@Composable
fun ShiftEnvironmentCard() {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = null,
                        tint = SafetyRedPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Shift Ambient Environmental Baseline",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Calibrated 06:15",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AmbientMetricItem(label = "Dry Bulb", value = "24.5 °C")
                AmbientMetricItem(label = "Wet Bulb", value = "19.2 °C")
                AmbientMetricItem(label = "WBGT Index", value = "20.8 °C")
                AmbientMetricItem(label = "Baro Pressure", value = "94.2 kPa")
                AmbientMetricItem(label = "Wind / Vel", value = "0.8 m/s")
            }
        }
    }
}

@Composable
fun AmbientMetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// -------------------------------------------------------------------------------------------------
// ACTION TILE
// -------------------------------------------------------------------------------------------------
@Composable
fun DashboardActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = modifier
            .testTag(testTag)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = iconBg,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// STATUTORY ASSESSMENT MODULES MENU SECTION (WITH ICONS)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssessmentModulesMenuSection(
    activeTheme: AppTheme,
    assessments: List<AssessmentEntity>,
    onSelectModule: (AssessmentType) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All (10)") }

    val categories = listOf(
        "All (10)",
        "Acoustics",
        "Air & Dust",
        "Thermal",
        "Lighting",
        "Water & Sanitation"
    )

    val allModules = AssessmentType.values()

    val filteredModules = remember(selectedCategory) {
        when (selectedCategory) {
            "Acoustics" -> allModules.filter { it == AssessmentType.PERSONAL_NOISE || it == AssessmentType.AREA_SOURCE_NOISE }
            "Air & Dust" -> allModules.filter { it == AssessmentType.PERSONAL_AIRBORNE || it == AssessmentType.FALLOUT_DUST }
            "Thermal" -> allModules.filter { it == AssessmentType.THERMAL_STRESS }
            "Lighting" -> allModules.filter { it == AssessmentType.ILLUMINATION_DAY_NIGHT || it == AssessmentType.TMM_ILLUMINATION }
            "Water & Sanitation" -> allModules.filter { it == AssessmentType.POTABLE_WATER_A || it == AssessmentType.POTABLE_WATER_B || it == AssessmentType.ABLUTION_FACILITIES }
            else -> allModules.toList()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Assessment Modules Menu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "10 Statutory Environmental & Hygiene Modules",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = activeTheme.primaryColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "10 PROTOCOLS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = activeTheme.primaryColor,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                )
            }
        }

        // Category Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = activeTheme.primaryColor,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Modules Grid
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            filteredModules.forEach { module ->
                val countInDb = assessments.count { it.type == module }
                AssessmentModuleTile(
                    module = module,
                    countInDb = countInDb,
                    activeTheme = activeTheme,
                    onClick = { onSelectModule(module) },
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(min = 155.dp)
                )
            }
        }
    }
}

@Composable
fun AssessmentModuleTile(
    module: AssessmentType,
    countInDb: Int,
    activeTheme: AppTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, badgeColor) = when (module) {
        AssessmentType.PERSONAL_NOISE -> Icons.Default.VolumeUp to activeTheme.primaryColor
        AssessmentType.PERSONAL_AIRBORNE -> Icons.Default.Air to Color(0xFF0EA5E9)
        AssessmentType.THERMAL_STRESS -> Icons.Default.Thermostat to Color(0xFFF59E0B)
        AssessmentType.AREA_SOURCE_NOISE -> Icons.Default.Hearing to activeTheme.accentColor
        AssessmentType.ILLUMINATION_DAY_NIGHT -> Icons.Default.LightMode to Color(0xFFEAB308)
        AssessmentType.TMM_ILLUMINATION -> Icons.Default.DirectionsCar to Color(0xFF6366F1)
        AssessmentType.FALLOUT_DUST -> Icons.Default.Grain to Color(0xFF8B5CF6)
        AssessmentType.POTABLE_WATER_A -> Icons.Default.WaterDrop to Color(0xFF06B6D4)
        AssessmentType.POTABLE_WATER_B -> Icons.Default.Science to Color(0xFF3B82F6)
        AssessmentType.ABLUTION_FACILITIES -> Icons.Default.CleanHands to Color(0xFF10B981)
    }

    val standardTag = when (module) {
        AssessmentType.PERSONAL_NOISE -> "SANS 10083"
        AssessmentType.PERSONAL_AIRBORNE -> "NIOSH 7500"
        AssessmentType.THERMAL_STRESS -> "Reg 9.2 WBGT"
        AssessmentType.AREA_SOURCE_NOISE -> "Acoustic Zoning"
        AssessmentType.ILLUMINATION_DAY_NIGHT -> "SANS 10114-1"
        AssessmentType.TMM_ILLUMINATION -> "Fleet 10 Lux"
        AssessmentType.FALLOUT_DUST -> "ASTM D1739"
        AssessmentType.POTABLE_WATER_A -> "SANS 241 (A)"
        AssessmentType.POTABLE_WATER_B -> "SANS 241 (B)"
        AssessmentType.ABLUTION_FACILITIES -> "MHSA Sanitation"
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("assessment_module_tile_${module.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = module.title,
                            tint = badgeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = badgeColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "$countInDb in DB",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = module.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = standardTag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = badgeColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = module.shortDesc,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// ADMIN & BACKEND PORTAL CARD
// -------------------------------------------------------------------------------------------------
@Composable
fun AdminBackendPortalCard(
    activeTheme: AppTheme,
    currentRole: UserRole,
    assessmentsCount: Int,
    syncState: SyncState,
    onNavigateToAdminBackend: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CorporateNavy900),
        border = BorderStroke(1.2.dp, activeTheme.primaryColor.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateToAdminBackend)
            .testTag("admin_backend_portal_banner_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = activeTheme.primaryColor,
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Admin & Backend Module",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "BACKEND READY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "Room SQLite ($assessmentsCount records) • Sync: ${syncState.name} • Roles & Master Config",
                        fontSize = 11.sp,
                        color = Slate300
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onNavigateToAdminBackend,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = activeTheme.primaryColor),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Dns, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Portal", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
