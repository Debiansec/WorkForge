package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ActionPriority
import com.example.data.model.ActionStatus
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.HazardType
import com.example.data.model.UserRole

@Entity(tableName = "site_info")
data class SiteInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    val previousRemedialActions: String = "Replace defective acoustic rubber skirts on primary crusher feed hopper. Completed 2026-09-28.",
    val specialInstructions: String = "Ensure personal noise dosimeters include full pre/post shift acoustic calibration checks. Flag any Lex,8h exceeding 85 dB(A)."
)

@Entity(tableName = "site_visits")
data class SiteVisitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val visitDate: String,
    val clientName: String = "Letaba Crushers (Pty) Ltd.",
    val mineCode: String = "14466",
    val mineLocation: String = "Tzaneen, Limpopo",
    val leadAssessor: String = "J. Naidoo",
    val reviewingHygienist: String = "Dr. M. Dlamini",
    val visitStatus: String = "COMPLETED", // IN_PROGRESS, COMPLETED, SCHEDULED
    val completedModulesCount: Int = 5,
    val pendingModulesCount: Int = 2,
    val openActionCount: Int = 1,
    val summaryNotes: String = "Monthly occupational hygiene statutory audit & environmental monitoring."
)

@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val documentRef: String,
    val type: AssessmentType,
    val status: AssessmentStatus,
    val visitDate: String,
    val clientName: String = "Letaba Crushers (Pty) Ltd.",
    val mineCode: String = "14466",
    val assessor: String = "J. Naidoo",
    val reviewer: String = "Dr. M. Dlamini",
    val targetAreaOrHeg: String, // e.g. "Primary Crusher / HEG 01"
    val employeeInitialsSurname: String = "",
    val employeeCompanyNo: String = "",
    val specificDataJson: String = "{}",
    val shiftEnvironmentJson: String = "{}",
    val findingsSummary: String = "",
    val complianceFlag: Boolean = true, // true = compliant, false = exceedance/finding
    val signaturePath: String = "", // vector representation or file URI
    val reviewerSignaturePath: String = "",
    val photoUri: String? = null,
    val isDemo: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Room entity for 'RemedialAction' to manage corrective actions,
 * including description, status, due date, responsible person, and parent assessment ID.
 */
@Entity(tableName = "remedial_actions")
data class RemedialAction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val assessmentId: Long? = null,
    val assessmentRef: String = "",
    val assessmentTitle: String = "",
    val findingDescription: String = "",
    val correctiveAction: String = "",
    val assignedPerson: String = "",
    val priority: ActionPriority = ActionPriority.HIGH,
    val dueDate: String = "",
    val status: ActionStatus = ActionStatus.OPEN,
    val photoUri: String? = null,
    val followUpNotes: String = "",
    val closureDate: String? = null,
    val isDemo: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
) {
    val parentAssessmentId: Long? get() = assessmentId
    val description: String get() = findingDescription
    val responsiblePerson: String get() = assignedPerson
}

typealias RemedialActionEntity = RemedialAction

/**
 * Room entity for 'Equipment' to store industrial tools (e.g. WBGT loggers, sound level meters)
 * with fields for model, serial number, and calibration details.
 */
@Entity(tableName = "equipment_register")
data class Equipment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val equipmentName: String = "",
    val model: String = "",
    val serialNumber: String = "",
    val category: String = "Industrial Hygiene Instrumentation",
    val parameterMeasured: String = "",
    val lastCalibrationDate: String = "",
    val nextCalibrationDate: String = "",
    val calibrationCertNo: String = "",
    val status: String = "VALID", // VALID, DUE_SOON, EXPIRED
    val notes: String = "",
    val isDemo: Boolean = true
) {
    val name: String get() = equipmentName
    val calibrationStatus: String get() = status
}

typealias EquipmentEntity = Equipment

/**
 * Room entity for 'User' to handle user profiles with CRUD operations.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String = "",
    val email: String = "",
    val fullName: String = "",
    val role: UserRole = UserRole.OH_ASSISTANT,
    val phone: String = "",
    val certificationNumber: String = "",
    val designation: String = "",
    val companyId: Long? = null,
    val isActive: Boolean = true,
    val createdTimestamp: Long = System.currentTimeMillis()
)

typealias UserEntity = User

/**
 * Room entity for 'Company' to handle company setup and details.
 */
@Entity(tableName = "companies")
data class Company(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val shortName: String = "",
    val registrationNumber: String = "",
    val vatNumber: String = "",
    val industryType: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val dmeCode: String = "",
    val mineManager: String = "",
    val managerContact: String = "",
    val isClient: Boolean = false,
    val primaryHegsJson: String = "[]",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)

typealias CompanyEntity = Company

/**
 * Room entity for 'Site' linked to a company, representing an industrial location.
 */
@Entity(tableName = "sites")
data class Site(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val companyId: Long = 0,
    val siteName: String = "",
    val dmeCode: String = "",
    val location: String = "",
    val operationType: String = "",
    val mineManager: String = "",
    val managerContact: String = "",
    val siteHygienist: String = "",
    val createdTimestamp: Long = System.currentTimeMillis()
)

typealias SiteEntity = Site

/**
 * Room entity for 'ClientAccount' to manage industrial client accounts with CRUD.
 */
@Entity(tableName = "client_accounts")
data class ClientAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String = "",
    val accountNumber: String = "",
    val industrySector: String = "",
    val primaryContact: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val physicalAddress: String = "",
    val activeSitesCount: Int = 1,
    val contractStatus: String = "ACTIVE", // ACTIVE, PENDING, INACTIVE
    val createdTimestamp: Long = System.currentTimeMillis()
)

/**
 * Room database entity named 'Assessment' to capture specific occupational hygiene data,
 * including timestamps, hazard types, and exposure measurements, linked to a Site ID.
 */
@Entity(tableName = "occupational_assessments")
data class Assessment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val siteId: Long = 0, // linked to Site ID
    val timestamp: Long = System.currentTimeMillis(), // timestamps
    val hazardType: HazardType = HazardType.NOISE, // hazard types
    val exposureMeasurement: Double = 0.0, // exposure measurements
    val measurementUnit: String = "dB(A)",
    val occupationalExposureLimit: Double = 85.0,
    val isExceedance: Boolean = false, // exposureMeasurement > occupationalExposureLimit
    val assessmentRef: String = "",
    val assessedAreaOrHeg: String = "",
    val employeeName: String = "",
    val employeeNumber: String = "",
    val status: AssessmentStatus = AssessmentStatus.IN_PROGRESS,
    val findingsSummary: String = "",
    val assessorName: String = "",
    val instrumentation: String = "",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Room entity for 'DocumentTemplate' to manage statutory document templates.
 */
@Entity(tableName = "document_templates")
data class DocumentTemplate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateCode: String = "",
    val title: String = "",
    val category: String = "",
    val standardCode: String = "",
    val statutoryLimit: String = "",
    val instrumentRequirement: String = "",
    val isMandatory: Boolean = true,
    val description: String = "",
    val defaultParametersJson: String = "{}"
)
