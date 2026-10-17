package com.example.data.model

enum class UserRole(val label: String, val badge: String) {
    OH_ASSISTANT("Occupational Hygiene Assistant", "Field Assessor"),
    OH_HYGIENIST("Occupational Hygienist", "Reviewer & Approver"),
    MINE_MANAGEMENT("Mine Management", "Viewer & Actions"),
    ADMIN("System Administrator", "Admin")
}

enum class AssessmentStatus(val label: String) {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    AWAITING_REVIEW("Awaiting Review"),
    AWAITING_RESULTS("Awaiting Lab Results"),
    COMPLETED("Completed Approved")
}

enum class AssessmentType(
    val id: String,
    val title: String,
    val category: String,
    val iconName: String,
    val shortDesc: String
) {
    PERSONAL_NOISE(
        "personal_noise",
        "Personal Noise Exposure",
        "Acoustics",
        "VolumeUp",
        "Dosimetry, Lex,8h, calibration checks & employee questionnaire"
    ),
    PERSONAL_AIRBORNE(
        "personal_airborne",
        "Personal Airborne Pollutants",
        "Occupational Health",
        "Air",
        "Respirable dust, silica, O3 & gaseous pollutants sampling"
    ),
    THERMAL_STRESS(
        "thermal_stress",
        "Thermal Stress Assessment",
        "Environmental",
        "Thermostat",
        "Personal artificial-heat & ambient WBGT index monitoring"
    ),
    AREA_SOURCE_NOISE(
        "area_source_noise",
        "Area & Source Noise",
        "Acoustics",
        "Hearing",
        "Plant zoning, machine source sound level monitoring in dB(A)"
    ),
    ILLUMINATION_DAY_NIGHT(
        "illumination_day_night",
        "Artificial Illumination (Day/Night)",
        "Lighting",
        "LightMode",
        "Working plane, walkway & task illuminance in Lux"
    ),
    TMM_ILLUMINATION(
        "tmm_illumination",
        "Trackless Mobile Machine (TMM) Illumination",
        "Lighting",
        "DirectionsCar",
        "Fleet headlamp verification: 10 Lux @ 20m standard"
    ),
    FALLOUT_DUST(
        "fallout_dust",
        "Fall-Out Dust Monitoring",
        "Environmental",
        "Grain",
        "ASTM D1739 collection buckets & boundary dust deposition"
    ),
    POTABLE_WATER_A(
        "potable_water_a",
        "Potable Water — Bacteriological (Sheet A)",
        "Water Quality",
        "WaterDrop",
        "Bacteriological sampling, free chlorine, pH & E. coli verification"
    ),
    POTABLE_WATER_B(
        "potable_water_b",
        "Potable Water — Additional Assessment (Sheet B)",
        "Water Quality",
        "Science",
        "Distribution reservoirs, turbidity NTU & transport chain verification"
    ),
    ABLUTION_FACILITIES(
        "ablution_facilities",
        "Ablution Facilities Inspection",
        "Sanitation",
        "CleanHands",
        "Showers, change house sanitation, hot water & hygiene audit"
    )
}

enum class ActionPriority {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class ActionStatus {
    OPEN, IN_PROGRESS, CLOSED
}

enum class HazardType(
    val label: String,
    val standardUnit: String,
    val statutoryLimit: Double,
    val iconName: String
) {
    NOISE("Personal & Area Noise", "dB(A)", 85.0, "VolumeUp"),
    RESPIRABLE_DUST("Respirable Dust & Silica", "mg/m³", 0.10, "Air"),
    THERMAL_HEAT("Thermal Stress (WBGT)", "°C", 27.5, "Thermostat"),
    WHOLE_BODY_VIBRATION("Vibration (A(8))", "m/s²", 0.50, "Vibration"),
    HAZARDOUS_CHEMICAL("Chemical Vapor / Gas", "ppm", 50.0, "Science"),
    ILLUMINATION("Task Illuminance", "Lux", 150.0, "LightMode"),
    ERGONOMICS("Ergonomic Strain Score", "RULA", 5.0, "Accessibility"),
    WATER_QUALITY("Potable Water Quality", "E.coli/100ml", 0.0, "WaterDrop"),
    SANITATION("Ablution Sanitation Score", "%", 85.0, "CleanHands")
}


data class SiteInfo(
    val clientName: String = "Letaba Crushers (Pty) Ltd.",
    val mineName: String = "Letaba Quarry & Crushing Plant",
    val location: String = "Farm Tarentaalrant 24LT, Tzaneen, Limpopo Province, South Africa",
    val dmeCode: String = "14466",
    val mineManager: String = "J. Van Der Merwe",
    val managerContact: String = "+27 15 307 8900 / admin@letabacrushers.co.za",
    val siteHygienist: String = "Dr. M. Dlamini (MOH Reg #OH0412)",
    val medicalPractitioner: String = "Dr. K. Botha (MBChB, DOH)",
    val assessorName: String = "J. Naidoo",
    val assessorCert: String = "OH Assistant Cert #7721",
    val previousVisitDate: String = "2026-09-14",
    val specialInstructions: String = "Ensure personal noise dosimeters include full pre/post shift acoustic calibration checks. Flag any Lex,8h exceeding 85 dB(A)."
)

data class ShiftEnvironment(
    val measurementPeriod: String = "Start of Shift", // Start, Middle, End
    val timeRecorded: String = "07:30",
    val wetBulbTemp: Double? = 21.4, // °C
    val dryBulbTemp: Double? = 26.8, // °C
    val globeTemp: Double? = 28.5, // °C
    val wbgtIndex: Double? = 23.2, // °C
    val relativeHumidity: Double? = 64.0, // %
    val windSpeed: Double? = 1.8, // m/s
    val generalConditions: String = "Partly cloudy, light breeze from South East",
    val instrumentModel: String = "AZ 87786 WBGT Logger & Kestrel 4000",
    val instrumentSerial: String = "WBGT-98421 / K4-7712",
    val comments: String = "Within normal seasonal ranges"
)

data class CompanySite(
    val id: String,
    val name: String,
    val shortName: String,
    val dmeCode: String,
    val location: String,
    val operationType: String,
    val mineManager: String,
    val managerContact: String,
    val primaryHegs: List<String> = listOf("Crushing Plant", "Screening Decks", "TMM Haulage", "Workshops")
)

val DEFAULT_COMPANIES = listOf(
    CompanySite(
        id = "letaba",
        name = "Letaba Crushers (Pty) Ltd.",
        shortName = "Letaba Crushers",
        dmeCode = "14466",
        location = "Farm Tarentaalrant 24LT, Tzaneen, Limpopo",
        operationType = "Dolerite Quarry, Crushing & Screening",
        mineManager = "J. Van Der Merwe",
        managerContact = "+27 15 307 8900 / admin@letabacrushers.co.za",
        primaryHegs = listOf("Primary Jaw Crusher (HEG 01)", "Secondary Cone & Screens (HEG 02)", "TMM Haul Truck Operators (HEG 03)", "Front End Loader Loading Area (HEG 04)", "Maintenance Workshop (HEG 05)")
    ),
    CompanySite(
        id = "pmc",
        name = "Phalaborwa Mining Company (PMC)",
        shortName = "Phalaborwa Mining",
        dmeCode = "08219",
        location = "Copper Road, Phalaborwa, Limpopo",
        operationType = "Open Pit Copper & Magnetite Beneficiation",
        mineManager = "T. Baloyi",
        managerContact = "+27 15 780 2911 / sheq@pmc.co.za",
        primaryHegs = listOf("Smelter Plant (HEG 01)", "Acid Plant (HEG 02)", "Underground Haulage (HEG 03)", "Concentrator Flotation (HEG 04)")
    ),
    CompanySite(
        id = "mopani",
        name = "Mopani Aggregates & ReadyMix",
        shortName = "Mopani Aggregates",
        dmeCode = "22105",
        location = "Section D Industrial Area, Giyani, Limpopo",
        operationType = "Granite Quarry & Aggregate Processing",
        mineManager = "S. Nkuna",
        managerContact = "+27 15 812 4000 / ops@mopaniaggregates.co.za",
        primaryHegs = listOf("Drill & Blast Bench (HEG 01)", "Tertiary Crusher (HEG 02)", "Batching Plant (HEG 03)")
    ),
    CompanySite(
        id = "limpopo_sand",
        name = "Limpopo Industrial Sands (Pty) Ltd",
        shortName = "Limpopo Sands",
        dmeCode = "19842",
        location = "Silicon Road, Polokwane Industrial, Limpopo",
        operationType = "Silica Quarry, Washing & Drying Plant",
        mineManager = "H. Grobler",
        managerContact = "+27 15 293 1100 / safety@limpoposands.co.za",
        primaryHegs = listOf("Silica Sand Dryer (HEG 01)", "Classification Cyclones (HEG 02)", "Bagging & Palletising (HEG 03)")
    )
)

data class AuthUser(
    val email: String,
    val name: String,
    val role: UserRole,
    val certNumber: String = "OH-SA-88412",
    val designation: String = "Lead Field Assessor"
)

val DEMO_USERS = listOf(
    AuthUser(
        email = "hygienist@workforcehealth.co.za",
        name = "Dr. M. Dlamini",
        role = UserRole.OH_HYGIENIST,
        certNumber = "MOH Reg #OH0412",
        designation = "Occupational Hygienist (Lead Reviewer)"
    ),
    AuthUser(
        email = "assessor@workforcehealth.co.za",
        name = "J. Naidoo",
        role = UserRole.OH_ASSISTANT,
        certNumber = "OH Assistant Cert #7721",
        designation = "Field Occupational Hygiene Assessor"
    ),
    AuthUser(
        email = "manager@letaba.co.za",
        name = "J. Van Der Merwe",
        role = UserRole.MINE_MANAGEMENT,
        certNumber = "MHSA Cert #2201",
        designation = "Mine Operations & Safety Manager"
    ),
    AuthUser(
        email = "admin@workforcehealth.co.za",
        name = "Operations Admin",
        role = UserRole.ADMIN,
        certNumber = "SYS-ADM-01",
        designation = "System & Equipment Administrator"
    )
)

