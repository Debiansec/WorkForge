package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import com.example.data.model.HazardType
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteInfoDao {
    @Query("SELECT * FROM site_info LIMIT 1")
    fun getSiteInfo(): Flow<SiteInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(siteInfo: SiteInfoEntity): Long
}

@Dao
interface SiteVisitDao {
    @Query("SELECT * FROM site_visits ORDER BY visitDate DESC")
    fun getAllSiteVisits(): Flow<List<SiteVisitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiteVisit(visit: SiteVisitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(visits: List<SiteVisitEntity>)
}

@Dao
interface AssessmentDao {
    @Query("SELECT * FROM assessments ORDER BY updatedTimestamp DESC")
    fun getAllAssessments(): Flow<List<AssessmentEntity>>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun getAssessmentById(id: Long): AssessmentEntity?

    @Query("SELECT * FROM assessments WHERE type = :type ORDER BY updatedTimestamp DESC")
    fun getAssessmentsByType(type: String): Flow<List<AssessmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: AssessmentEntity): Long

    @Update
    suspend fun updateAssessment(assessment: AssessmentEntity)

    @Delete
    suspend fun deleteAssessment(assessment: AssessmentEntity)

    @Query("SELECT COUNT(*) FROM assessments")
    suspend fun getCount(): Int

    @Query("SELECT * FROM assessments")
    suspend fun getAllAssessmentsList(): List<AssessmentEntity>
}

@Dao
interface RemedialActionDao {
    @Query("SELECT * FROM remedial_actions ORDER BY createdTimestamp DESC")
    fun getAllActions(): Flow<List<RemedialActionEntity>>

    @Query("SELECT * FROM remedial_actions WHERE id = :id")
    suspend fun getActionById(id: Long): RemedialActionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: RemedialActionEntity): Long

    @Update
    suspend fun updateAction(action: RemedialActionEntity)

    @Delete
    suspend fun deleteAction(action: RemedialActionEntity)

    @Query("SELECT COUNT(*) FROM remedial_actions WHERE status != 'CLOSED'")
    fun getOpenActionsCount(): Flow<Int>
}

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment_register ORDER BY equipmentName ASC")
    fun getAllEquipment(): Flow<List<EquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: EquipmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(equipmentList: List<EquipmentEntity>)

    @Query("SELECT COUNT(*) FROM equipment_register")
    suspend fun getCount(): Int
}

/**
 * Room DAO for User entity to handle CRUD operations.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY fullName ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserByIdSync(id: Long): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Long)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int
}

/**
 * Room DAO for Company entity to handle CRUD operations.
 */
@Dao
interface CompanyDao {
    @Query("SELECT * FROM companies ORDER BY name ASC")
    fun getAllCompanies(): Flow<List<Company>>

    @Query("SELECT * FROM companies WHERE id = :id")
    fun getCompanyById(id: Long): Flow<Company?>

    @Query("SELECT * FROM companies WHERE id = :id")
    suspend fun getCompanyByIdSync(id: Long): Company?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: Company): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanies(companies: List<Company>)

    @Update
    suspend fun updateCompany(company: Company)

    @Delete
    suspend fun deleteCompany(company: Company)

    @Query("DELETE FROM companies WHERE id = :id")
    suspend fun deleteCompanyById(id: Long)

    @Query("SELECT COUNT(*) FROM companies")
    suspend fun getCount(): Int
}

/**
 * Room DAO for Site entity to handle CRUD operations.
 */
@Dao
interface SiteDao {
    @Query("SELECT * FROM sites ORDER BY siteName ASC")
    fun getAllSites(): Flow<List<Site>>

    @Query("SELECT * FROM sites WHERE companyId = :companyId ORDER BY siteName ASC")
    fun getSitesForCompany(companyId: Long): Flow<List<Site>>

    @Query("SELECT * FROM sites WHERE id = :id")
    fun getSiteById(id: Long): Flow<Site?>

    @Query("SELECT * FROM sites WHERE id = :id")
    suspend fun getSiteByIdSync(id: Long): Site?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: Site): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSites(sites: List<Site>)

    @Update
    suspend fun updateSite(site: Site)

    @Delete
    suspend fun deleteSite(site: Site)

    @Query("DELETE FROM sites WHERE id = :id")
    suspend fun deleteSiteById(id: Long)

    @Query("SELECT COUNT(*) FROM sites")
    suspend fun getCount(): Int
}

/**
 * Room DAO for ClientAccount entity to handle CRUD operations.
 */
@Dao
interface ClientAccountDao {
    @Query("SELECT * FROM client_accounts ORDER BY clientName ASC")
    fun getAllClients(): Flow<List<ClientAccount>>

    @Query("SELECT * FROM client_accounts WHERE id = :id")
    fun getClientById(id: Long): Flow<ClientAccount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientAccount): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClients(clients: List<ClientAccount>)

    @Update
    suspend fun updateClient(client: ClientAccount)

    @Delete
    suspend fun deleteClient(client: ClientAccount)

    @Query("DELETE FROM client_accounts WHERE id = :id")
    suspend fun deleteClientById(id: Long)

    @Query("SELECT COUNT(*) FROM client_accounts")
    suspend fun getCount(): Int
}

/**
 * Room DAO for occupational hygiene 'Assessment' entity linked to Site ID.
 */
@Dao
interface OccupationalAssessmentDao {
    @Query("SELECT * FROM occupational_assessments ORDER BY timestamp DESC")
    fun getAllAssessments(): Flow<List<Assessment>>

    @Query("SELECT * FROM occupational_assessments WHERE siteId = :siteId ORDER BY timestamp DESC")
    fun getAssessmentsForSite(siteId: Long): Flow<List<Assessment>>

    @Query("SELECT * FROM occupational_assessments WHERE hazardType = :hazardType ORDER BY timestamp DESC")
    fun getAssessmentsByHazardType(hazardType: HazardType): Flow<List<Assessment>>

    @Query("SELECT * FROM occupational_assessments WHERE id = :id")
    suspend fun getAssessmentById(id: Long): Assessment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: Assessment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessments(assessments: List<Assessment>)

    @Update
    suspend fun updateAssessment(assessment: Assessment)

    @Delete
    suspend fun deleteAssessment(assessment: Assessment)

    @Query("DELETE FROM occupational_assessments WHERE id = :id")
    suspend fun deleteAssessmentById(id: Long)

    @Query("SELECT COUNT(*) FROM occupational_assessments WHERE isExceedance = 1")
    fun getExceedanceCount(): Flow<Int>
}

/**
 * Room DAO for DocumentTemplate entity.
 */
@Dao
interface DocumentTemplateDao {
    @Query("SELECT * FROM document_templates ORDER BY category ASC, title ASC")
    fun getAllTemplates(): Flow<List<DocumentTemplate>>

    @Query("SELECT * FROM document_templates WHERE id = :id")
    fun getTemplateById(id: Long): Flow<DocumentTemplate?>

    @Query("SELECT * FROM document_templates WHERE templateCode = :code LIMIT 1")
    suspend fun getTemplateByCode(code: String): DocumentTemplate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: DocumentTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplates(templates: List<DocumentTemplate>)

    @Update
    suspend fun updateTemplate(template: DocumentTemplate)

    @Delete
    suspend fun deleteTemplate(template: DocumentTemplate)
}
