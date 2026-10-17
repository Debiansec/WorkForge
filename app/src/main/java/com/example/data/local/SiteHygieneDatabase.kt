package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.AssessmentDao
import com.example.data.local.dao.ClientAccountDao
import com.example.data.local.dao.CompanyDao
import com.example.data.local.dao.DocumentTemplateDao
import com.example.data.local.dao.EquipmentDao
import com.example.data.local.dao.OccupationalAssessmentDao
import com.example.data.local.dao.RemedialActionDao
import com.example.data.local.dao.SiteDao
import com.example.data.local.dao.SiteInfoDao
import com.example.data.local.dao.SiteVisitDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.Assessment
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.ClientAccount
import com.example.data.local.entity.Company
import com.example.data.local.entity.DocumentTemplate
import com.example.data.local.entity.EquipmentEntity
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.local.entity.Site
import com.example.data.local.entity.SiteInfoEntity
import com.example.data.local.entity.SiteVisitEntity
import com.example.data.local.entity.User
import com.example.data.model.ActionPriority
import com.example.data.model.ActionStatus
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.HazardType
import com.example.data.model.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        SiteInfoEntity::class,
        SiteVisitEntity::class,
        AssessmentEntity::class,
        RemedialActionEntity::class,
        EquipmentEntity::class,
        User::class,
        Company::class,
        Site::class,
        ClientAccount::class,
        Assessment::class,
        DocumentTemplate::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SiteHygieneDatabase : RoomDatabase() {

    abstract fun siteInfoDao(): SiteInfoDao
    abstract fun siteVisitDao(): SiteVisitDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun remedialActionDao(): RemedialActionDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun siteDao(): SiteDao
    abstract fun clientAccountDao(): ClientAccountDao
    abstract fun occupationalAssessmentDao(): OccupationalAssessmentDao
    abstract fun documentTemplateDao(): DocumentTemplateDao

    companion object {
        @Volatile
        private var INSTANCE: SiteHygieneDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SiteHygieneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SiteHygieneDatabase::class.java,
                    "workforce_health_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration(true)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialDemoData(database)
                }
            }
        }

        private suspend fun populateInitialDemoData(db: SiteHygieneDatabase) {
            val siteInfoDao = db.siteInfoDao()
            val siteVisitDao = db.siteVisitDao()
            val equipDao = db.equipmentDao()
            val assessDao = db.assessmentDao()
            val actionDao = db.remedialActionDao()

            // 1. Initial Site Info for Letaba Crushers (DME 14466)
            siteInfoDao.insertOrUpdate(
                SiteInfoEntity(
                    clientName = "Letaba Crushers (Pty) Ltd.",
                    mineName = "Letaba Quarry & Crushing Plant",
                    location = "Farm Tarentaalrant 24LT, Tzaneen, Limpopo Province, South Africa",
                    dmeCode = "14466",
                    mineManager = "J. Van Der Merwe",
                    managerContact = "+27 15 307 8900 / admin@letabacrushers.co.za",
                    siteHygienist = "Dr. M. Dlamini (MOH Reg #OH0412)",
                    medicalPractitioner = "Dr. K. Botha (MBChB, DOH)",
                    assessorName = "J. Naidoo",
                    assessorCert = "OH Assistant Cert #7721",
                    previousVisitDate = "2026-09-14",
                    previousRemedialActions = "Primary crusher hopper rubber damping skirt fitted.",
                    specialInstructions = "Ensure personal noise dosimeters include full pre/post shift acoustic calibration checks. Flag any Lex,8h exceeding 85 dB(A)."
                )
            )

            // 2. Initial Site Visits History
            val siteVisits = listOf(
                SiteVisitEntity(
                    visitDate = "2026-10-14",
                    clientName = "Letaba Crushers (Pty) Ltd.",
                    mineCode = "14466",
                    mineLocation = "Farm Tarentaalrant 24LT, Tzaneen",
                    leadAssessor = "J. Naidoo (OH Assistant)",
                    reviewingHygienist = "Dr. M. Dlamini",
                    visitStatus = "IN_PROGRESS",
                    completedModulesCount = 3,
                    pendingModulesCount = 2,
                    openActionCount = 2,
                    summaryNotes = "Statutory shift occupational hygiene baseline: personal noise dosimeter survey, TMM headlamp verification, and bacteriological potable water sampling."
                ),
                SiteVisitEntity(
                    visitDate = "2026-09-14",
                    clientName = "Letaba Crushers (Pty) Ltd.",
                    mineCode = "14466",
                    mineLocation = "Farm Tarentaalrant 24LT, Tzaneen",
                    leadAssessor = "J. Naidoo (OH Assistant)",
                    reviewingHygienist = "Dr. M. Dlamini",
                    visitStatus = "COMPLETED",
                    completedModulesCount = 8,
                    pendingModulesCount = 0,
                    openActionCount = 0,
                    summaryNotes = "Quarterly comprehensive environmental and occupational health monitoring. All parameters reviewed and approved in Part 2 report."
                ),
                SiteVisitEntity(
                    visitDate = "2026-08-12",
                    clientName = "Letaba Crushers (Pty) Ltd.",
                    mineCode = "14466",
                    mineLocation = "Farm Tarentaalrant 24LT, Tzaneen",
                    leadAssessor = "J. Naidoo (OH Assistant)",
                    reviewingHygienist = "Dr. M. Dlamini",
                    visitStatus = "COMPLETED",
                    completedModulesCount = 7,
                    pendingModulesCount = 0,
                    openActionCount = 0,
                    summaryNotes = "Routine monthly dust fallout gauge collection and plant area noise mapping."
                )
            )
            siteVisitDao.insertAll(siteVisits)

            // 3. Equipment Register
            val equipmentList = listOf(
                EquipmentEntity(
                    equipmentName = "Sound Level Meter (Class 1)",
                    model = "Casella CEL-620",
                    serialNumber = "CEL-620-10928",
                    parameterMeasured = "Area & Source Sound Pressure dB(A)/dB(C)",
                    lastCalibrationDate = "2026-01-15",
                    nextCalibrationDate = "2027-01-15",
                    calibrationCertNo = "SANAS-AC-2026-441",
                    status = "VALID",
                    isDemo = true
                ),
                EquipmentEntity(
                    equipmentName = "Personal Noise Dosimeter (Unit 1)",
                    model = "Cirrus doseBadge Mk4",
                    serialNumber = "DB-3419",
                    parameterMeasured = "Lex,8h, Lpeak, Sound Dose %",
                    lastCalibrationDate = "2026-03-10",
                    nextCalibrationDate = "2027-03-10",
                    calibrationCertNo = "SANAS-AC-2026-890",
                    status = "VALID",
                    isDemo = true
                ),
                EquipmentEntity(
                    equipmentName = "Acoustic Calibrator",
                    model = "Cirrus CR:514 (94.0 dB @ 1kHz)",
                    serialNumber = "CAL-99120",
                    parameterMeasured = "Acoustic Reference 94.0 dB(A)",
                    lastCalibrationDate = "2026-03-05",
                    nextCalibrationDate = "2027-03-05",
                    calibrationCertNo = "SANAS-CAL-2026-119",
                    status = "VALID",
                    isDemo = true
                ),
                EquipmentEntity(
                    equipmentName = "Thermal Stress Heat Logger",
                    model = "AZ 87786 WBGT Logger",
                    serialNumber = "AZ-87786-4401",
                    parameterMeasured = "Wet Bulb, Dry Bulb, Globe Temp, WBGT",
                    lastCalibrationDate = "2025-11-20",
                    nextCalibrationDate = "2026-11-20",
                    calibrationCertNo = "SANAS-TH-2025-782",
                    status = "VALID",
                    isDemo = true
                ),
                EquipmentEntity(
                    equipmentName = "Weather & Microclimate Tracker",
                    model = "Kestrel 4000 Pocket Tracker",
                    serialNumber = "K4-7712",
                    parameterMeasured = "Wind Speed m/s, RH %, Barometric Press",
                    lastCalibrationDate = "2026-02-14",
                    nextCalibrationDate = "2027-02-14",
                    calibrationCertNo = "SANAS-ENV-2026-301",
                    status = "VALID",
                    isDemo = true
                ),
                EquipmentEntity(
                    equipmentName = "Digital Lux Meter",
                    model = "Testo 540",
                    serialNumber = "TST-540-8472",
                    parameterMeasured = "Illuminance in Lux (0 - 99,999 Lux)",
                    lastCalibrationDate = "2025-12-05",
                    nextCalibrationDate = "2026-12-05",
                    calibrationCertNo = "SANAS-OPT-2025-612",
                    status = "VALID",
                    isDemo = true
                ),
                EquipmentEntity(
                    equipmentName = "Personal Air Sampling Pump",
                    model = "SKC AirChek 52",
                    serialNumber = "SKC-52-9011",
                    parameterMeasured = "Air Flow Rate 1.0 - 3.0 L/min",
                    lastCalibrationDate = "2026-02-28",
                    nextCalibrationDate = "2027-02-28",
                    calibrationCertNo = "SANAS-FLOW-2026-552",
                    status = "VALID",
                    isDemo = true
                )
            )
            equipDao.insertAll(equipmentList)

            // 4. Demonstration Assessments
            val demoAssessments = listOf(
                AssessmentEntity(
                    documentRef = "LC-PN-2026-001",
                    type = AssessmentType.PERSONAL_NOISE,
                    status = AssessmentStatus.AWAITING_REVIEW,
                    visitDate = "2026-10-14",
                    targetAreaOrHeg = "Primary Crusher Platform / HEG 01",
                    employeeInitialsSurname = "J. Mashaba",
                    employeeCompanyNo = "LC-4109",
                    complianceFlag = false, // Exceeded 85 dB(A)
                    findingsSummary = "[DEMO] Shift dose Lex,8h measured at 87.4 dB(A), exceeding the 85.0 dB(A) occupational exposure limit. Investigation conducted; employee issued custom-moulded hearing protection with SNR 28dB.",
                    specificDataJson = """{
                        "hegNumber": "HEG 01",
                        "occupationCode": "CRUSH-OP",
                        "dosimeterNo": "DB-3419",
                        "calSourceNo": "CAL-99120",
                        "calPreCheck": 94.0,
                        "calPostCheck": 94.1,
                        "calCompliant": true,
                        "startTime": "07:15",
                        "stopTime": "15:45",
                        "durationMinutes": 510,
                        "lex8h": 87.4,
                        "peakDbc": 136.2,
                        "investigationConducted": true,
                        "hearingProtection": "Custom Moulded Plugs SNR 28dB",
                        "questionnaireNormalDuties": true,
                        "questionnaireEarIssues": false,
                        "questionnaireTrained": true,
                        "remedialAction": "Fit noise dampers to hopper feed chute; schedule follow-up audiometric screening."
                    }""",
                    shiftEnvironmentJson = """{
                        "measurementPeriod": "Start of Shift",
                        "timeRecorded": "07:30",
                        "wetBulbTemp": 21.2,
                        "dryBulbTemp": 26.5,
                        "globeTemp": 28.1,
                        "wbgtIndex": 23.0,
                        "relativeHumidity": 62.0,
                        "windSpeed": 1.5,
                        "generalConditions": "Clear skies, mild temperature"
                    }""",
                    signaturePath = "CONFIRMED_J_MASHABA",
                    isDemo = true
                ),
                AssessmentEntity(
                    documentRef = "LC-TMM-2026-002",
                    type = AssessmentType.TMM_ILLUMINATION,
                    status = AssessmentStatus.COMPLETED,
                    visitDate = "2026-10-14",
                    targetAreaOrHeg = "Quarry Haul Route / Fleet Yard",
                    employeeInitialsSurname = "P. Maluleke",
                    employeeCompanyNo = "LC-3180",
                    complianceFlag = true,
                    findingsSummary = "[DEMO] Bell B30D Articulated Dump Truck (Fleet #FL-14) headlamp illuminance verified at 15.2 Lux at 20.0m forward position. Exceeds the 10 Lux @ 20m mandatory safety standard.",
                    specificDataJson = """{
                        "machineDesc": "Bell B30D Articulated Dump Truck",
                        "fleetNumber": "FL-14",
                        "measurementDistance": "20.0 m",
                        "measuredLux": 15.2,
                        "standardThreshold": "10 Lux @ 20m",
                        "headlampCondition": "Clean, lenses clear, alignment correct",
                        "reverseAlarmsWorking": true,
                        "operatorComments": "Headlamp optics serviced during 500hr maintenance."
                    }""",
                    signaturePath = "CONFIRMED_INSPECTOR",
                    isDemo = true
                ),
                AssessmentEntity(
                    documentRef = "LC-WA-2026-003",
                    type = AssessmentType.POTABLE_WATER_A,
                    status = AssessmentStatus.AWAITING_RESULTS,
                    visitDate = "2026-10-14",
                    targetAreaOrHeg = "Change House Kitchen Drinking Tap",
                    employeeInitialsSurname = "N/A - Area Sample",
                    employeeCompanyNo = "SAMP-PW-01",
                    complianceFlag = true,
                    findingsSummary = "[DEMO] Potable water bacteriological sample PW-A-01 drawn and placed in cooler box at 4.2°C. Dispatched to EcoChem Laboratory for Coliforms & E. coli culture.",
                    specificDataJson = """{
                        "sampleId": "PW-A-2026-001",
                        "sourceDescription": "Change House Kitchen Main Supply Tap",
                        "samplingTime": "08:15",
                        "freeChlorine": 0.42,
                        "phLevel": 7.35,
                        "tempAtSample": 18.6,
                        "coolerBoxTemp": 4.2,
                        "labName": "EcoChem Accredited Laboratory (SANAS 041)",
                        "coliformResult": "PENDING_LAB",
                        "eColiResult": "PENDING_LAB"
                    }""",
                    isDemo = true
                ),
                AssessmentEntity(
                    documentRef = "LC-AB-2026-004",
                    type = AssessmentType.ABLUTION_FACILITIES,
                    status = AssessmentStatus.IN_PROGRESS,
                    visitDate = "2026-10-14",
                    targetAreaOrHeg = "Workshop & Plant Change House North",
                    employeeInitialsSurname = "Supervisor: B. Dube",
                    employeeCompanyNo = "LC-1022",
                    complianceFlag = false,
                    findingsSummary = "[DEMO] Non-conformance noted: Shower block #3 hot water supply temperature below standard (28°C) due to geyser element fault. Soap dispensers require refilling.",
                    specificDataJson = """{
                        "facilityName": "Change House North",
                        "toiletsClean": true,
                        "handBasinsWorking": true,
                        "soapAvailable": false,
                        "handDryingAvailable": true,
                        "showersHotWater": false,
                        "showerTempRecorded": "28.4°C (Min required 38°C)",
                        "ventilationAdequate": true,
                        "floorsCleanSlipResistant": true,
                        "actionRequired": "Replace geyser heating element & replenish antiseptic liquid soap dispensers."
                    }""",
                    isDemo = true
                ),
                AssessmentEntity(
                    documentRef = "LC-TS-2026-005",
                    type = AssessmentType.THERMAL_STRESS,
                    status = AssessmentStatus.IN_PROGRESS,
                    visitDate = "2026-10-14",
                    targetAreaOrHeg = "Secondary Screen Deck Walkway",
                    employeeInitialsSurname = "K. Sithole",
                    employeeCompanyNo = "LC-5021",
                    complianceFlag = true,
                    findingsSummary = "[DEMO] Ambient WBGT monitored at 24.8°C at mid-shift. Below the 27.5°C screening threshold. Cold potable drinking water stations verified available and iced.",
                    specificDataJson = """{
                        "hegNumber": "HEG 02",
                        "period": "Middle of Shift (12:00)",
                        "wetBulb": 22.8,
                        "dryBulb": 29.5,
                        "globeTemp": 32.1,
                        "wbgtIndex": 24.8,
                        "rhPercent": 56.0,
                        "workRestRegime": "Continuous Work (Light-Moderate)",
                        "hydrationChecked": true
                    }""",
                    isDemo = true
                )
            )

            for (assessment in demoAssessments) {
                val insertedId = assessDao.insertAssessment(assessment)
                if (!assessment.complianceFlag) {
                    if (assessment.type == AssessmentType.PERSONAL_NOISE) {
                        actionDao.insertAction(
                            RemedialActionEntity(
                                assessmentId = insertedId,
                                assessmentRef = assessment.documentRef,
                                assessmentTitle = "Personal Noise Overexposure Investigation",
                                findingDescription = "Operator J. Mashaba Lex,8h exposure reached 87.4 dB(A), exceeding 85 dB(A) OEL at Primary Crusher.",
                                correctiveAction = "Fit acoustic damping curtains to hopper chute and issue custom silicone earplugs.",
                                assignedPerson = "Engineering Foreman (R. Marais)",
                                priority = ActionPriority.HIGH,
                                dueDate = "2026-10-22",
                                status = ActionStatus.IN_PROGRESS,
                                followUpNotes = "Damping materials ordered from supplier. Fitting scheduled for weekend maintenance shutdown.",
                                isDemo = true
                            )
                        )
                    } else if (assessment.type == AssessmentType.ABLUTION_FACILITIES) {
                        actionDao.insertAction(
                            RemedialActionEntity(
                                assessmentId = insertedId,
                                assessmentRef = assessment.documentRef,
                                assessmentTitle = "Change House North Hot Water Restoration",
                                findingDescription = "Geyser heating element failure in Change House North resulting in cold shower water.",
                                correctiveAction = "Replace 4kW geyser heating element and thermostat; restock wall soap dispensers.",
                                assignedPerson = "Plant Electrician (T. Ndlovu)",
                                priority = ActionPriority.MEDIUM,
                                dueDate = "2026-10-17",
                                status = ActionStatus.OPEN,
                                followUpNotes = "Work order #WO-9912 generated in maintenance log.",
                                isDemo = true
                            )
                        )
                    }
                }
            }

            // 5. Initial Companies Setup
            val companyDao = db.companyDao()
            val companyId1 = companyDao.insertCompany(
                Company(
                    name = "WorkForce Health SA (Pty) Ltd.",
                    shortName = "WorkForce Health",
                    registrationNumber = "2015/098412/07",
                    vatNumber = "4901847120",
                    industryType = "Occupational Health & Hygiene Consultancy",
                    email = "info@workforcehealth.co.za",
                    phone = "+27 11 555 0199",
                    address = "14 Corporate Boulevard, Sandton, Johannesburg, South Africa",
                    dmeCode = "DMR-HQ-001",
                    mineManager = "Dr. M. Dlamini",
                    managerContact = "+27 82 555 1024",
                    isClient = false,
                    primaryHegsJson = "[\"Air Quality\", \"Acoustics\", \"Thermal Stress\", \"Ergonomics\"]"
                )
            )

            val companyId2 = companyDao.insertCompany(
                Company(
                    name = "Letaba Crushers (Pty) Ltd.",
                    shortName = "Letaba Crushers",
                    registrationNumber = "2008/019283/07",
                    vatNumber = "4910283746",
                    industryType = "Dolerite Quarry & Crushing Plant",
                    email = "admin@letabacrushers.co.za",
                    phone = "+27 15 307 8900",
                    address = "Farm Tarentaalrant 24LT, Tzaneen, Limpopo",
                    dmeCode = "14466",
                    mineManager = "J. Van Der Merwe",
                    managerContact = "+27 15 307 8900 / admin@letabacrushers.co.za",
                    isClient = true,
                    primaryHegsJson = "[\"Primary Jaw Crusher (HEG 01)\", \"Secondary Cone & Screens (HEG 02)\", \"TMM Haul Truck Operators (HEG 03)\", \"Maintenance Workshop (HEG 05)\"]"
                )
            )

            // 6. Initial Users
            val userDao = db.userDao()
            userDao.insertUsers(listOf(
                User(
                    username = "m.dlamini",
                    email = "hygienist@workforcehealth.co.za",
                    fullName = "Dr. M. Dlamini",
                    role = UserRole.OH_HYGIENIST,
                    phone = "+27 82 410 8821",
                    certificationNumber = "MOH Reg #OH0412",
                    designation = "Lead Reviewing Hygienist",
                    companyId = companyId1
                ),
                User(
                    username = "j.naidoo",
                    email = "assessor@workforcehealth.co.za",
                    fullName = "J. Naidoo",
                    role = UserRole.OH_ASSISTANT,
                    phone = "+27 83 992 4102",
                    certificationNumber = "OH Assistant Cert #7721",
                    designation = "Field Occupational Hygiene Assessor",
                    companyId = companyId1
                ),
                User(
                    username = "j.vandermerwe",
                    email = "manager@letaba.co.za",
                    fullName = "J. Van Der Merwe",
                    role = UserRole.MINE_MANAGEMENT,
                    phone = "+27 15 307 8900",
                    certificationNumber = "MHSA Cert #2201",
                    designation = "Mine Operations & Safety Manager",
                    companyId = companyId2
                ),
                User(
                    username = "admin",
                    email = "admin@workforcehealth.co.za",
                    fullName = "Operations Admin",
                    role = UserRole.ADMIN,
                    phone = "+27 11 555 0100",
                    certificationNumber = "SYS-ADM-01",
                    designation = "System Administrator",
                    companyId = companyId1
                )
            ))

            // 7. Initial Sites
            val siteDao = db.siteDao()
            val siteId1 = siteDao.insertSite(
                Site(
                    companyId = companyId2,
                    siteName = "Letaba Quarry & Crushing Plant",
                    dmeCode = "14466",
                    location = "Farm Tarentaalrant 24LT, Tzaneen, Limpopo Province",
                    operationType = "Dolerite Quarry, Crushing & Screening",
                    mineManager = "J. Van Der Merwe",
                    managerContact = "+27 15 307 8900",
                    siteHygienist = "Dr. M. Dlamini"
                )
            )

            val siteId2 = siteDao.insertSite(
                Site(
                    companyId = companyId2,
                    siteName = "Phalaborwa Mining Open Pit & Smelter",
                    dmeCode = "08219",
                    location = "Copper Road, Phalaborwa, Limpopo",
                    operationType = "Open Pit Copper & Magnetite",
                    mineManager = "T. Baloyi",
                    managerContact = "+27 15 780 2911",
                    siteHygienist = "Dr. M. Dlamini"
                )
            )

            // 8. Initial Client Accounts
            val clientDao = db.clientAccountDao()
            clientDao.insertClients(listOf(
                ClientAccount(
                    clientName = "Letaba Crushers (Pty) Ltd.",
                    accountNumber = "CLI-LET-14466",
                    industrySector = "Aggregate & Mining Quarrying",
                    primaryContact = "J. Van Der Merwe",
                    contactEmail = "admin@letabacrushers.co.za",
                    contactPhone = "+27 15 307 8900",
                    physicalAddress = "Farm Tarentaalrant 24LT, Tzaneen, Limpopo",
                    activeSitesCount = 2,
                    contractStatus = "ACTIVE"
                ),
                ClientAccount(
                    clientName = "Phalaborwa Mining Company (PMC)",
                    accountNumber = "CLI-PMC-08219",
                    industrySector = "Copper Beneficiation & Smelting",
                    primaryContact = "T. Baloyi",
                    contactEmail = "sheq@pmc.co.za",
                    contactPhone = "+27 15 780 2911",
                    physicalAddress = "Copper Road, Phalaborwa, Limpopo",
                    activeSitesCount = 3,
                    contractStatus = "ACTIVE"
                ),
                ClientAccount(
                    clientName = "Mopani Aggregates & ReadyMix",
                    accountNumber = "CLI-MOP-22105",
                    industrySector = "Granite Quarry & Concrete Batching",
                    primaryContact = "S. Nkuna",
                    contactEmail = "ops@mopaniaggregates.co.za",
                    contactPhone = "+27 15 812 4000",
                    physicalAddress = "Section D Industrial Area, Giyani, Limpopo",
                    activeSitesCount = 1,
                    contractStatus = "ACTIVE"
                ),
                ClientAccount(
                    clientName = "Limpopo Industrial Sands (Pty) Ltd",
                    accountNumber = "CLI-LIS-19842",
                    industrySector = "Silica Washing, Drying & Bagging",
                    primaryContact = "H. Grobler",
                    contactEmail = "safety@limpoposands.co.za",
                    contactPhone = "+27 15 293 1100",
                    physicalAddress = "Silicon Road, Polokwane Industrial, Limpopo",
                    activeSitesCount = 1,
                    contractStatus = "ACTIVE"
                )
            ))

            // 9. Initial Occupational Hygiene 'Assessment' entities linked to Site ID
            val occDao = db.occupationalAssessmentDao()
            occDao.insertAssessments(listOf(
                Assessment(
                    siteId = siteId1,
                    timestamp = System.currentTimeMillis() - 86400000L * 2,
                    hazardType = HazardType.NOISE,
                    exposureMeasurement = 87.4,
                    measurementUnit = "dB(A)",
                    occupationalExposureLimit = 85.0,
                    isExceedance = true,
                    assessmentRef = "LC-PN-2026-001",
                    assessedAreaOrHeg = "Primary Crusher Platform / HEG 01",
                    employeeName = "J. Mashaba",
                    employeeNumber = "LC-4109",
                    status = AssessmentStatus.AWAITING_REVIEW,
                    findingsSummary = "Personal noise dose Lex,8h measured at 87.4 dB(A), exceeding the 85 dB(A) statutory OEL.",
                    assessorName = "J. Naidoo",
                    instrumentation = "Cirrus doseBadge Mk4 (DB-3419)"
                ),
                Assessment(
                    siteId = siteId1,
                    timestamp = System.currentTimeMillis() - 86400000L * 3,
                    hazardType = HazardType.ILLUMINATION,
                    exposureMeasurement = 15.2,
                    measurementUnit = "Lux",
                    occupationalExposureLimit = 10.0,
                    isExceedance = false,
                    assessmentRef = "LC-TMM-2026-002",
                    assessedAreaOrHeg = "Quarry Haul Route / Fleet Yard",
                    employeeName = "P. Maluleke",
                    employeeNumber = "LC-3180",
                    status = AssessmentStatus.COMPLETED,
                    findingsSummary = "Bell B30D Articulated Dump Truck headlamp illuminance verified compliant at 15.2 Lux @ 20m.",
                    assessorName = "J. Naidoo",
                    instrumentation = "Testo 540 Digital Lux Meter"
                ),
                Assessment(
                    siteId = siteId1,
                    timestamp = System.currentTimeMillis() - 86400000L * 1,
                    hazardType = HazardType.THERMAL_HEAT,
                    exposureMeasurement = 24.8,
                    measurementUnit = "°C WBGT",
                    occupationalExposureLimit = 27.5,
                    isExceedance = false,
                    assessmentRef = "LC-TS-2026-005",
                    assessedAreaOrHeg = "Secondary Screen Deck Walkway",
                    employeeName = "K. Sithole",
                    employeeNumber = "LC-5021",
                    status = AssessmentStatus.IN_PROGRESS,
                    findingsSummary = "Ambient WBGT monitored at 24.8°C at mid-shift; compliant with heat screening thresholds.",
                    assessorName = "J. Naidoo",
                    instrumentation = "AZ 87786 WBGT Logger"
                ),
                Assessment(
                    siteId = siteId1,
                    timestamp = System.currentTimeMillis() - 86400000L * 4,
                    hazardType = HazardType.RESPIRABLE_DUST,
                    exposureMeasurement = 0.14,
                    measurementUnit = "mg/m³",
                    occupationalExposureLimit = 0.10,
                    isExceedance = true,
                    assessmentRef = "LC-PA-2026-006",
                    assessedAreaOrHeg = "Screening Plant Hopper Feed",
                    employeeName = "D. Chauke",
                    employeeNumber = "LC-2291",
                    status = AssessmentStatus.AWAITING_REVIEW,
                    findingsSummary = "Respirable crystalline silica elevated at 0.14 mg/m³; mist suppression nozzle clogged.",
                    assessorName = "J. Naidoo",
                    instrumentation = "SKC AirChek 52 Personal Pump"
                )
            ))

            // 10. Initial Document Templates
            val templateDao = db.documentTemplateDao()
            templateDao.insertTemplates(listOf(
                DocumentTemplate(
                    templateCode = "TMP-NOISE-01",
                    title = "Personal Noise Exposure Dosimetry (SANS 10083)",
                    category = "Acoustics & Vibration",
                    standardCode = "SANS 10083 / MHSA Reg 9.2(1)",
                    statutoryLimit = "85.0 dB(A) Lex,8h / 140 dB(C) Peak",
                    instrumentRequirement = "Type 1 / 2 Acoustic Dosimeter & Calibrator (SANAS Validated)",
                    isMandatory = true,
                    description = "Comprehensive shift dosimetry monitoring sheet including pre/post acoustic calibration, employee activity logs, and personal hearing protection factor verification."
                ),
                DocumentTemplate(
                    templateCode = "TMP-DUST-02",
                    title = "Airborne Particulates & Respirable Crystalline Silica",
                    category = "Air Quality & Inhalables",
                    standardCode = "NIOSH 0600 / DMR Guideline 16/3/2/4-A1",
                    statutoryLimit = "Respirable Dust: 3.0 mg/m³ / Silica: 0.1 mg/m³",
                    instrumentRequirement = "Cyclone Pre-separator & Personal Sampling Pump (Flow rate 1.7 L/min)",
                    isMandatory = true,
                    description = "Personal air sampling protocol for respirable dust and quartz silica sampling across designated Homogeneous Exposure Groups (HEGs)."
                ),
                DocumentTemplate(
                    templateCode = "TMP-HEAT-03",
                    title = "Thermal Stress & Environmental Heat Load (WBGT)",
                    category = "Thermal & Climate",
                    standardCode = "ISO 7243 / SANS 7243 / MHSA Chapter 22",
                    statutoryLimit = "Screening threshold: 27.5°C WBGT / Action limit: 30.0°C WBGT",
                    instrumentRequirement = "Calibrated Wet Bulb Globe Thermometer (Natural Wet Bulb, Dry Bulb, 150mm Globe)",
                    isMandatory = true,
                    description = "Shift thermal screening and environmental assessment protocol calculating metabolic rate, work-rest cycles, and mandatory hydration station verification."
                ),
                DocumentTemplate(
                    templateCode = "TMP-LIGHT-04",
                    title = "TMM Mobile Machine & Plant Illumination Verification",
                    category = "Illumination & Visual",
                    standardCode = "MHSA Reg 8.10(1) / SANS 10114-1",
                    statutoryLimit = "Trackless Mobile Machines: Minimum 10 Lux @ 20m forward plane",
                    instrumentRequirement = "Cosine-corrected Digital Illuminance Meter (Lux)",
                    isMandatory = true,
                    description = "Statutory haul truck, excavator, and front-end loader headlamp optical check verifying forward working visibility and reverse alarm safety."
                ),
                DocumentTemplate(
                    templateCode = "TMP-WATER-05",
                    title = "Potable Drinking Water Quality & Chemical Assessment",
                    category = "Water Quality & Potable Supply",
                    standardCode = "SANS 241-1:2015 / WHO Guidelines",
                    statutoryLimit = "E. coli: 0 counts/100ml; Free Chlorine: 0.2 - 0.5 mg/L; Turbidity < 1 NTU",
                    instrumentRequirement = "Sterile Sampling Flasks, DPD Chlorine Colorimeter, Digital pH Meter",
                    isMandatory = true,
                    description = "Statutory drinking water bacteriological chain-of-custody log for mine tap, reservoir, and water bowser verification."
                ),
                DocumentTemplate(
                    templateCode = "TMP-ABLUTION-06",
                    title = "Change House & Ablution Sanitation Hygiene Audit",
                    category = "Sanitation & Facilities",
                    standardCode = "MHSA Reg 4.1 / OHS Act Facilities Reg 2004",
                    statutoryLimit = "Water Temp: 38 - 42°C; 1 Shower per 15 Employees; Antiseptic Hand Wash",
                    instrumentRequirement = "Immersion Digital Thermometer, Inspection Checklist Form",
                    isMandatory = false,
                    description = "Comprehensive workplace sanitation compliance assessment auditing shower water temperatures, ventilation, soap replenishment, and slip prevention."
                )
            ))
        }

    }
}
