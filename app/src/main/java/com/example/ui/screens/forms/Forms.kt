package com.example.ui.screens.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialSlate200
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusCriticalBg
import com.example.ui.theme.StatusSuccess

@Composable
fun PersonalNoiseForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    val lex8h = formData["lex8h"]?.toDoubleOrNull()
    val exceedsOel = lex8h != null && lex8h > 85.0

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Exceedance Alert Banner if > 85 dB(A)
        if (exceedsOel) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StatusCriticalBg, RoundedCornerShape(8.dp))
                    .border(1.dp, StatusCritical, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "OEL Exceeded",
                        tint = StatusCritical,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "MANDATORY INVESTIGATION REQUIRED",
                            style = MaterialTheme.typography.titleSmall,
                            color = StatusCritical,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Lex,8h (${lex8h} dB(A)) exceeds South African Mine OEL of 85.0 dB(A). Verify hearing protection attenuation and initiate engineering control inquiry.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section 1: Employee & Exposure Group
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("1. Employee & HEG Identification", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["employeeInitialsSurname"] ?: "",
                        onValueChange = { onFieldChanged("employeeInitialsSurname", it) },
                        label = { Text("Initials & Surname *") },
                        placeholder = { Text("e.g. J. Mashaba") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["employeeCompanyNo"] ?: "",
                        onValueChange = { onFieldChanged("employeeCompanyNo", it) },
                        label = { Text("Company No *") },
                        placeholder = { Text("LC-4109") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["hegNumber"] ?: "HEG 01",
                        onValueChange = { onFieldChanged("hegNumber", it) },
                        label = { Text("HEG Number") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["occupationCode"] ?: "CRUSHER-OP",
                        onValueChange = { onFieldChanged("occupationCode", it) },
                        label = { Text("Occupation Code") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        // Section 2: Dosimetry & Calibration Checks
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("2. Dosimeter Equipment & Calibration (dB)", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["dosimeterNo"] ?: "DB-3419",
                        onValueChange = { onFieldChanged("dosimeterNo", it) },
                        label = { Text("Dosimeter Serial No") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["calSourceNo"] ?: "CAL-99120",
                        onValueChange = { onFieldChanged("calSourceNo", it) },
                        label = { Text("Calibrator Serial No") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["calPreCheck"] ?: "94.0",
                        onValueChange = { onFieldChanged("calPreCheck", it) },
                        label = { Text("Pre-Cal Check (dB)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["calPostCheck"] ?: "94.1",
                        onValueChange = { onFieldChanged("calPostCheck", it) },
                        label = { Text("Post-Cal Check (dB)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Calibration within spec (±0.5 dB)", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = formData["calCompliant"]?.toBoolean() ?: true,
                        onCheckedChange = { onFieldChanged("calCompliant", it.toString()) },
                        colors = SwitchDefaults.colors(checkedThumbColor = SafetyOrangePrimary)
                    )
                }
            }
        }

        // Section 3: Measurements
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("3. Run Times & Noise Exposure", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["startTime"] ?: "07:15",
                        onValueChange = { onFieldChanged("startTime", it) },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["stopTime"] ?: "15:45",
                        onValueChange = { onFieldChanged("stopTime", it) },
                        label = { Text("Stop Time") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["durationMinutes"] ?: "510",
                        onValueChange = { onFieldChanged("durationMinutes", it) },
                        label = { Text("Duration (min)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["lex8h"] ?: "",
                        onValueChange = { onFieldChanged("lex8h", it) },
                        label = { Text("Lex,8h Exposure dB(A) *") },
                        placeholder = { Text("e.g. 87.4") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["peakDbc"] ?: "",
                        onValueChange = { onFieldChanged("peakDbc", it) },
                        label = { Text("Peak Sound dB(C)") },
                        placeholder = { Text("e.g. 136.2") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        // Section 4: Questionnaire & Observations
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("4. Wearer Questionnaire & PPE", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                FormCheckboxRow(
                    label = "Performing normal shift duties throughout sampling?",
                    checked = formData["questNormal"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("questNormal", it.toString()) }
                )
                FormCheckboxRow(
                    label = "Free of current ear pain, infection or hearing trouble?",
                    checked = formData["questEarTrouble"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("questEarTrouble", it.toString()) }
                )
                FormCheckboxRow(
                    label = "Received noise exposure & hearing conservation training?",
                    checked = formData["questTrained"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("questTrained", it.toString()) }
                )

                OutlinedTextField(
                    value = formData["hearingProtection"] ?: "Custom Moulded Plugs SNR 28dB",
                    onValueChange = { onFieldChanged("hearingProtection", it) },
                    label = { Text("Hearing Protection Device Issued & Worn") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = formData["observations"] ?: "",
                    onValueChange = { onFieldChanged("observations", it) },
                    label = { Text("Assessor Shift Observations & Tasks") },
                    placeholder = { Text("e.g. Spent 4.5 hours on screen platform during crushing surge") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalAirborneForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    var expandedPollutant by remember { mutableStateOf(false) }
    val pollutants = listOf(
        "Respirable Crystalline Silica (α-Quartz)",
        "Respirable Dust (Particulates)",
        "Inhalable Dust (Total Particulates)",
        "Ozone (O₃ Gas)",
        "Nitrogen Dioxide (NO₂ Gas)",
        "Carbon Monoxide (CO Gas)"
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("1. Employee & Exposure Group", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["employeeInitialsSurname"] ?: "",
                        onValueChange = { onFieldChanged("employeeInitialsSurname", it) },
                        label = { Text("Employee Name *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["employeeCompanyNo"] ?: "",
                        onValueChange = { onFieldChanged("employeeCompanyNo", it) },
                        label = { Text("Company No") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = formData["hegNumber"] ?: "HEG 02 - Plant Artisans",
                    onValueChange = { onFieldChanged("hegNumber", it) },
                    label = { Text("Homogeneous Exposure Group (HEG)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("2. Pollutant Type & Measurement", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                ExposedDropdownMenuBox(
                    expanded = expandedPollutant,
                    onExpandedChange = { expandedPollutant = !expandedPollutant }
                ) {
                    OutlinedTextField(
                        value = formData["pollutantType"] ?: pollutants.first(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pollutant Category *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPollutant) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPollutant,
                        onDismissRequest = { expandedPollutant = false }
                    ) {
                        pollutants.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    onFieldChanged("pollutantType", item)
                                    expandedPollutant = false
                                }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["measurementResult"] ?: "",
                        onValueChange = { onFieldChanged("measurementResult", it) },
                        label = { Text("Result *") },
                        placeholder = { Text("0.045") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["measurementUnit"] ?: "mg/m³",
                        onValueChange = { onFieldChanged("measurementUnit", it) },
                        label = { Text("Unit (e.g. mg/m³ or ppm) *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["pumpSerial"] ?: "SKC-52-9011",
                        onValueChange = { onFieldChanged("pumpSerial", it) },
                        label = { Text("Pump Serial") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["flowRate"] ?: "2.0 L/min",
                        onValueChange = { onFieldChanged("flowRate", it) },
                        label = { Text("Flow Rate") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = formData["respiratorType"] ?: "Half-mask with P3 Particulate Filter",
                    onValueChange = { onFieldChanged("respiratorType", it) },
                    label = { Text("Respirator / Mask Type Issued") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun ThermalStressForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("1. Thermal Assessment Setup", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Ambient WBGT vs Personal Heat", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = formData["isPersonal"]?.toBoolean() ?: false,
                        onCheckedChange = { onFieldChanged("isPersonal", it.toString()) },
                        colors = SwitchDefaults.colors(checkedThumbColor = SafetyOrangePrimary)
                    )
                }

                OutlinedTextField(
                    value = formData["targetAreaOrHeg"] ?: "Quarry Excavation Zone / HEG 02",
                    onValueChange = { onFieldChanged("targetAreaOrHeg", it) },
                    label = { Text("Work Area / HEG Description *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("2. Temperature & WBGT Index Readings", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["wetBulb"] ?: "22.5",
                        onValueChange = { onFieldChanged("wetBulb", it) },
                        label = { Text("Wet Bulb (°C)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["dryBulb"] ?: "29.2",
                        onValueChange = { onFieldChanged("dryBulb", it) },
                        label = { Text("Dry Bulb (°C)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["globeTemp"] ?: "31.8",
                        onValueChange = { onFieldChanged("globeTemp", it) },
                        label = { Text("Globe Temp (°C)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["wbgtIndex"] ?: "24.5",
                        onValueChange = { onFieldChanged("wbgtIndex", it) },
                        label = { Text("WBGT Index (°C) *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Text(
                    text = "Reference Standard: 27.5°C Screening Threshold / 32.5°C Work Withdrawal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FormCheckboxRow(
                    label = "Cold potable water station verified available and stocked with ice",
                    checked = formData["hydrationChecked"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("hydrationChecked", it.toString()) }
                )
            }
        }
    }
}

@Composable
fun AreaSourceNoiseForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    var isSourceNoise by remember { mutableStateOf(formData["isSourceNoise"]?.toBoolean() ?: false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            if (isSourceNoise) "Source Noise (Machinery)" else "Area Noise (Work Zone)",
                            fontWeight = FontWeight.Bold,
                            color = IndustrialNavy800
                        )
                        Text("Switch to toggle area vs specific machine sound level", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = isSourceNoise,
                        onCheckedChange = {
                            isSourceNoise = it
                            onFieldChanged("isSourceNoise", it.toString())
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = SafetyOrangePrimary)
                    )
                }

                OutlinedTextField(
                    value = formData["locationOrMachine"] ?: if (isSourceNoise) "Jaw Crusher Drive Motor" else "Primary Crusher Feed Floor",
                    onValueChange = { onFieldChanged("locationOrMachine", it) },
                    label = { Text(if (isSourceNoise) "Machine / Source Name *" else "Plant Area / Work Zone *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["soundLevelDba"] ?: "88.5",
                        onValueChange = { onFieldChanged("soundLevelDba", it) },
                        label = { Text("Sound Level dB(A) *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["meterSerial"] ?: "CEL-620-10928",
                        onValueChange = { onFieldChanged("meterSerial", it) },
                        label = { Text("SLM Serial") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                FormCheckboxRow(
                    label = "Demarcated Hearing Protection Zone signage posted?",
                    checked = formData["zoneSignage"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("zoneSignage", it.toString()) }
                )
            }
        }
    }
}

@Composable
fun TmmIlluminationForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    val measuredLux = formData["measuredLux"]?.toDoubleOrNull()
    val isCompliant = measuredLux != null && measuredLux >= 10.0

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Trackless Mobile Machine (TMM) Details", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["machineDesc"] ?: "Bell B30D Articulated Dump Truck",
                        onValueChange = { onFieldChanged("machineDesc", it) },
                        label = { Text("Machine Description *") },
                        modifier = Modifier.weight(1.5f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["fleetNumber"] ?: "FL-14",
                        onValueChange = { onFieldChanged("fleetNumber", it) },
                        label = { Text("Fleet No *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["measuredLux"] ?: "15.2",
                        onValueChange = { onFieldChanged("measuredLux", it) },
                        label = { Text("Headlamp Lux @ 20m *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["headlampCondition"] ?: "Clean & Intact",
                        onValueChange = { onFieldChanged("headlampCondition", it) },
                        label = { Text("Lens Condition") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isCompliant) StatusSuccess.copy(alpha = 0.1f) else StatusCriticalBg, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = if (isCompliant) "✓ Standard met: ≥10 Lux at 20.0 metres in travel direction"
                        else "⚠ Non-compliant: Headlamp illuminance is below 10 Lux at 20.0 metres",
                        fontWeight = FontWeight.Bold,
                        color = if (isCompliant) StatusSuccess else StatusCritical,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PotableWaterAForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Potable Water Bacteriological Sampling (Sheet A)", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                OutlinedTextField(
                    value = formData["sampleId"] ?: "PW-A-2026-001",
                    onValueChange = { onFieldChanged("sampleId", it) },
                    label = { Text("Sample ID *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = formData["sourceDescription"] ?: "Change House Kitchen Drinking Tap",
                    onValueChange = { onFieldChanged("sourceDescription", it) },
                    label = { Text("Water Source / Tap Location *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["freeChlorine"] ?: "0.45",
                        onValueChange = { onFieldChanged("freeChlorine", it) },
                        label = { Text("Free Chlorine (mg/L)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["phLevel"] ?: "7.2",
                        onValueChange = { onFieldChanged("phLevel", it) },
                        label = { Text("pH Level") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = formData["labName"] ?: "EcoChem Laboratories (SANAS Accredited)",
                    onValueChange = { onFieldChanged("labName", it) },
                    label = { Text("Accredited Testing Laboratory") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun PotableWaterBForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Potable Water Additional Assessment (Sheet B)", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                OutlinedTextField(
                    value = formData["samplePoint"] ?: "Plant Header Storage Tank Outflow",
                    onValueChange = { onFieldChanged("samplePoint", it) },
                    label = { Text("Distribution Sampling Point *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["turbidityNtu"] ?: "0.68",
                        onValueChange = { onFieldChanged("turbidityNtu", it) },
                        label = { Text("Turbidity (NTU)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["conductivity"] ?: "24.5",
                        onValueChange = { onFieldChanged("conductivity", it) },
                        label = { Text("Conductivity (mS/m)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = formData["coolerBoxTemp"] ?: "4.0°C (Ice Pack Preserved)",
                    onValueChange = { onFieldChanged("coolerBoxTemp", it) },
                    label = { Text("Chain of Custody Cooler Temp") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun IlluminationForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Artificial Illumination (Day / Night)", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Day vs Night Assessment", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = formData["isNight"]?.toBoolean() ?: false,
                        onCheckedChange = { onFieldChanged("isNight", it.toString()) },
                        colors = SwitchDefaults.colors(checkedThumbColor = SafetyOrangePrimary)
                    )
                }

                OutlinedTextField(
                    value = formData["workingLocation"] ?: "Secondary Screen Deck Walkway",
                    onValueChange = { onFieldChanged("workingLocation", it) },
                    label = { Text("Measurement Location & Plane *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["measuredLux"] ?: "120.0",
                        onValueChange = { onFieldChanged("measuredLux", it) },
                        label = { Text("Measured Lux *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["requiredLux"] ?: "100.0",
                        onValueChange = { onFieldChanged("requiredLux", it) },
                        label = { Text("Benchmark Standard (Lux)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }
    }
}

@Composable
fun FalloutDustForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("ASTM D1739 Fall-Out Dust Gauge", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                OutlinedTextField(
                    value = formData["bucketId"] ?: "FD-BUCKET-04",
                    onValueChange = { onFieldChanged("bucketId", it) },
                    label = { Text("Bucket Sample ID *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = formData["samplingLocation"] ?: "South Boundary Fence (Adjacent to Community)",
                    onValueChange = { onFieldChanged("samplingLocation", it) },
                    label = { Text("Monitoring Station Location *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formData["exposureDays"] ?: "30",
                        onValueChange = { onFieldChanged("exposureDays", it) },
                        label = { Text("Exposure Days") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formData["dustRate"] ?: "480",
                        onValueChange = { onFieldChanged("dustRate", it) },
                        label = { Text("Rate (mg/m²/day)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Text(
                    text = "National Dust Control Regulations: Residential <600 mg/m²/day | Industrial <1200 mg/m²/day",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AblutionForm(
    formData: MutableMap<String, String>,
    onFieldChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ablution Facilities Sanitation Audit", fontWeight = FontWeight.Bold, color = IndustrialNavy800)

                OutlinedTextField(
                    value = formData["facilityName"] ?: "Change House North (Plant & Quarry Staff)",
                    onValueChange = { onFieldChanged("facilityName", it) },
                    label = { Text("Facility Name & Location *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                FormCheckboxRow(
                    label = "Handwashing basins operational with clean running water",
                    checked = formData["basinsWorking"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("basinsWorking", it.toString()) }
                )
                FormCheckboxRow(
                    label = "Antiseptic soap dispensers filled and functional",
                    checked = formData["soapAvailable"]?.toBoolean() ?: false,
                    onCheckedChange = { onFieldChanged("soapAvailable", it.toString()) }
                )
                FormCheckboxRow(
                    label = "Hot water available for showers (≥38°C standard)",
                    checked = formData["hotWaterWorking"]?.toBoolean() ?: false,
                    onCheckedChange = { onFieldChanged("hotWaterWorking", it.toString()) }
                )
                FormCheckboxRow(
                    label = "Toilets, urinals and cubicles clean and sanitized",
                    checked = formData["toiletsClean"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("toiletsClean", it.toString()) }
                )
                FormCheckboxRow(
                    label = "Flooring slip-resistant, clean and adequately drained",
                    checked = formData["floorsClean"]?.toBoolean() ?: true,
                    onCheckedChange = { onFieldChanged("floorsClean", it.toString()) }
                )
            }
        }
    }
}

@Composable
fun FormCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = SafetyOrangePrimary)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
