package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SiteHygieneDatabase
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.EquipmentEntity
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.local.entity.SiteInfoEntity
import com.example.data.local.entity.SiteVisitEntity
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.UserRole
import com.example.data.repository.SiteHygieneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.data.model.AuthUser
import com.example.data.model.CompanySite
import com.example.data.model.DEFAULT_COMPANIES
import com.example.data.model.DEMO_USERS

import com.example.ui.theme.AppTheme

data class SiteHygieneUiState(
    val assessments: List<AssessmentEntity> = emptyList(),
    val actions: List<RemedialActionEntity> = emptyList(),
    val equipment: List<EquipmentEntity> = emptyList(),
    val siteVisits: List<SiteVisitEntity> = emptyList(),
    val siteInfo: SiteInfoEntity = SiteInfoEntity(),
    val currentRole: UserRole = UserRole.OH_ASSISTANT,
    val selectedAssessment: AssessmentEntity? = null,
    val isDarkMode: Boolean = false,
    val selectedTheme: AppTheme = AppTheme.INDUSTRIAL_RED,
    val isLoggedIn: Boolean = false,
    val currentUser: AuthUser? = null,
    val availableCompanies: List<CompanySite> = DEFAULT_COMPANIES,
    val selectedCompany: CompanySite = DEFAULT_COMPANIES.first()
)

class SiteHygieneViewModel(application: Application) : AndroidViewModel(application) {

    private val database = SiteHygieneDatabase.getDatabase(application, viewModelScope)
    private val repository = SiteHygieneRepository(database)

    private val _currentRole = MutableStateFlow(UserRole.OH_ASSISTANT)
    private val _selectedAssessment = MutableStateFlow<AssessmentEntity?>(null)
    private val _isDarkMode = MutableStateFlow(false)
    private val _isLoggedIn = MutableStateFlow(false)
    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    private val _availableCompanies = MutableStateFlow(DEFAULT_COMPANIES)
    private val _selectedCompany = MutableStateFlow(DEFAULT_COMPANIES.first())
    private val _selectedTheme = MutableStateFlow(AppTheme.INDUSTRIAL_RED)

    val uiState: StateFlow<SiteHygieneUiState> = combine(
        repository.allAssessments,
        repository.allActions,
        repository.allEquipment,
        repository.allSiteVisits
    ) { assessments, actions, equipment, visits ->
        SiteHygieneUiState(
            assessments = assessments,
            actions = actions,
            equipment = equipment,
            siteVisits = visits,
            currentRole = _currentRole.value,
            selectedAssessment = _selectedAssessment.value,
            isDarkMode = _isDarkMode.value,
            selectedTheme = _selectedTheme.value,
            isLoggedIn = _isLoggedIn.value,
            currentUser = _currentUser.value,
            availableCompanies = _availableCompanies.value,
            selectedCompany = _selectedCompany.value
        )
    }.combine(repository.siteInfo) { state, siteInfo ->
        val effectiveSiteInfo = siteInfo ?: SiteInfoEntity(
            clientName = _selectedCompany.value.name,
            mineName = _selectedCompany.value.shortName,
            location = _selectedCompany.value.location,
            dmeCode = _selectedCompany.value.dmeCode,
            mineManager = _selectedCompany.value.mineManager,
            managerContact = _selectedCompany.value.managerContact
        )
        state.copy(siteInfo = effectiveSiteInfo)
    }.combine(_currentRole) { state, role ->
        state.copy(currentRole = role)
    }.combine(_selectedAssessment) { state, selected ->
        state.copy(selectedAssessment = selected)
    }.combine(_isDarkMode) { state, dark ->
        state.copy(isDarkMode = dark)
    }.combine(_selectedTheme) { state, theme ->
        state.copy(selectedTheme = theme)
    }.combine(_isLoggedIn) { state, loggedIn ->
        state.copy(isLoggedIn = loggedIn)
    }.combine(_currentUser) { state, user ->
        state.copy(currentUser = user)
    }.combine(_selectedCompany) { state, company ->
        state.copy(selectedCompany = company)
    }.combine(_availableCompanies) { state, companies ->
        state.copy(availableCompanies = companies)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SiteHygieneUiState()
    )

    fun login(email: String, role: UserRole = UserRole.OH_ASSISTANT, user: AuthUser? = null) {
        val resolvedUser = user ?: DEMO_USERS.find { it.email.equals(email.trim(), ignoreCase = true) }
            ?: AuthUser(
                email = email.trim(),
                name = email.substringBefore("@").replace(".", " ").capitalizeWords(),
                role = role,
                certNumber = "MOH-${System.currentTimeMillis() % 10000}",
                designation = role.label
            )
        _currentUser.value = resolvedUser
        _currentRole.value = resolvedUser.role
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    fun selectCompany(company: CompanySite) {
        _selectedCompany.value = company
    }

    fun addCompany(company: CompanySite) {
        if (_availableCompanies.value.none { it.id == company.id || it.dmeCode == company.dmeCode }) {
            _availableCompanies.value = _availableCompanies.value + company
        }
        _selectedCompany.value = company
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setTheme(theme: AppTheme) {
        _selectedTheme.value = theme
    }

    fun createEnvironmentalFieldAssessmentTask(
        type: AssessmentType,
        customTitle: String,
        targetAreaHeg: String,
        notes: String = "",
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val company = _selectedCompany.value
            val site = uiState.value.siteInfo
            val count = uiState.value.assessments.size + 1
            val prefix = when (type) {
                AssessmentType.PERSONAL_NOISE -> "ENV-NOISE"
                AssessmentType.PERSONAL_AIRBORNE -> "ENV-AIR"
                AssessmentType.THERMAL_STRESS -> "ENV-THERMAL"
                AssessmentType.AREA_SOURCE_NOISE -> "ENV-ACOUSTIC"
                AssessmentType.ILLUMINATION_DAY_NIGHT -> "ENV-LIGHT"
                AssessmentType.TMM_ILLUMINATION -> "ENV-TMM"
                AssessmentType.FALLOUT_DUST -> "ENV-DUST"
                AssessmentType.POTABLE_WATER_A -> "ENV-WATER"
                AssessmentType.POTABLE_WATER_B -> "ENV-WATER-B"
                AssessmentType.ABLUTION_FACILITIES -> "ENV-HYGIENE"
            }
            val docRef = "$prefix-2026-${String.format("%03d", count)}"
            val entity = AssessmentEntity(
                documentRef = docRef,
                type = type,
                status = AssessmentStatus.IN_PROGRESS,
                visitDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
                targetAreaOrHeg = targetAreaHeg.ifBlank { company.primaryHegs.firstOrNull() ?: "Quarry Processing Plant" },
                clientName = company.name,
                mineCode = company.dmeCode,
                assessor = site.assessorName.ifBlank { _currentUser.value?.name ?: "Field Assessor" },
                reviewer = site.siteHygienist,
                findingsSummary = notes.ifBlank { "Environmental field task: $customTitle" },
                isDemo = false
            )
            val newId = repository.insertAssessment(entity)
            val created = repository.getAssessmentById(newId) ?: entity.copy(id = newId)
            _selectedAssessment.value = created
            onCreated(newId)
        }
    }

    fun selectAssessmentById(id: Long, onLoaded: (AssessmentEntity) -> Unit = {}) {
        viewModelScope.launch {
            val item = repository.getAssessmentById(id)
            if (item != null) {
                _selectedAssessment.value = item
                onLoaded(item)
            }
        }
    }

    fun startNewAssessment(type: AssessmentType, onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val refCount = uiState.value.assessments.size + 1
            val prefix = when (type) {
                AssessmentType.PERSONAL_NOISE -> "LC-PN"
                AssessmentType.PERSONAL_AIRBORNE -> "LC-PA"
                AssessmentType.THERMAL_STRESS -> "LC-TS"
                AssessmentType.AREA_SOURCE_NOISE -> "LC-AN"
                AssessmentType.ILLUMINATION_DAY_NIGHT -> "LC-IL"
                AssessmentType.TMM_ILLUMINATION -> "LC-TMM"
                AssessmentType.FALLOUT_DUST -> "LC-FD"
                AssessmentType.POTABLE_WATER_A -> "LC-PWA"
                AssessmentType.POTABLE_WATER_B -> "LC-PWB"
                AssessmentType.ABLUTION_FACILITIES -> "LC-AB"
            }
            val docRef = "$prefix-2026-${String.format("%03d", refCount)}"
            val company = _selectedCompany.value
            val site = uiState.value.siteInfo

            val newAssessment = AssessmentEntity(
                documentRef = docRef,
                type = type,
                status = AssessmentStatus.IN_PROGRESS,
                visitDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
                targetAreaOrHeg = company.primaryHegs.firstOrNull() ?: "Operational Plant & Quarry",
                clientName = company.name,
                mineCode = company.dmeCode,
                assessor = site.assessorName,
                reviewer = site.siteHygienist,
                isDemo = false
            )

            val insertedId = repository.insertAssessment(newAssessment)
            val created = repository.getAssessmentById(insertedId) ?: newAssessment.copy(id = insertedId)
            _selectedAssessment.value = created
            onCreated(insertedId)
        }
    }

    fun saveAssessment(assessment: AssessmentEntity) {
        viewModelScope.launch {
            repository.updateAssessment(assessment)
            _selectedAssessment.value = assessment
        }
    }

    fun addAction(action: RemedialActionEntity) {
        viewModelScope.launch {
            repository.insertAction(action)
        }
    }

    fun updateAction(action: RemedialActionEntity) {
        viewModelScope.launch {
            repository.updateAction(action)
        }
    }

    fun addEquipment(equipment: EquipmentEntity) {
        viewModelScope.launch {
            repository.insertEquipment(equipment)
        }
    }
}

private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
}
