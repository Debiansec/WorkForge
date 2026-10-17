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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.model.ActionPriority
import com.example.data.model.ActionStatus
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialSlate100
import com.example.ui.theme.IndustrialSlate700
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusSuccess
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemedialActionsScreen(
    actions: List<RemedialActionEntity>,
    onAddAction: (RemedialActionEntity) -> Unit,
    onUpdateAction: (RemedialActionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showActionDialog by remember { mutableStateOf(false) }

    // Form inputs for new action plan
    var findingText by remember { mutableStateOf("") }
    var correctiveText by remember { mutableStateOf("") }
    var assignedUser by remember { mutableStateOf("James - Technician") }
    var priority by remember { mutableStateOf(ActionPriority.HIGH) }
    var dueDate by remember { mutableStateOf("2026-10-25") }
    var isActionRequired by remember { mutableStateOf(true) }

    val userOptions = listOf(
        "James - Technician",
        "R. Marais - Engineering Foreman",
        "T. Ndlovu - Plant Electrician",
        "S. Botha - Safety Officer",
        "J. Van Der Merwe - Mine Manager"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Button(
                onClick = { showActionDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = SafetyOrangePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("create_action_plan_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(6.dp))
                Text("New Action Plan", fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(IndustrialSlate100)
        ) {
            // Header stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Non-Conformance Action Plans",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = IndustrialNavy800
                    )
                    Text(
                        text = "Statutory corrective measures & closure tracking",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = SafetyOrangePrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${actions.count { it.status != ActionStatus.CLOSED }} Active",
                        style = MaterialTheme.typography.labelSmall,
                        color = SafetyOrangePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    ActionPlanCard(
                        action = action,
                        onToggleStatus = {
                            val newStatus = if (action.status == ActionStatus.CLOSED) ActionStatus.OPEN else ActionStatus.CLOSED
                            val closureDate = if (newStatus == ActionStatus.CLOSED) "2026-10-15" else null
                            onUpdateAction(action.copy(status = newStatus, closureDate = closureDate))
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    if (newStatus == ActionStatus.CLOSED) "Action plan marked CLOSED" else "Action plan REOPENED"
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet for "Action Plan" (matching user screenshot 2)
    if (showActionDialog) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showActionDialog = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Action Plan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndustrialNavy800
                    )
                    IconButton(onClick = { showActionDialog = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Action Required toggle row (matching screenshot)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isActionRequired,
                        onCheckedChange = { isActionRequired = it },
                        colors = CheckboxDefaults.colors(checkedColor = StatusSuccess)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Action Required",
                        fontWeight = FontWeight.Bold,
                        color = IndustrialNavy800
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        color = StatusCritical.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "OPEN",
                            color = StatusCritical,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = findingText,
                    onValueChange = { findingText = it },
                    label = { Text("Follow-up Notes / Non-conformance Finding *") },
                    placeholder = { Text("e.g. Left side curtain keeps getting stuck, James please review.") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                OutlinedTextField(
                    value = correctiveText,
                    onValueChange = { correctiveText = it },
                    label = { Text("Corrective Action Prescribed") },
                    placeholder = { Text("e.g. Service roller guide assembly & replace bent runner brackets") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // User Dropdown
                var userExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = userExpanded,
                    onExpandedChange = { userExpanded = !userExpanded }
                ) {
                    OutlinedTextField(
                        value = assignedUser,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("User (Assigned Responsibility) *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = userExpanded,
                        onDismissRequest = { userExpanded = false }
                    ) {
                        userOptions.forEach { user ->
                            DropdownMenuItem(
                                text = { Text(user) },
                                onClick = {
                                    assignedUser = user
                                    userExpanded = false
                                }
                            )
                        }
                    }
                }

                // Priority Dropdown
                var priorityExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = priorityExpanded,
                    onExpandedChange = { priorityExpanded = !priorityExpanded }
                ) {
                    OutlinedTextField(
                        value = priority.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = priorityExpanded,
                        onDismissRequest = { priorityExpanded = false }
                    ) {
                        ActionPriority.values().forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    priority = p
                                    priorityExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { showActionDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CANCEL")
                    }

                    Button(
                        onClick = {
                            if (findingText.isNotBlank()) {
                                onAddAction(
                                    RemedialActionEntity(
                                        assessmentRef = "LC-REM-2026",
                                        assessmentTitle = "Field Finding Action Plan",
                                        findingDescription = findingText,
                                        correctiveAction = correctiveText.ifBlank { "Inspect and remediate per standard" },
                                        assignedPerson = assignedUser,
                                        priority = priority,
                                        dueDate = dueDate,
                                        status = ActionStatus.OPEN
                                    )
                                )
                                showActionDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyOrangePrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("submit_action_plan_button")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Submit")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SUBMIT", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ActionPlanCard(
    action: RemedialActionEntity,
    onToggleStatus: () -> Unit
) {
    val isClosed = action.status == ActionStatus.CLOSED

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("action_item_${action.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isClosed) Color(0xFFF8FAFC) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isClosed) 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isClosed,
                        onCheckedChange = { onToggleStatus() },
                        colors = CheckboxDefaults.colors(checkedColor = StatusSuccess)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isClosed) "CLOSED" else "ACTION OPEN",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isClosed) StatusSuccess else StatusCritical
                    )
                }

                PriorityBadge(priority = action.priority)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = action.findingDescription,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isClosed) Color.Gray else IndustrialNavy800
            )

            if (action.correctiveAction.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fix: ${action.correctiveAction}",
                    style = MaterialTheme.typography.bodySmall,
                    color = IndustrialSlate700
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = SafetyOrangePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = action.assignedPerson,
                        style = MaterialTheme.typography.labelSmall,
                        color = IndustrialNavy800
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Due",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Due: ${action.dueDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
