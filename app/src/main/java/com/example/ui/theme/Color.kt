package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Industrial Safety Red Palette (Primary Theme default - Changed from Orange to Red)
val SafetyRedPrimary = Color(0xFFDC2626) // Vivid Industrial Safety Red
val SafetyRedLight = Color(0xFFEF4444)
val SafetyRedDark = Color(0xFF991B1B)
val SafetyRedContainer = Color(0xFFFEE2E2)
val SafetyRedOnContainer = Color(0xFF7F1D1D)

// Classic High-Vis Safety Orange Palette
val SafetyOrangeClassic = Color(0xFFEA580C)
val SafetyOrangeClassicLight = Color(0xFFFB923C)
val SafetyOrangeClassicContainer = Color(0xFFFFEDD5)
val SafetyOrangeClassicOnContainer = Color(0xFF7C2D12)

// By default, map SafetyOrangePrimary to Red as requested ("Change colour of theme from orange to red")
val SafetyOrangePrimary = SafetyRedPrimary
val SafetyOrangeLight = SafetyRedLight
val SafetyOrangeContainer = SafetyRedContainer
val SafetyOrangeOnContainer = SafetyRedOnContainer

// Corporate Navy & Slate Palette
val CorporateNavy900 = Color(0xFF0B132B)
val CorporateNavy800 = Color(0xFF0F1E36)
val CorporateNavy700 = Color(0xFF1C2C4E)
val CorporateNavy600 = Color(0xFF263D66)
val CorporateBluePrimary = Color(0xFF1D4ED8)
val CorporateBlueDark = Color(0xFF1E3A8A)
val CorporateBlueLight = Color(0xFF60A5FA)

// Eco Environmental Emerald Palette
val EcoEmeraldPrimary = Color(0xFF059669)
val EcoEmeraldLight = Color(0xFF10B981)
val EcoEmeraldDark = Color(0xFF064E3B)
val EcoEmeraldContainer = Color(0xFFD1FAE5)
val EcoEmeraldOnContainer = Color(0xFF064E3B)

// Obsidian Tactical Dark Palette
val ObsidianBlack = Color(0xFF121212)
val ObsidianSurface = Color(0xFF1E1E1E)
val ObsidianAccent = Color(0xFFE11D48)

// Clean Neutral Slates
val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate600 = Color(0xFF475569)
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF94A3B8)
val Slate300 = Color(0xFFCBD5E1)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)

// Status & Evaluation Colors
val StatusSuccess = Color(0xFF16A34A)
val StatusSuccessBg = Color(0xFFDCFCE7)
val StatusWarning = Color(0xFFD97706)
val StatusWarningBg = Color(0xFFFEF3C7)
val StatusCritical = Color(0xFFDC2626)
val StatusCriticalBg = Color(0xFFFEE2E2)
val StatusInfo = Color(0xFF0284C7)
val StatusInfoBg = Color(0xFFE0F2FE)
val StatusPending = Color(0xFF9333EA)
val StatusPendingBg = Color(0xFFF3E8FF)

// Industrial Palette Aliases for backward compatibility
val IndustrialNavy900 = CorporateNavy900
val IndustrialNavy800 = CorporateNavy800
val IndustrialNavy700 = CorporateNavy700
val IndustrialNavy500 = CorporateBlueDark
val IndustrialNavy100 = Slate100

val IndustrialSlate900 = Slate900
val IndustrialSlate800 = Slate800
val IndustrialSlate700 = Slate700
val IndustrialSlate600 = Slate600
val IndustrialSlate400 = Slate400
val IndustrialSlate200 = Slate200
val IndustrialSlate100 = Slate100
val IndustrialSlate50 = Slate50

/**
 * App Themes that the user can choose from ("choose different themes")
 */
enum class AppTheme(
    val id: String,
    val title: String,
    val primaryColor: Color,
    val accentColor: Color,
    val lightColor: Color,
    val containerColor: Color,
    val onContainerColor: Color,
    val description: String
) {
    INDUSTRIAL_RED(
        id = "red",
        title = "Industrial Red",
        primaryColor = SafetyRedPrimary,
        accentColor = SafetyRedDark,
        lightColor = SafetyRedLight,
        containerColor = SafetyRedContainer,
        onContainerColor = SafetyRedOnContainer,
        description = "Industrial safety red for urgent hazard & environmental compliance"
    ),
    SAFETY_ORANGE(
        id = "orange",
        title = "Safety Orange",
        primaryColor = SafetyOrangeClassic,
        accentColor = Color(0xFFC2410C),
        lightColor = SafetyOrangeClassicLight,
        containerColor = SafetyOrangeClassicContainer,
        onContainerColor = SafetyOrangeClassicOnContainer,
        description = "High-visibility occupational hygiene & quarry hazard orange"
    ),
    CORPORATE_NAVY(
        id = "navy",
        title = "Corporate Navy",
        primaryColor = CorporateBluePrimary,
        accentColor = CorporateNavy900,
        lightColor = CorporateBlueLight,
        containerColor = Color(0xFFDBEAFE),
        onContainerColor = Color(0xFF1E3A8A),
        description = "Executive statutory audit slate & corporate engineering blue"
    ),
    ECO_EMERALD(
        id = "emerald",
        title = "Eco Emerald",
        primaryColor = EcoEmeraldPrimary,
        accentColor = EcoEmeraldDark,
        lightColor = EcoEmeraldLight,
        containerColor = EcoEmeraldContainer,
        onContainerColor = EcoEmeraldOnContainer,
        description = "Environmental sampling, potable water & clean ecology green"
    ),
    OBSIDIAN_DARK(
        id = "obsidian",
        title = "Tactical Obsidian",
        primaryColor = ObsidianAccent,
        accentColor = Color(0xFF9F1239),
        lightColor = Color(0xFFFB7185),
        containerColor = Color(0xFF4C0519),
        onContainerColor = Color(0xFFFFE4E6),
        description = "Ultra high-contrast dark field night ops mode"
    )
}
