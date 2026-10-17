package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.SiteInfoEntity
import com.example.data.model.AssessmentType
import com.example.ui.components.SiteHeaderBanner
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialSlate100
import com.example.ui.theme.IndustrialSlate700
import com.example.ui.theme.SafetyOrangePrimary

@Composable
fun NewAssessmentScreen(
    siteInfo: SiteInfoEntity = SiteInfoEntity(),
    onSelectAssessmentType: (AssessmentType) -> Unit,
    modifier: Modifier = Modifier
) {
    val modules = AssessmentType.values()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSlate100),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SiteHeaderBanner(siteInfo = siteInfo)
        }

        item {
            Column {
                Text(
                    text = "Select Assessment Module",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = IndustrialNavy800
                )
                Text(
                    text = "Choose one of the 10 statutory assessment workflows to start recording",
                    style = MaterialTheme.typography.bodySmall,
                    color = IndustrialSlate700
                )
            }
        }

        items(modules) { module ->
            ModuleSelectionCard(
                module = module,
                onClick = { onSelectAssessmentType(module) }
            )
        }
    }
}

@Composable
fun ModuleSelectionCard(
    module: AssessmentType,
    onClick: () -> Unit
) {
    val icon = when (module) {
        AssessmentType.PERSONAL_NOISE -> Icons.Default.VolumeUp
        AssessmentType.PERSONAL_AIRBORNE -> Icons.Default.Air
        AssessmentType.THERMAL_STRESS -> Icons.Default.Thermostat
        AssessmentType.AREA_SOURCE_NOISE -> Icons.Default.Hearing
        AssessmentType.ILLUMINATION_DAY_NIGHT -> Icons.Default.LightMode
        AssessmentType.TMM_ILLUMINATION -> Icons.Default.DirectionsCar
        AssessmentType.FALLOUT_DUST -> Icons.Default.Grain
        AssessmentType.POTABLE_WATER_A -> Icons.Default.WaterDrop
        AssessmentType.POTABLE_WATER_B -> Icons.Default.Science
        AssessmentType.ABLUTION_FACILITIES -> Icons.Default.CleanHands
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("module_item_${module.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(IndustrialNavy800, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = module.title,
                    tint = SafetyOrangePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = SafetyOrangePrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = module.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyOrangePrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = IndustrialNavy800
                )

                Text(
                    text = module.shortDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
