package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AssessmentEntity
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialSlate100
import com.example.ui.theme.IndustrialSlate700
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusSuccess

data class ChatMessage(
    val sender: String, // "USER" or "AI"
    val text: String,
    val isDraftSuggestion: Boolean = false
)

@Composable
fun AiAssistantScreen(
    assessments: List<AssessmentEntity>,
    modifier: Modifier = Modifier
) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                sender = "AI",
                text = "Welcome to SiteHygiene AI Assistant. I can audit recorded site data against South African Mine Health and Safety Act criteria, identify incomplete fields, check noise dosimeter calibration drift, or draft technical report summaries from field observations."
            )
        )
    }

    var userInput by remember { mutableStateOf("") }

    val quickQuestions = listOf(
        "Audit today's assessments for incomplete fields",
        "Check noise readings against 85 dB(A) OEL",
        "Draft Part 1 Executive Summary for Letaba Crushers",
        "Explain TMM Headlamp 10 Lux @ 20m standard"
    )

    fun handleSend(query: String) {
        if (query.isBlank()) return
        messages.add(ChatMessage(sender = "USER", text = query))

        val lower = query.lowercase()
        val response = when {
            lower.contains("incomplete") || lower.contains("audit") || lower.contains("missing") -> {
                val unsigned = assessments.filter { it.signaturePath.isBlank() }
                val inProgress = assessments.filter { it.status == AssessmentStatus.IN_PROGRESS }
                val awaitingLab = assessments.filter { it.status == AssessmentStatus.AWAITING_RESULTS }

                buildString {
                    append("Audit Results for Letaba Crushers (DME 14466):\n")
                    append("• Assessments In Progress: ${inProgress.size} (${inProgress.joinToString { it.documentRef }})\n")
                    append("• Missing Signatures: ${unsigned.size} (${if (unsigned.isEmpty()) "All signed" else unsigned.joinToString { it.documentRef }})\n")
                    append("• Awaiting Accredited Lab Results: ${awaitingLab.size} (${awaitingLab.joinToString { it.documentRef }})\n\n")
                    append("Recommendation: Complete signatures for pending records before generating final Part 2 reports.")
                }
            }
            lower.contains("noise") || lower.contains("85") || lower.contains("oel") -> {
                val noiseAssessments = assessments.filter { it.type == AssessmentType.PERSONAL_NOISE }
                val nonCompliant = noiseAssessments.filter { !it.complianceFlag }

                buildString {
                    append("Occupational Noise Exposure Analysis:\n")
                    append("Statutory standard: 85.0 dB(A) Lex,8h (MHSA Act 29 of 1996).\n")
                    if (nonCompliant.isNotEmpty()) {
                        nonCompliant.forEach {
                            append("⚠ Exceedance detected: ${it.documentRef} (${it.employeeInitialsSurname}, ${it.targetAreaOrHeg}). ${it.findingsSummary}\n")
                        }
                        append("\nMandatory Action: Form 12(1) investigation required. Acoustic damping recommended for hopper chute.")
                    } else {
                        append("✓ All evaluated noise exposure records comply with the 85 dB(A) threshold.")
                    }
                }
            }
            lower.contains("tmm") || lower.contains("headlamp") || lower.contains("lux") -> {
                "Trackless Mobile Machine (TMM) Illumination Standard:\n" +
                "Reference Standard: Minimum 10 Lux at 20.0 metres directly forward in the direction of travel.\n" +
                "Applies to: Haul trucks, front-end loaders, drill rigs and utility fleet.\n" +
                "Verified at Letaba Crushers: Bell B30D (FL-14) measured 15.2 Lux at 20m (COMPLIANT)."
            }
            lower.contains("draft") || lower.contains("summary") || lower.contains("executive") -> {
                "DRAFT EXECUTIVE SUMMARY — LETABA CRUSHERS (PTY) LTD.\n" +
                "Date: 2026-10-15 | DME Mine Code: 14466\n\n" +
                "WorkForce Health SA conducted an occupational hygiene and environmental assessment across the primary crushing and quarry operations. " +
                "Baseline environmental conditions were recorded with WBGT 23.2°C at start of shift. Personal noise dosimetry identified an 87.4 dB(A) Lex,8h exposure on the primary crusher operator, requiring engineering chute dampening. " +
                "TMM mobile fleet headlamp illumination complied with the 10 Lux @ 20m standard. Bacteriological potable water samples were cold-preserved and dispatched to EcoChem Laboratories for micro-biological culture.\n\n" +
                "[Note: This is an AI-generated draft requiring review and signature by Dr. M. Dlamini, Appointed Hygienist]."
            }
            else -> {
                "Recorded data overview for Letaba Crushers: ${assessments.size} assessments in database. " +
                "You can ask me to evaluate noise OEL compliance, review incomplete records, or draft report text."
            }
        }

        messages.add(ChatMessage(sender = "AI", text = response, isDraftSuggestion = lower.contains("draft")))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSlate100)
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SafetyOrangePrimary, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Occupational Hygiene Field AI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IndustrialNavy800
                )
                Text(
                    text = "Strict adherence to recorded observations & MHSA limits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Quick query chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(quickQuestions) { question ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = IndustrialNavy800.copy(alpha = 0.08f),
                    modifier = Modifier.testTag("quick_question_${question.take(10)}")
                ) {
                    Text(
                        text = question,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = IndustrialNavy800,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .padding(top = 1.dp)
                    )
                }
            }
        }

        // Chat message list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "USER"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) IndustrialNavy800 else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (isUser) "You" else "Field Hygiene AI",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isUser) SafetyOrangePrimary else IndustrialNavy800
                                )
                                if (msg.isDraftSuggestion) {
                                    Surface(
                                        color = SafetyOrangePrimary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "DRAFT ONLY",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SafetyOrangePrimary,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Message input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Ask about OEL limits, missing fields, drafting...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_query_input"),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    handleSend(userInput)
                    userInput = ""
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(SafetyOrangePrimary, RoundedCornerShape(24.dp))
                    .testTag("send_ai_query_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}
