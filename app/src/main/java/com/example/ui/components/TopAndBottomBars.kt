package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.model.UserRole
import com.example.ui.theme.CorporateNavy900
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.StatusCritical
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Animation
import com.example.data.model.AuthUser
import com.example.data.model.CompanySite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkforceHealthTopBar(
    currentRoute: String,
    currentRole: UserRole,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onRoleChange: (UserRole) -> Unit,
    onNavigate: (String) -> Unit,
    selectedCompany: CompanySite? = null,
    currentUser: AuthUser? = null,
    onSelectCompanyClick: (() -> Unit)? = null,
    onLogout: (() -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }
    var showRoleMenu by remember { mutableStateOf(false) }

    val screenTitle = when {
        currentRoute.startsWith("dashboard") -> "Workforce Health"
        currentRoute.startsWith("new_assessment") -> "Start Assessment"
        currentRoute.startsWith("assessments_list") -> "All Assessments"
        currentRoute.startsWith("assessment_form") -> "Assessment Record"
        currentRoute.startsWith("remedial_actions") -> "Action Plans"
        currentRoute.startsWith("samples_lab") -> "Samples & Lab Log"
        currentRoute.startsWith("equipment") -> "Equipment Register"
        currentRoute.startsWith("reports") -> "Reports & Evaluation"
        currentRoute.startsWith("ai_assistant") -> "Field AI Assistant"
        currentRoute.startsWith("settings") -> "Settings & Mine Info"
        else -> "Workforce Health"
    }

    TopAppBar(
        navigationIcon = {
            Surface(
                modifier = Modifier
                    .padding(start = 12.dp, end = 4.dp)
                    .size(36.dp)
                    .clickable { onSelectCompanyClick?.invoke() },
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Workforce Health Logo",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        title = {
            Column(
                modifier = if (onSelectCompanyClick != null) {
                    Modifier.clickable { onSelectCompanyClick() }
                } else Modifier
            ) {
                Text(
                    text = screenTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = SafetyOrangePrimary,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${selectedCompany?.shortName ?: "Letaba Crushers"} (DME: ${selectedCompany?.dmeCode ?: "14466"})",
                        style = MaterialTheme.typography.labelSmall,
                        color = SafetyOrangePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        actions = {
            // Dark Mode Quick Toggle
            IconButton(
                onClick = onToggleDarkMode,
                modifier = Modifier.testTag("dark_mode_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Dark Mode",
                    tint = Color.White
                )
            }

            // Role Badge clicker to change active role
            Surface(
                color = SafetyOrangePrimary.copy(alpha = 0.25f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .clickable { showRoleMenu = true }
                    .padding(end = 4.dp)
                    .testTag("role_badge_selector")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Role",
                        tint = SafetyOrangePrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentRole.badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            DropdownMenu(
                expanded = showRoleMenu,
                onDismissRequest = { showRoleMenu = false }
            ) {
                UserRole.values().forEach { role ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(role.label, fontWeight = FontWeight.Bold)
                                Text(role.badge, fontSize = 11.sp, color = Color.Gray)
                            }
                        },
                        onClick = {
                            onRoleChange(role)
                            showRoleMenu = false
                        }
                    )
                }
            }

            // Site Analytics Shortcut
            IconButton(
                onClick = { onNavigate("site_analytics") },
                modifier = Modifier.testTag("top_bar_analytics_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PieChart,
                    contentDescription = "Site Analytics",
                    tint = if (currentRoute == "site_analytics") SafetyOrangePrimary else Color.White
                )
            }

            // Admin & Backend Shortcut
            IconButton(
                onClick = { onNavigate("admin_backend") },
                modifier = Modifier.testTag("top_bar_admin_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin & Backend",
                    tint = if (currentRoute == "admin_backend") SafetyOrangePrimary else Color.White
                )
            }

            // Overflow Menu
            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier.testTag("overflow_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Admin Functions & Backend") },
                    leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SafetyOrangePrimary) },
                    onClick = {
                        onNavigate("admin_backend")
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Site Analytics & Visuals (D3)") },
                    leadingIcon = { Icon(Icons.Default.PieChart, contentDescription = null, tint = SafetyOrangePrimary) },
                    onClick = {
                        onNavigate("site_analytics")
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Certified Equipment Register") },
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                    onClick = {
                        onNavigate("equipment")
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Samples & Lab Chain") },
                    leadingIcon = { Icon(Icons.Default.Science, contentDescription = null) },
                    onClick = {
                        onNavigate("samples_lab")
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings & Mine Info") },
                    leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    onClick = {
                        onNavigate("settings")
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Switch Assessment Company") },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = SafetyOrangePrimary) },
                    onClick = {
                        showMenu = false
                        onSelectCompanyClick?.invoke()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Replay Splash Screen") },
                    leadingIcon = { Icon(Icons.Default.Animation, contentDescription = null) },
                    onClick = {
                        showMenu = false
                        onNavigate("splash")
                    }
                )
                if (onLogout != null) {
                    DropdownMenuItem(
                        text = { Text("Sign Out (${currentUser?.name ?: "User"})") },
                        leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red) },
                        onClick = {
                            showMenu = false
                            onLogout.invoke()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = CorporateNavy900)
    )
}

@Composable
fun WorkforceHealthBottomBar(
    currentRoute: String,
    openActionsCount: Int,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = CorporateNavy900,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = { onNavigate("dashboard") },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SafetyOrangePrimary,
                selectedTextColor = SafetyOrangePrimary,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.testTag("nav_dashboard")
        )

        NavigationBarItem(
            selected = currentRoute == "new_assessment",
            onClick = { onNavigate("new_assessment") },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "New") },
            label = { Text("New Form", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SafetyOrangePrimary,
                selectedTextColor = SafetyOrangePrimary,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.testTag("nav_new_assessment")
        )

        NavigationBarItem(
            selected = currentRoute == "assessments_list",
            onClick = { onNavigate("assessments_list") },
            icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = "Assessments") },
            label = { Text("Records", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SafetyOrangePrimary,
                selectedTextColor = SafetyOrangePrimary,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.testTag("nav_assessments")
        )

        NavigationBarItem(
            selected = currentRoute == "remedial_actions",
            onClick = { onNavigate("remedial_actions") },
            icon = {
                if (openActionsCount > 0) {
                    BadgedBox(badge = {
                        Badge(containerColor = StatusCritical) {
                            Text(openActionsCount.toString(), color = Color.White)
                        }
                    }) {
                        Icon(Icons.Default.Warning, contentDescription = "Actions")
                    }
                } else {
                    Icon(Icons.Default.Warning, contentDescription = "Actions")
                }
            },
            label = { Text("Actions", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SafetyOrangePrimary,
                selectedTextColor = SafetyOrangePrimary,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.testTag("nav_actions")
        )

        NavigationBarItem(
            selected = currentRoute == "ai_assistant",
            onClick = { onNavigate("ai_assistant") },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI") },
            label = { Text("AI Assist", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SafetyOrangePrimary,
                selectedTextColor = SafetyOrangePrimary,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.testTag("nav_ai")
        )
    }
}
