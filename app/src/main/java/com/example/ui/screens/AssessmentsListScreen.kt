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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.local.entity.AssessmentEntity
import com.example.data.model.AssessmentStatus
import com.example.ui.components.AssessmentListRow
import com.example.ui.theme.IndustrialNavy800
import com.example.ui.theme.IndustrialSlate100
import com.example.ui.theme.SafetyOrangePrimary

@Composable
fun AssessmentsListScreen(
    assessments: List<AssessmentEntity>,
    onSelectAssessment: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf<AssessmentStatus?>(null) }

    val filteredList = assessments.filter { item ->
        val matchesQuery = searchQuery.isBlank() ||
                item.documentRef.contains(searchQuery, ignoreCase = true) ||
                item.employeeInitialsSurname.contains(searchQuery, ignoreCase = true) ||
                item.targetAreaOrHeg.contains(searchQuery, ignoreCase = true) ||
                item.type.title.contains(searchQuery, ignoreCase = true)

        val matchesStatus = selectedStatusFilter == null || item.status == selectedStatusFilter
        matchesQuery && matchesStatus
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSlate100)
    ) {
        // Search & Filter header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Reference, Employee or Area") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = IndustrialNavy800
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_assessments_input"),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedStatusFilter == null,
                        onClick = { selectedStatusFilter = null },
                        label = { Text("All (${assessments.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IndustrialNavy800,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                items(AssessmentStatus.values()) { status ->
                    val count = assessments.count { it.status == status }
                    FilterChip(
                        selected = selectedStatusFilter == status,
                        onClick = {
                            selectedStatusFilter = if (selectedStatusFilter == status) null else status
                        },
                        label = { Text("${status.label} ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SafetyOrangePrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No assessments matching criteria",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                items(filteredList) { item ->
                    AssessmentListRow(
                        assessment = item,
                        onClick = { onSelectAssessment(item.id) }
                    )
                }
            }
        }
    }
}
