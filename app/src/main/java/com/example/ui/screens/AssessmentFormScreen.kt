package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.entity.AssessmentEntity
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.ui.components.PhotoAttachmentView
import com.example.ui.components.ShiftEnvironmentCard
import com.example.ui.components.SignaturePad
import com.example.ui.components.StatusBadge
import com.example.ui.screens.forms.AblutionForm
import com.example.ui.screens.forms.AreaSourceNoiseForm
import com.example.ui.screens.forms.FalloutDustForm
import com.example.ui.screens.forms.IlluminationForm
import com.example.ui.screens.forms.PersonalAirborneForm
import com.example.ui.screens.forms.PersonalNoiseForm
import com.example.ui.screens.forms.PotableWaterAForm
import com.example.ui.screens.forms.PotableWaterBForm
import com.example.ui.screens.forms.ThermalStressForm
import com.example.ui.screens.forms.TmmIlluminationForm
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialNavy900
import com.example.ui.theme.IndustrialSlate100
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusSuccess
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentFormScreen(
    initialAssessment: AssessmentEntity,
    onSaveAssessment: (AssessmentEntity) -> Unit,
    onNavigateBack: () -> Unit,
    onPreviewReport: (AssessmentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentStatus by remember { mutableStateOf(initialAssessment.status) }
    var photoUri by remember { mutableStateOf(initialAssessment.photoUri) }
    var signaturePath by remember { mutableStateOf(initialAssessment.signaturePath) }
    var reviewerSignaturePath by remember { mutableStateOf(initialAssessment.reviewerSignaturePath) }

    // Parse initial specific JSON into a mutable map for UI editing
    val formData = remember {
        mutableStateMapOf<String, String>().apply {
            put("employeeInitialsSurname", initialAssessment.employeeInitialsSurname)
            put("employeeCompanyNo", initialAssessment.employeeCompanyNo)
            put("targetAreaOrHeg", initialAssessment.targetAreaOrHeg)
            try {
                if (initialAssessment.specificDataJson.isNotBlank()) {
                    val jsonObj = JSONObject(initialAssessment.specificDataJson)
                    val keys = jsonObj.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        put(key, jsonObj.optString(key, ""))
                    }
                }
            } catch (_: Exception) {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = initialAssessment.documentRef,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = initialAssessment.type.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("assessment_form_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    StatusBadge(
                        status = currentStatus,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndustrialNavy900)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val updated = buildUpdatedEntity(
                                base = initialAssessment,
                                formData = formData,
                                status = AssessmentStatus.IN_PROGRESS,
                                photo = photoUri,
                                sig = signaturePath,
                                revSig = reviewerSignaturePath
                            )
                            onSaveAssessment(updated)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Draft saved locally")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_draft_button")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Draft")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save Draft")
                    }

                    Button(
                        onClick = {
                            currentStatus = AssessmentStatus.AWAITING_REVIEW
                            val updated = buildUpdatedEntity(
                                base = initialAssessment,
                                formData = formData,
                                status = AssessmentStatus.AWAITING_REVIEW,
                                photo = photoUri,
                                sig = signaturePath,
                                revSig = reviewerSignaturePath
                            )
                            onSaveAssessment(updated)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Assessment submitted for Hygienist review")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyOrangePrimary),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("submit_review_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Submit")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Submit Review", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val current = buildUpdatedEntity(
                                base = initialAssessment,
                                formData = formData,
                                status = currentStatus,
                                photo = photoUri,
                                sig = signaturePath,
                                revSig = reviewerSignaturePath
                            )
                            onPreviewReport(current)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("report_preview_button")
                    ) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = "Report")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Report")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(IndustrialSlate100),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Shift Environment baseline
            item {
                ShiftEnvironmentCard()
            }

            // The specific module form
            item {
                when (initialAssessment.type) {
                    AssessmentType.PERSONAL_NOISE -> PersonalNoiseForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.PERSONAL_AIRBORNE -> PersonalAirborneForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.THERMAL_STRESS -> ThermalStressForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.AREA_SOURCE_NOISE -> AreaSourceNoiseForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.ILLUMINATION_DAY_NIGHT -> IlluminationForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.TMM_ILLUMINATION -> TmmIlluminationForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.FALLOUT_DUST -> FalloutDustForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.POTABLE_WATER_A -> PotableWaterAForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.POTABLE_WATER_B -> PotableWaterBForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                    AssessmentType.ABLUTION_FACILITIES -> AblutionForm(
                        formData = formData,
                        onFieldChanged = { k, v -> formData[k] = v }
                    )
                }
            }

            // Photo Evidence
            item {
                PhotoAttachmentView(
                    photoUri = photoUri,
                    documentRef = initialAssessment.documentRef,
                    onPhotoChanged = { photoUri = it }
                )
            }

            // Employee / Assessor Digital Signature
            item {
                SignaturePad(
                    title = "Assessor & Employee Acknowledgement",
                    subtitle = "Draw signature on screen to verify and seal assessment",
                    initialSignatureStatus = signaturePath,
                    assessmentRef = initialAssessment.documentRef,
                    onSignatureConfirmed = { signaturePath = it }
                )
            }
        }
    }
}

fun buildUpdatedEntity(
    base: AssessmentEntity,
    formData: Map<String, String>,
    status: AssessmentStatus,
    photo: String?,
    sig: String,
    revSig: String
): AssessmentEntity {
    val json = JSONObject()
    formData.forEach { (k, v) -> json.put(k, v) }

    val employeeName = formData["employeeInitialsSurname"] ?: base.employeeInitialsSurname
    val companyNo = formData["employeeCompanyNo"] ?: base.employeeCompanyNo
    val targetArea = formData["targetAreaOrHeg"] ?: base.targetAreaOrHeg

    // Check compliance flags based on type
    var isCompliant = true
    if (base.type == AssessmentType.PERSONAL_NOISE) {
        val lex8h = formData["lex8h"]?.toDoubleOrNull()
        if (lex8h != null && lex8h > 85.0) isCompliant = false
    } else if (base.type == AssessmentType.TMM_ILLUMINATION) {
        val lux = formData["measuredLux"]?.toDoubleOrNull()
        if (lux != null && lux < 10.0) isCompliant = false
    }

    return base.copy(
        status = status,
        employeeInitialsSurname = employeeName,
        employeeCompanyNo = companyNo,
        targetAreaOrHeg = targetArea,
        specificDataJson = json.toString(),
        complianceFlag = isCompliant,
        photoUri = photo,
        signaturePath = sig,
        reviewerSignaturePath = revSig,
        updatedTimestamp = System.currentTimeMillis()
    )
}
