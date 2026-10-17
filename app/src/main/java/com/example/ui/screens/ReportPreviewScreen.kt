package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.EquipmentEntity
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.local.entity.SiteInfoEntity
import com.example.ui.components.ComplianceIndicator
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialNavy900
import com.example.ui.theme.IndustrialSlate100
import com.example.ui.theme.IndustrialSlate200
import com.example.ui.theme.IndustrialSlate700
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusSuccess
import com.example.util.PdfReportGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPreviewScreen(
    assessment: AssessmentEntity,
    siteInfo: SiteInfoEntity = SiteInfoEntity(),
    actions: List<RemedialActionEntity> = emptyList(),
    equipmentList: List<EquipmentEntity> = emptyList(),
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by remember { mutableStateOf(0) } // 0: Field Data, 1: Statutory Summary & Actions, 2: Calibrated Equipment
    var isGeneratingPdf by remember { mutableStateOf(false) }
    var generatedPdfFile by remember { mutableStateOf<File?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }

    fun triggerPdfExport(shareImmediately: Boolean = false) {
        if (isGeneratingPdf) return
        isGeneratingPdf = true
        coroutineScope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    PdfReportGenerator.generateSummaryPdf(
                        context = context,
                        siteInfo = siteInfo,
                        assessments = listOf(assessment),
                        actions = actions,
                        equipmentList = equipmentList,
                        targetAssessment = assessment
                    )
                }
                generatedPdfFile = file
                isGeneratingPdf = false

                if (shareImmediately) {
                    PdfReportGenerator.openOrSharePdf(context, file, share = true)
                } else {
                    showExportDialog = true
                }
                snackbarHostState.showSnackbar("PDF Report generated: ${file.name}")
            } catch (e: Exception) {
                isGeneratingPdf = false
                snackbarHostState.showSnackbar("Error generating PDF: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Statutory Assessment Report", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            Text(assessment.documentRef, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("report_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { triggerPdfExport(shareImmediately = false) },
                        modifier = Modifier.testTag("export_pdf_button"),
                        enabled = !isGeneratingPdf
                    ) {
                        if (isGeneratingPdf) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF", tint = Color.White)
                        }
                    }
                    IconButton(
                        onClick = { triggerPdfExport(shareImmediately = true) },
                        enabled = !isGeneratingPdf
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share PDF", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndustrialNavy900)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { triggerPdfExport(shareImmediately = false) },
                        modifier = Modifier.weight(1f),
                        enabled = !isGeneratingPdf
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF", tint = SafetyOrangePrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export PDF", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { triggerPdfExport(shareImmediately = true) },
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyOrangePrimary),
                        modifier = Modifier.weight(1f),
                        enabled = !isGeneratingPdf
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share PDF", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(IndustrialSlate100)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IndustrialNavy800
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Part 1: Field Data", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Part 2: Actions (${actions.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Equipment (${equipmentList.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Quick Export Callout Banner
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = IndustrialNavy800),
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
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(SafetyOrangePrimary.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        tint = SafetyOrangePrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Official Statutory PDF Report",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Combines assessment records, remedial actions, & equipment",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Button(
                                onClick = { triggerPdfExport(shareImmediately = false) },
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrangePrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Generate", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Official Document Paper Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Letterhead with Branded Attached Logo
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        modifier = Modifier.size(54.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        color = IndustrialNavy800,
                                        shadowElevation = 4.dp
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.app_logo),
                                            contentDescription = "Workforce Health Logo",
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = "WORKFORCE HEALTH",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Black,
                                            color = IndustrialNavy800,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "OCCUPATIONAL HYGIENE FIELD ASSESSMENT",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SafetyOrangePrimary
                                        )
                                        Text(
                                            text = "Mine Health & Safety Act (Act 29 of 1996)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Surface(
                                    color = SafetyOrangePrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "CONFIDENTIAL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SafetyOrangePrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp))

                            // Site Info Table
                            ReportDataGrid(
                                rows = listOf(
                                    "Client Name" to siteInfo.clientName,
                                    "Mine / Operation" to "${siteInfo.mineName} (DME Code: ${siteInfo.dmeCode})",
                                    "Physical Location" to siteInfo.location,
                                    "Mine Manager" to siteInfo.mineManager,
                                    "Appointed Hygienist" to siteInfo.siteHygienist,
                                    "Lead Field Assessor" to "${siteInfo.assessorName} (${siteInfo.assessorCert})",
                                    "Assessment Date" to assessment.visitDate,
                                    "Document Ref #" to assessment.documentRef,
                                    "Assessment Module" to assessment.type.title
                                )
                            )

                            Divider(modifier = Modifier.padding(vertical = 12.dp))

                            when (selectedTab) {
                                0 -> {
                                    // PART 1: FIELD DATA & MEASUREMENTS
                                    Text(
                                        text = "1. Environmental Shift Baseline",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Temperature: Wet Bulb 21.4°C | Dry Bulb 26.8°C | Globe 28.5°C | Calculated WBGT: 23.2°C\nWind Speed: 1.8 m/s | Relative Humidity: 64% | Weather: Partly cloudy, calm wind",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = IndustrialSlate700
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "2. Field Observations & Measurements",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(IndustrialSlate100, RoundedCornerShape(6.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text("Target Area / HEG: ${assessment.targetAreaOrHeg}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                            if (assessment.employeeInitialsSurname.isNotBlank()) {
                                                Text("Employee Monitored: ${assessment.employeeInitialsSurname} (Company #${assessment.employeeCompanyNo})", fontSize = 13.sp)
                                            }
                                            Text("Raw Data Log: ${assessment.specificDataJson}", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.DarkGray)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "3. Compliance Finding Summary",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    ComplianceIndicator(isCompliant = assessment.complianceFlag)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = assessment.findingsSummary.ifBlank { "All measured parameters verified within statutory limits. Regular sampling schedule maintained." },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = IndustrialSlate700
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))
                                    // Signatures Section
                                    Text(
                                        text = "4. Signatures & Field Acknowledgements",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        SignatureStampBox(
                                            title = "Employee / Supervisor",
                                            name = assessment.employeeInitialsSurname.ifBlank { "Representative" },
                                            status = if (assessment.signaturePath.isNotBlank()) "SIGNED ELECTRONICALLY" else "PENDING SIGNATURE"
                                        )
                                        SignatureStampBox(
                                            title = "Field Assessor",
                                            name = siteInfo.assessorName,
                                            status = "VERIFIED ON SITE"
                                        )
                                    }
                                }
                                1 -> {
                                    // PART 2: REMEDIAL ACTIONS & STATUTORY RECOMMENDATIONS
                                    Text(
                                        text = "PART 2: STATUTORY RECOMMENDATIONS & CORRECTIVE ACTIONS",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Completed by Appointed Occupational Hygienist: ${siteInfo.siteHygienist}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "1. Technical Evaluation & MHSA Section 12 Mandate",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (!assessment.complianceFlag) {
                                            "The measured results exceed statutory occupational exposure limits. Under Section 12(1) of the MHSA, ${siteInfo.clientName} must implement immediate hierarchy-of-controls remedial actions. Employee has been fitted with verified PPE."
                                        } else {
                                            "The measured parameters comply with statutory Mine Health and Safety Act standards. Engineering controls and current work practices provide satisfactory worker protection."
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = IndustrialSlate700
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "2. Active Remedial Action Register (${actions.size} Items)",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (actions.isEmpty()) {
                                        Text(
                                            text = "No open remedial action items recorded for this assessment.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            actions.forEach { action ->
                                                Card(
                                                    colors = CardDefaults.cardColors(containerColor = IndustrialSlate100),
                                                    shape = RoundedCornerShape(8.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Column(modifier = Modifier.padding(10.dp)) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = "Action #${action.id} • ${action.priority.name}",
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 11.sp,
                                                                color = SafetyOrangePrimary
                                                            )
                                                            Text(
                                                                text = action.status.name,
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 10.sp,
                                                                color = if (action.status == com.example.data.model.ActionStatus.CLOSED) StatusSuccess else StatusCritical
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = action.correctiveAction.ifBlank { action.findingDescription },
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = IndustrialNavy800
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = "Assigned: ${action.assignedPerson} | Due: ${action.dueDate}",
                                                            fontSize = 11.sp,
                                                            color = IndustrialSlate700
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        SignatureStampBox(
                                            title = "Occupational Hygienist",
                                            name = siteInfo.siteHygienist,
                                            status = "SANAS ACCREDITED REVIEW"
                                        )
                                        SignatureStampBox(
                                            title = "Mine Management Review",
                                            name = siteInfo.mineManager,
                                            status = "ACKNOWLEDGED"
                                        )
                                    }
                                }
                                2 -> {
                                    // PART 3: CERTIFIED EQUIPMENT REGISTER
                                    Text(
                                        text = "CERTIFIED EQUIPMENT & INSTRUMENTATION REGISTER",
                                        fontWeight = FontWeight.Bold,
                                        color = IndustrialNavy800
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Traceable SANAS/ILAC calibration instruments used during monitoring:",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (equipmentList.isEmpty()) {
                                        Text(
                                            text = "Default statutory equipment register configured.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            equipmentList.forEach { eq ->
                                                Card(
                                                    colors = CardDefaults.cardColors(containerColor = IndustrialSlate100),
                                                    shape = RoundedCornerShape(8.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(10.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = eq.equipmentName.ifBlank { eq.model },
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 12.sp,
                                                                color = IndustrialNavy800
                                                            )
                                                            Text(
                                                                text = "S/N: ${eq.serialNumber} | Param: ${eq.parameterMeasured}",
                                                                fontSize = 11.sp,
                                                                color = IndustrialSlate700
                                                            )
                                                            Text(
                                                                text = "Cal. Valid to: ${eq.nextCalibrationDate} (Cert: ${eq.calibrationCertNo})",
                                                                fontSize = 10.sp,
                                                                color = Color.Gray
                                                            )
                                                        }
                                                        Surface(
                                                            color = if (eq.status == "VALID" || eq.status.isBlank()) StatusSuccess.copy(alpha = 0.15f) else SafetyOrangePrimary.copy(alpha = 0.15f),
                                                            shape = RoundedCornerShape(4.dp)
                                                        ) {
                                                            Text(
                                                                text = eq.status.ifBlank { "VALID" },
                                                                fontSize = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = if (eq.status == "VALID" || eq.status.isBlank()) StatusSuccess else SafetyOrangePrimary,
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
                    }
                }
            }
        }
    }

    // PDF Export Success Dialog
    if (showExportDialog && generatedPdfFile != null) {
        val file = generatedPdfFile!!
        val fileSizeKb = file.length() / 1024

        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = SafetyOrangePrimary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text("Statutory PDF Report Generated", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "The official occupational hygiene assessment summary report has been compiled and saved to local storage.",
                        fontSize = 13.sp
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialSlate100),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("File: ${file.name}", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Size: $fileSizeKb KB (2 Pages, A4)", fontSize = 11.sp, color = Color.Gray)
                            Text("Includes: Site Profile, Field Data, Remedial Actions, Equipment Register & Signatures", fontSize = 10.sp, color = IndustrialNavy800)
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
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialNavy800)
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

@Composable
fun ReportDataGrid(rows: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { (label, value) ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "$label:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = IndustrialNavy800,
                    modifier = Modifier.width(140.dp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = IndustrialSlate700,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SignatureStampBox(title: String, name: String, status: String) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .border(1.dp, IndustrialSlate200, RoundedCornerShape(6.dp))
            .background(IndustrialSlate100.copy(alpha = 0.5f))
            .padding(10.dp)
    ) {
        Column {
            Text(text = title, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IndustrialNavy800)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "✓ $status", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = StatusSuccess)
        }
    }
}
