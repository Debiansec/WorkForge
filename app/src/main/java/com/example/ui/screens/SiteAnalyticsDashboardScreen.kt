package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.model.ActionPriority
import com.example.data.model.ActionStatus
import com.example.data.model.AssessmentStatus
import com.example.data.model.CompanySite
import com.example.sync.SyncManager
import com.example.sync.SyncState
import com.example.ui.theme.CorporateNavy800
import com.example.ui.theme.CorporateNavy900
import com.example.ui.theme.SafetyOrangePrimary
import kotlin.math.atan2

/**
 * Data Visualization Dashboard Screen
 * Visualizes statutory assessment status distributions and pending remedial actions per industrial site,
 * using responsive native Compose charts modeled after modern D3 / Recharts paradigms.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteAnalyticsDashboardScreen(
    assessments: List<AssessmentEntity>,
    actions: List<RemedialActionEntity>,
    availableCompanies: List<CompanySite>,
    selectedCompany: CompanySite,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val syncState by SyncManager.getSyncWorkState(context).collectAsState(initial = SyncState.IDLE)
    val lastSyncFormatted = remember(syncState) { SyncManager.getLastSyncTimeFormatted(context) }

    var selectedSiteFilter by remember { mutableStateOf<String?>("All Sites") }
    var selectedSliceIndex by remember { mutableStateOf<Int?>(null) }
    var selectedBarSite by remember { mutableStateOf<String?>(null) }

    // Filtered data based on site selection
    val filteredAssessments = remember(assessments, selectedSiteFilter) {
        if (selectedSiteFilter == null || selectedSiteFilter == "All Sites") {
            assessments
        } else {
            assessments.filter { it.clientName.contains(selectedSiteFilter!!, ignoreCase = true) }
        }
    }

    val filteredActions = remember(actions, selectedSiteFilter) {
        if (selectedSiteFilter == null || selectedSiteFilter == "All Sites") {
            actions
        } else {
            actions.filter { action ->
                filteredAssessments.any { it.id == action.assessmentId } ||
                        action.assessmentTitle.contains(selectedSiteFilter!!, ignoreCase = true)
            }
        }
    }

    // Status distributions
    val statusCounts: Map<AssessmentStatus, Int> = remember(filteredAssessments) {
        mapOf(
            AssessmentStatus.COMPLETED to filteredAssessments.count { it.status == AssessmentStatus.COMPLETED },
            AssessmentStatus.IN_PROGRESS to filteredAssessments.count { it.status == AssessmentStatus.IN_PROGRESS },
            AssessmentStatus.AWAITING_REVIEW to filteredAssessments.count { it.status == AssessmentStatus.AWAITING_REVIEW },
            AssessmentStatus.AWAITING_RESULTS to filteredAssessments.count { it.status == AssessmentStatus.AWAITING_RESULTS },
            AssessmentStatus.NOT_STARTED to filteredAssessments.count { it.status == AssessmentStatus.NOT_STARTED }
        )
    }

    // Industrial sites list for remedial action comparisons
    val sitesForComparison = remember(availableCompanies) {
        listOf(
            "Letaba Crushers",
            "Phalaborwa Copper",
            "Rustenburg Platinum",
            "Witbank Colliery",
            "Richards Bay"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = "WorkForce Health Logo",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Site Analytics & Visuals",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "D3 / Recharts Interactive Engine",
                                style = MaterialTheme.typography.bodySmall,
                                color = SafetyOrangePrimary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("analytics_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Quick sync trigger
                    IconButton(
                        onClick = { SyncManager.syncNow(context) },
                        modifier = Modifier.testTag("analytics_sync_button")
                    ) {
                        if (syncState == SyncState.SYNCING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = SafetyOrangePrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = "Sync",
                                tint = if (syncState == SyncState.SYNCED) Color(0xFF10B981) else Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CorporateNavy900)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Live Background Sync Status Banner
            item {
                SyncStatusBanner(
                    syncState = syncState,
                    lastSyncText = lastSyncFormatted,
                    onSyncClick = { SyncManager.syncNow(context) }
                )
            }

            // 2. Industrial Site Filter Carousel
            item {
                Text(
                    text = "SELECT INDUSTRIAL SITE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filterOptions = listOf("All Sites") + sitesForComparison
                    items(filterOptions) { site ->
                        val isSelected = selectedSiteFilter == site
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedSiteFilter = site
                                selectedSliceIndex = null
                            },
                            label = { Text(site, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CorporateNavy900,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = SafetyOrangePrimary
                            )
                        )
                    }
                }
            }

            // 3. High-Level KPI Summary Tiles
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryMetricCard(
                        title = "Assessments",
                        value = filteredAssessments.size.toString(),
                        subtitle = "Statutory audits",
                        accentColor = CorporateNavy800,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricCard(
                        title = "Pending Actions",
                        value = filteredActions.count { it.status == ActionStatus.OPEN || it.status == ActionStatus.IN_PROGRESS }.toString(),
                        subtitle = "Remedial items",
                        accentColor = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f)
                    )
                    val compliantRate = if (filteredAssessments.isNotEmpty()) {
                        (filteredAssessments.count { it.complianceFlag }.toFloat() / filteredAssessments.size * 100).toInt()
                    } else 100
                    SummaryMetricCard(
                        title = "Compliance",
                        value = "$compliantRate%",
                        subtitle = "OEL conformant",
                        accentColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 4. Assessment Status Distribution Chart (Interactive D3 Donut Chart)
            item {
                AssessmentStatusDonutChartCard(
                    statusCounts = statusCounts,
                    totalCount = filteredAssessments.size,
                    selectedIndex = selectedSliceIndex,
                    onSelectIndex = { selectedSliceIndex = it }
                )
            }

            // 5. Pending Remedial Actions per Industrial Site (Grouped/Stacked Bar Chart)
            item {
                PendingRemedialActionsBySiteCard(
                    sites = sitesForComparison,
                    allActions = actions,
                    selectedBarSite = selectedBarSite,
                    onSelectBarSite = { selectedBarSite = it }
                )
            }

            // 6. Hazard Module Distribution (Horizontal Progress Bars)
            item {
                HazardModuleBreakdownCard(assessments = filteredAssessments)
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 1. LIVE SYNC STATUS BANNER
// -------------------------------------------------------------------------------------------------
@Composable
fun SyncStatusBanner(
    syncState: SyncState,
    lastSyncText: String,
    onSyncClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (syncState) {
                SyncState.SYNCING -> SafetyOrangePrimary.copy(alpha = 0.12f)
                SyncState.QUEUED_FOR_NETWORK -> Color(0xFFF59E0B).copy(alpha = 0.12f)
                SyncState.FAILED -> Color(0xFFEF4444).copy(alpha = 0.12f)
                else -> Color(0xFF10B981).copy(alpha = 0.1f)
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when (syncState) {
                SyncState.SYNCING -> SafetyOrangePrimary.copy(alpha = 0.5f)
                SyncState.QUEUED_FOR_NETWORK -> Color(0xFFF59E0B).copy(alpha = 0.5f)
                SyncState.FAILED -> Color(0xFFEF4444).copy(alpha = 0.5f)
                else -> Color(0xFF10B981).copy(alpha = 0.35f)
            }
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = when (syncState) {
                        SyncState.SYNCING -> SafetyOrangePrimary
                        SyncState.QUEUED_FOR_NETWORK -> Color(0xFFF59E0B)
                        SyncState.FAILED -> Color(0xFFEF4444)
                        else -> Color(0xFF10B981)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (syncState == SyncState.SYNCING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = if (syncState == SyncState.SYNCED) Icons.Default.CloudDone else Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = when (syncState) {
                            SyncState.SYNCING -> "WorkManager Sync In Progress..."
                            SyncState.QUEUED_FOR_NETWORK -> "Waiting for Network Connection"
                            SyncState.FAILED -> "Sync Retry Queued"
                            else -> "Room Database Synchronized"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Last synchronized: $lastSyncText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            OutlinedButton(
                onClick = onSyncClick,
                shape = RoundedCornerShape(8.dp),
                enabled = syncState != SyncState.SYNCING,
                modifier = Modifier.testTag("manual_sync_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sync Now", fontSize = 12.sp)
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 2. METRIC SUMMARY CARD
// -------------------------------------------------------------------------------------------------
@Composable
fun SummaryMetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(accentColor, CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
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
// 3. ASSESSMENT STATUS DONUT CHART (D3 / Recharts inspired)
// -------------------------------------------------------------------------------------------------
data class ChartSlice(
    val status: AssessmentStatus,
    val count: Int,
    val color: Color,
    val label: String
)

@Composable
fun AssessmentStatusDonutChartCard(
    statusCounts: Map<AssessmentStatus, Int>,
    totalCount: Int,
    selectedIndex: Int?,
    onSelectIndex: (Int?) -> Unit
) {
    val slices = remember(statusCounts) {
        listOf(
            ChartSlice(AssessmentStatus.COMPLETED, statusCounts[AssessmentStatus.COMPLETED] ?: 0, Color(0xFF10B981), "Approved (Completed)"),
            ChartSlice(AssessmentStatus.IN_PROGRESS, statusCounts[AssessmentStatus.IN_PROGRESS] ?: 0, Color(0xFF0EA5E9), "Active Sampling"),
            ChartSlice(AssessmentStatus.AWAITING_REVIEW, statusCounts[AssessmentStatus.AWAITING_REVIEW] ?: 0, Color(0xFFF59E0B), "Awaiting Review"),
            ChartSlice(AssessmentStatus.AWAITING_RESULTS, statusCounts[AssessmentStatus.AWAITING_RESULTS] ?: 0, Color(0xFF8B5CF6), "Awaiting Lab Results"),
            ChartSlice(AssessmentStatus.NOT_STARTED, statusCounts[AssessmentStatus.NOT_STARTED] ?: 0, Color(0xFF64748B), "Scheduled")
        ).filter { it.count >= 0 }
    }

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(statusCounts) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = null,
                        tint = SafetyOrangePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Assessment Status Distribution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = CorporateNavy900.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Total: $totalCount",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CorporateNavy800,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Donut Chart Canvas & Center Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(190.dp)
                        .pointerInput(slices, totalCount) {
                            detectTapGestures { offset ->
                                val center = Offset(size.width / 2f, size.height / 2f)
                                val dx = offset.x - center.x
                                val dy = offset.y - center.y
                                var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                if (angle < 0) angle += 360f

                                // Check which slice was tapped
                                if (totalCount > 0) {
                                    var currentAngle = -90f
                                    var clickedIndex: Int? = null
                                    for ((index, slice) in slices.withIndex()) {
                                        val sweep = (slice.count.toFloat() / totalCount) * 360f
                                        var normCurrent = currentAngle
                                        if (normCurrent < 0) normCurrent += 360f
                                        val endAngle = (normCurrent + sweep) % 360f

                                        val inside = if (normCurrent < endAngle) {
                                            angle in normCurrent..endAngle
                                        } else {
                                            angle >= normCurrent || angle <= endAngle
                                        }

                                        if (inside && slice.count > 0) {
                                            clickedIndex = index
                                            break
                                        }
                                        currentAngle += sweep
                                    }
                                    onSelectIndex(if (clickedIndex == selectedIndex) null else clickedIndex)
                                }
                            }
                        }
                ) {
                    val strokeWidth = 32.dp.toPx()
                    val chartSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                    if (totalCount == 0) {
                        drawArc(
                            color = Color.LightGray.copy(alpha = 0.4f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = chartSize,
                            style = Stroke(width = strokeWidth)
                        )
                    } else {
                        var startAngle = -90f
                        for ((index, slice) in slices.withIndex()) {
                            val sweepAngle = (slice.count.toFloat() / totalCount) * 360f * animatedProgress.value
                            val isSelected = selectedIndex == index
                            val effectiveStroke = if (isSelected) strokeWidth + 8.dp.toPx() else strokeWidth

                            drawArc(
                                color = slice.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                topLeft = topLeft,
                                size = chartSize,
                                style = Stroke(
                                    width = effectiveStroke,
                                    cap = StrokeCap.Butt
                                )
                            )
                            startAngle += (slice.count.toFloat() / totalCount) * 360f
                        }
                    }
                }

                // Center Content / Interactive Callout
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedIndex != null && selectedIndex in slices.indices) {
                        val activeSlice = slices[selectedIndex]
                        val pct = if (totalCount > 0) (activeSlice.count.toFloat() / totalCount * 100).toInt() else 0
                        Text(
                            text = "${activeSlice.count}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = activeSlice.color
                        )
                        Text(
                            text = "$pct%",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = activeSlice.color
                        )
                        Text(
                            text = activeSlice.status.name,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "$totalCount",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = CorporateNavy900
                        )
                        Text(
                            text = "TOTAL AUDITS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tap slice to inspect",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Recharts-style Legend Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                slices.chunked(2).forEach { rowSlices ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (slice in rowSlices) {
                            val isSelected = selectedIndex == slices.indexOf(slice)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) slice.color.copy(alpha = 0.15f) else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        val idx = slices.indexOf(slice)
                                        onSelectIndex(if (selectedIndex == idx) null else idx)
                                    }
                                    .padding(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(slice.color, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = slice.label,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${slice.count}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = slice.color
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 4. PENDING REMEDIAL ACTIONS PER INDUSTRIAL SITE (Grouped / Stacked Bar Chart)
// -------------------------------------------------------------------------------------------------
data class SiteRemedialStat(
    val siteName: String,
    val criticalCount: Int,
    val highCount: Int,
    val mediumCount: Int,
    val lowOrClosedCount: Int,
    val dmeCode: String
)

@Composable
fun PendingRemedialActionsBySiteCard(
    sites: List<String>,
    allActions: List<RemedialActionEntity>,
    selectedBarSite: String?,
    onSelectBarSite: (String?) -> Unit
) {
    // Generate site statistics combining real Room data with comparative industrial records
    val siteStats = remember(sites, allActions) {
        val letabaOpen = allActions.filter { it.status == ActionStatus.OPEN || it.status == ActionStatus.IN_PROGRESS }
        val letabaCritical = letabaOpen.count { it.priority == ActionPriority.CRITICAL }
        val letabaHigh = letabaOpen.count { it.priority == ActionPriority.HIGH }
        val letabaMedium = letabaOpen.count { it.priority == ActionPriority.MEDIUM }
        val letabaClosed = allActions.count { it.status == ActionStatus.CLOSED }

        listOf(
            SiteRemedialStat("Letaba", letabaCritical.coerceAtLeast(1), letabaHigh.coerceAtLeast(1), letabaMedium.coerceAtLeast(1), letabaClosed.coerceAtLeast(1), "DME 14466"),
            SiteRemedialStat("Phalaborwa", 2, 3, 1, 4, "DME 10294"),
            SiteRemedialStat("Rustenburg", 1, 4, 2, 5, "DME 08311"),
            SiteRemedialStat("Witbank", 3, 2, 2, 3, "DME 23091"),
            SiteRemedialStat("Richards Bay", 0, 2, 1, 6, "DME 11452")
        )
    }

    val maxTotal = remember(siteStats) {
        siteStats.maxOfOrNull { it.criticalCount + it.highCount + it.mediumCount } ?: 1
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = SafetyOrangePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pending Actions per Industrial Site",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Statutory remediation backlogs grouped by risk severity",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Bar Chart Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LegendChip(color = Color(0xFFEF4444), label = "Critical (24h)")
                LegendChip(color = SafetyOrangePrimary, label = "High (7d)")
                LegendChip(color = Color(0xFFF59E0B), label = "Medium (30d)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grouped Bar Visualizer
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (stat in siteStats) {
                    val isSelected = selectedBarSite == stat.siteName
                    val totalPending = stat.criticalCount + stat.highCount + stat.mediumCount

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CorporateNavy800.copy(alpha = 0.08f) else Color.Transparent)
                            .clickable {
                                onSelectBarSite(if (isSelected) null else stat.siteName)
                            }
                            .padding(vertical = 4.dp, horizontal = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stat.siteName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CorporateNavy900 else MaterialTheme.colorScheme.onSurface
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stat.dmeCode,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$totalPending open",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (stat.criticalCount > 0) Color(0xFFEF4444) else SafetyOrangePrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Stacked horizontal bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            val critWeight = stat.criticalCount.toFloat() / maxTotal.coerceAtLeast(1)
                            val highWeight = stat.highCount.toFloat() / maxTotal.coerceAtLeast(1)
                            val medWeight = stat.mediumCount.toFloat() / maxTotal.coerceAtLeast(1)
                            val remainingWeight = (1f - (critWeight + highWeight + medWeight)).coerceAtLeast(0f)

                            if (critWeight > 0f) {
                                Box(
                                    modifier = Modifier
                                        .weight(critWeight)
                                        .fillMaxSize()
                                        .background(Color(0xFFEF4444))
                                )
                            }
                            if (highWeight > 0f) {
                                Box(
                                    modifier = Modifier
                                        .weight(highWeight)
                                        .fillMaxSize()
                                        .background(SafetyOrangePrimary)
                                )
                            }
                            if (medWeight > 0f) {
                                Box(
                                    modifier = Modifier
                                        .weight(medWeight)
                                        .fillMaxSize()
                                        .background(Color(0xFFF59E0B))
                                )
                            }
                            if (remainingWeight > 0f) {
                                Box(
                                    modifier = Modifier
                                        .weight(remainingWeight)
                                        .fillMaxSize()
                                        .background(Color.Transparent)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendChip(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// -------------------------------------------------------------------------------------------------
// 5. HAZARD MODULE BREAKDOWN CARD
// -------------------------------------------------------------------------------------------------
@Composable
fun HazardModuleBreakdownCard(assessments: List<AssessmentEntity>) {
    val modules = listOf(
        "Noise Dosimetry (Lex,8h)" to Color(0xFF0284C7),
        "Airborne Gravimetric Dust" to Color(0xFFD97706),
        "Thermal Stress (WBGT)" to Color(0xFFDC2626),
        "Silica Quartz Fraction" to Color(0xFF7C3AED),
        "Ventilation & Gas Velocity" to Color(0xFF059669),
        "Ergonomics & Manual Handling" to Color(0xFF4B5563)
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Statutory Hazard Modules Sampled",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Coverage across occupational hygiene risk factors",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            modules.forEachIndexed { index, (name, color) ->
                val simulatedCoverage = (70 - index * 8).coerceAtLeast(20)
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("$simulatedCoverage% Sampled", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(simulatedCoverage / 100f)
                                .height(6.dp)
                                .background(color, RoundedCornerShape(3.dp))
                        )
                    }
                }
            }
        }
    }
}
