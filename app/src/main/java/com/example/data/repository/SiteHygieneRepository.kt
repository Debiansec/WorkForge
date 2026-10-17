package com.example.data.repository

import com.example.data.local.SiteHygieneDatabase
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
import kotlinx.coroutines.flow.Flow

class SiteHygieneRepository(private val database: SiteHygieneDatabase) {

    private val siteInfoDao = database.siteInfoDao()
    private val siteVisitDao = database.siteVisitDao()
    private val assessmentDao = database.assessmentDao()
    private val remedialActionDao = database.remedialActionDao()
    private val equipmentDao = database.equipmentDao()
    private val userDao = database.userDao()
    private val companyDao = database.companyDao()
    private val siteDao = database.siteDao()
    private val clientAccountDao = database.clientAccountDao()
    private val occupationalAssessmentDao = database.occupationalAssessmentDao()
    private val documentTemplateDao = database.documentTemplateDao()

    val siteInfo: Flow<SiteInfoEntity?> = siteInfoDao.getSiteInfo()
    val allSiteVisits: Flow<List<SiteVisitEntity>> = siteVisitDao.getAllSiteVisits()
    val allAssessments: Flow<List<AssessmentEntity>> = assessmentDao.getAllAssessments()
    val allActions: Flow<List<RemedialActionEntity>> = remedialActionDao.getAllActions()
    val allEquipment: Flow<List<EquipmentEntity>> = equipmentDao.getAllEquipment()
    val openActionsCount: Flow<Int> = remedialActionDao.getOpenActionsCount()

    // Users
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun deleteUserById(id: Long) = userDao.deleteUserById(id)

    // Companies
    val allCompanies: Flow<List<Company>> = companyDao.getAllCompanies()
    suspend fun insertCompany(company: Company): Long = companyDao.insertCompany(company)
    suspend fun updateCompany(company: Company) = companyDao.updateCompany(company)
    suspend fun deleteCompany(company: Company) = companyDao.deleteCompany(company)
    suspend fun deleteCompanyById(id: Long) = companyDao.deleteCompanyById(id)

    // Sites
    val allSites: Flow<List<Site>> = siteDao.getAllSites()
    fun getSitesForCompany(companyId: Long): Flow<List<Site>> = siteDao.getSitesForCompany(companyId)
    suspend fun insertSite(site: Site): Long = siteDao.insertSite(site)
    suspend fun updateSite(site: Site) = siteDao.updateSite(site)
    suspend fun deleteSite(site: Site) = siteDao.deleteSite(site)
    suspend fun deleteSiteById(id: Long) = siteDao.deleteSiteById(id)

    // Client Accounts
    val allClients: Flow<List<ClientAccount>> = clientAccountDao.getAllClients()
    suspend fun insertClient(client: ClientAccount): Long = clientAccountDao.insertClient(client)
    suspend fun updateClient(client: ClientAccount) = clientAccountDao.updateClient(client)
    suspend fun deleteClient(client: ClientAccount) = clientAccountDao.deleteClient(client)
    suspend fun deleteClientById(id: Long) = clientAccountDao.deleteClientById(id)

    // Occupational Hygiene Assessments (linked to Site ID)
    val allOccupationalAssessments: Flow<List<Assessment>> = occupationalAssessmentDao.getAllAssessments()
    fun getOccupationalAssessmentsForSite(siteId: Long): Flow<List<Assessment>> = occupationalAssessmentDao.getAssessmentsForSite(siteId)
    suspend fun insertOccupationalAssessment(assessment: Assessment): Long = occupationalAssessmentDao.insertAssessment(assessment)
    suspend fun updateOccupationalAssessment(assessment: Assessment) = occupationalAssessmentDao.updateAssessment(assessment)
    suspend fun deleteOccupationalAssessment(assessment: Assessment) = occupationalAssessmentDao.deleteAssessment(assessment)

    // Document Templates
    val allTemplates: Flow<List<DocumentTemplate>> = documentTemplateDao.getAllTemplates()
    suspend fun insertTemplate(template: DocumentTemplate): Long = documentTemplateDao.insertTemplate(template)
    suspend fun updateTemplate(template: DocumentTemplate) = documentTemplateDao.updateTemplate(template)
    suspend fun deleteTemplate(template: DocumentTemplate) = documentTemplateDao.deleteTemplate(template)

    // Core Assessment & Site Methods
    suspend fun saveSiteInfo(info: SiteInfoEntity): Long {
        return siteInfoDao.insertOrUpdate(info)
    }

    suspend fun insertSiteVisit(visit: SiteVisitEntity): Long {
        return siteVisitDao.insertSiteVisit(visit)
    }

    suspend fun getAssessmentById(id: Long): AssessmentEntity? {
        return assessmentDao.getAssessmentById(id)
    }

    suspend fun insertAssessment(assessment: AssessmentEntity): Long {
        return assessmentDao.insertAssessment(assessment)
    }

    suspend fun updateAssessment(assessment: AssessmentEntity) {
        assessmentDao.updateAssessment(assessment)
    }

    suspend fun deleteAssessment(assessment: AssessmentEntity) {
        assessmentDao.deleteAssessment(assessment)
    }

    suspend fun getActionById(id: Long): RemedialActionEntity? {
        return remedialActionDao.getActionById(id)
    }

    suspend fun insertAction(action: RemedialActionEntity): Long {
        return remedialActionDao.insertAction(action)
    }

    suspend fun updateAction(action: RemedialActionEntity) {
        remedialActionDao.updateAction(action)
    }

    suspend fun deleteAction(action: RemedialActionEntity) {
        remedialActionDao.deleteAction(action)
    }

    suspend fun insertEquipment(equipment: EquipmentEntity): Long {
        return equipmentDao.insertEquipment(equipment)
    }
}
