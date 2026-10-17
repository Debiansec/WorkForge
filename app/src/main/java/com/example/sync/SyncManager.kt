package com.example.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object SyncManager {

    /**
     * Schedules periodic background sync every 15 minutes whenever network connectivity is available.
     */
    fun schedulePeriodicSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicRequest = PeriodicWorkRequestBuilder<AssessmentSyncWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            AssessmentSyncWorker.WORK_NAME_PERIODIC,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

    /**
     * Triggers an immediate one-time sync as soon as network is connected.
     */
    fun syncNow(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeRequest = OneTimeWorkRequestBuilder<AssessmentSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            AssessmentSyncWorker.WORK_NAME_ONE_TIME,
            ExistingWorkPolicy.REPLACE,
            oneTimeRequest
        )
    }

    /**
     * Returns a Flow observing the current one-time sync state.
     */
    fun getSyncWorkState(context: Context): Flow<SyncState> {
        return WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(AssessmentSyncWorker.WORK_NAME_ONE_TIME)
            .map { workInfoList ->
                val workInfo = workInfoList.firstOrNull()
                when (workInfo?.state) {
                    WorkInfo.State.RUNNING -> SyncState.SYNCING
                    WorkInfo.State.ENQUEUED -> SyncState.QUEUED_FOR_NETWORK
                    WorkInfo.State.SUCCEEDED -> SyncState.SYNCED
                    WorkInfo.State.FAILED -> SyncState.FAILED
                    else -> {
                        // Check last sync timestamp from shared prefs
                        val prefs = context.getSharedPreferences(AssessmentSyncWorker.PREFS_SYNC, Context.MODE_PRIVATE)
                        val lastSync = prefs.getLong(AssessmentSyncWorker.KEY_LAST_SYNC_TIMESTAMP, 0L)
                        if (lastSync > 0) SyncState.SYNCED else SyncState.IDLE
                    }
                }
            }
    }

    /**
     * Human-readable string for the last sync time.
     */
    fun getLastSyncTimeFormatted(context: Context): String {
        val prefs = context.getSharedPreferences(AssessmentSyncWorker.PREFS_SYNC, Context.MODE_PRIVATE)
        val lastSync = prefs.getLong(AssessmentSyncWorker.KEY_LAST_SYNC_TIMESTAMP, 0L)
        if (lastSync == 0L) return "Never synchronized"

        val diffMinutes = (System.currentTimeMillis() - lastSync) / (1000 * 60)
        return when {
            diffMinutes < 1 -> "Just now"
            diffMinutes < 60 -> "$diffMinutes min ago"
            diffMinutes < 1440 -> "${diffMinutes / 60}h ago"
            else -> SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date(lastSync))
        }
    }

    fun getLastSyncedCount(context: Context): Int {
        val prefs = context.getSharedPreferences(AssessmentSyncWorker.PREFS_SYNC, Context.MODE_PRIVATE)
        return prefs.getInt(AssessmentSyncWorker.KEY_SYNCED_COUNT, 0)
    }
}

enum class SyncState(val label: String) {
    IDLE("Ready"),
    QUEUED_FOR_NETWORK("Waiting for Network"),
    SYNCING("Synchronizing..."),
    SYNCED("Synchronized"),
    FAILED("Sync Error")
}
