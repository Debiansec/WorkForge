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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.StatusSuccess

@Composable
fun SamplesLabScreen(
    assessments: List<AssessmentEntity>,
    onSelectAssessment: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter sample-related assessments (Water A, Water B, Fall-Out Dust, Airborne Pollutants)
    val sampleAssessments = assessments.filter {
        it.type == AssessmentType.POTABLE_WATER_A ||
        it.type == AssessmentType.POTABLE_WATER_B ||
        it.type == AssessmentType.FALLOUT_DUST ||
        it.type == AssessmentType.PERSONAL_AIRBORNE
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSlate100),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = IndustrialNavy800),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(SafetyOrangePrimary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = "Lab",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Samples & Accredited Laboratory Log",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "EcoChem SANAS Accredited Lab #041 Dispatch",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tracks bacteriological cultures, cooler chain preservation (≤6°C) and gravimetric dust analysis received from external laboratories.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        item {
            Text(
                text = "Tracked Field Samples (${sampleAssessments.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IndustrialNavy800
            )
        }

        items(sampleAssessments) { item ->
            SampleTrackingCard(
                assessment = item,
                onClick = { onSelectAssessment(item.id) }
            )
        }
    }
}

@Composable
fun SampleTrackingCard(
    assessment: AssessmentEntity,
    onClick: () -> Unit
) {
    val isPending = assessment.status == AssessmentStatus.AWAITING_RESULTS

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sample_item_${assessment.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = assessment.documentRef,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SafetyOrangePrimary
                    )
                    Text(
                        text = assessment.type.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = if (isPending) Color(0xFFF3E8FF) else Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (isPending) Icons.Default.HourglassEmpty else Icons.Default.Verified,
                            contentDescription = "Status",
                            tint = if (isPending) Color(0xFF7E22CE) else StatusSuccess,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPending) "Awaiting Lab" else "Results Verified",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPending) Color(0xFF7E22CE) else StatusSuccess
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📍 Sampling Point: ${assessment.targetAreaOrHeg}",
                style = MaterialTheme.typography.bodySmall,
                color = IndustrialSlate700
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Preservation: Ice chest chain maintained at 4.2°C • Courier dispatch verified",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = IndustrialNavy800),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(if (isPending) "View Sample & Record Lab Certificate" else "View Certificate Details")
            }
        }
    }
}
