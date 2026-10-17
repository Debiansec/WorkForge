package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.ActionPriority
import com.example.data.model.ActionStatus
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.HazardType
import com.example.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromAssessmentType(value: AssessmentType): String = value.name

    @TypeConverter
    fun toAssessmentType(value: String): AssessmentType = try {
        AssessmentType.valueOf(value)
    } catch (_: Exception) {
        AssessmentType.PERSONAL_NOISE
    }

    @TypeConverter
    fun fromAssessmentStatus(value: AssessmentStatus): String = value.name

    @TypeConverter
    fun toAssessmentStatus(value: String): AssessmentStatus = try {
        AssessmentStatus.valueOf(value)
    } catch (_: Exception) {
        AssessmentStatus.NOT_STARTED
    }

    @TypeConverter
    fun fromActionPriority(value: ActionPriority): String = value.name

    @TypeConverter
    fun toActionPriority(value: String): ActionPriority = try {
        ActionPriority.valueOf(value)
    } catch (_: Exception) {
        ActionPriority.MEDIUM
    }

    @TypeConverter
    fun fromActionStatus(value: ActionStatus): String = value.name

    @TypeConverter
    fun toActionStatus(value: String): ActionStatus = try {
        ActionStatus.valueOf(value)
    } catch (_: Exception) {
        ActionStatus.OPEN
    }

    @TypeConverter
    fun fromHazardType(value: HazardType): String = value.name

    @TypeConverter
    fun toHazardType(value: String): HazardType = try {
        HazardType.valueOf(value)
    } catch (_: Exception) {
        HazardType.NOISE
    }

    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = try {
        UserRole.valueOf(value)
    } catch (_: Exception) {
        UserRole.OH_ASSISTANT
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String = value?.joinToString(";;") ?: ""

    @TypeConverter
    fun toStringList(value: String?): List<String> = if (value.isNullOrEmpty()) {
        emptyList()
    } else {
        value.split(";;").filter { it.isNotEmpty() }
    }
}
