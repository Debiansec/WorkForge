package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.testTag
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.SiteInfoEntity
import com.example.data.model.ActionPriority
import com.example.data.model.AssessmentStatus
import com.example.data.model.ShiftEnvironment
import com.example.data.model.SiteInfo
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialNavy900
import com.example.ui.theme.IndustrialSlate200
import com.example.ui.theme.IndustrialSlate700
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusCriticalBg
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusInfoBg
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg

@Composable
fun StatusBadge(status: AssessmentStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        AssessmentStatus.NOT_STARTED -> Color(0xFFF1F5F9) to IndustrialSlate700
        AssessmentStatus.IN_PROGRESS -> StatusInfoBg to StatusInfo
        AssessmentStatus.AWAITING_REVIEW -> StatusWarningBg to StatusWarning
        AssessmentStatus.AWAITING_RESULTS -> Color(0xFFF3E8FF) to Color(0xFF7E22CE)
        AssessmentStatus.COMPLETED -> StatusSuccessBg to StatusSuccess
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PriorityBadge(priority: ActionPriority, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (priority) {
        ActionPriority.CRITICAL -> StatusCriticalBg to StatusCritical
        ActionPriority.HIGH -> Color(0xFFFFECE0) to SafetyOrangePrimary
        ActionPriority.MEDIUM -> StatusWarningBg to StatusWarning
        ActionPriority.LOW -> StatusSuccessBg to StatusSuccess
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = priority.name,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun ComplianceIndicator(isCompliant: Boolean, modifier: Modifier = Modifier) {
    if (isCompliant) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(StatusSuccessBg, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Compliant",
                tint = StatusSuccess,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "COMPLIANT",
                style = MaterialTheme.typography.labelSmall,
                color = StatusSuccess,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(StatusCriticalBg, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Exceedance",
                tint = StatusCritical,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "ACTION REQUIRED",
                style = MaterialTheme.typography.labelSmall,
                color = StatusCritical,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SiteHeaderBanner(siteInfo: SiteInfoEntity, modifier: Modifier = Modifier) {
    SiteHeaderBanner(
        siteInfo = SiteInfo(
            clientName = siteInfo.clientName,
            mineName = siteInfo.mineName,
            location = siteInfo.location,
            dmeCode = siteInfo.dmeCode
        ),
        modifier = modifier
    )
}

@Composable
fun SiteHeaderBanner(siteInfo: SiteInfo = SiteInfo(), modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = IndustrialNavy900
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SafetyOrangePrimary, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Mine Site",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = siteInfo.clientName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "DME Mine Code: ${siteInfo.dmeCode} • ${siteInfo.mineName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Surface(
                    color = SafetyOrangePrimary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "ACTIVE SITE",
                        style = MaterialTheme.typography.labelSmall,
                        color = SafetyOrangePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📍 ${siteInfo.location}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShiftEnvironmentCard(
    env: ShiftEnvironment = ShiftEnvironment(),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = "Shift Environment",
                        tint = SafetyOrangePrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Shift Environmental Baseline",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${env.measurementPeriod} (${env.timeRecorded})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                EnvMetricChip(label = "Wet Bulb", value = "${env.wetBulbTemp ?: "--"}°C")
                EnvMetricChip(label = "Dry Bulb", value = "${env.dryBulbTemp ?: "--"}°C")
                EnvMetricChip(label = "Globe Temp", value = "${env.globeTemp ?: "--"}°C")
                EnvMetricChip(label = "WBGT Index", value = "${env.wbgtIndex ?: "--"}°C", highlight = true)
                EnvMetricChip(label = "Rel. Humidity", value = "${env.relativeHumidity ?: "--"}%")
                EnvMetricChip(label = "Wind Speed", value = "${env.windSpeed ?: "--"} m/s")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Instrument: ${env.instrumentModel} (${env.instrumentSerial})",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EnvMetricChip(label: String, value: String, highlight: Boolean = false) {
    Box(
        modifier = Modifier
            .background(
                if (highlight) SafetyOrangePrimary.copy(alpha = 0.12f)
                else IndustrialNavy800.copy(alpha = 0.05f),
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                if (highlight) SafetyOrangePrimary.copy(alpha = 0.5f) else IndustrialSlate200,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = if (highlight) SafetyOrangePrimary else Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (highlight) SafetyOrangePrimary else IndustrialNavy800
            )
        }
    }
}

@Composable
fun AssessmentListRow(
    assessment: AssessmentEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("assessment_card_${assessment.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = assessment.documentRef,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = SafetyOrangePrimary
                        )
                        if (assessment.isDemo) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = IndustrialNavy800.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "DEMO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndustrialNavy800,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = assessment.type.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                StatusBadge(status = assessment.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📍 ${assessment.targetAreaOrHeg}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (assessment.employeeInitialsSurname.isNotBlank()) {
                Text(
                    text = "👤 Employee: ${assessment.employeeInitialsSurname} (${assessment.employeeCompanyNo})",
                    style = MaterialTheme.typography.bodySmall,
                    color = IndustrialSlate700
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ComplianceIndicator(isCompliant = assessment.complianceFlag)
                Text(
                    text = "Visit: ${assessment.visitDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

